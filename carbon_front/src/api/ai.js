export function getAuthToken(store) {
  const token = store && store.getters && store.getters.token
  return token || sessionStorage.getItem('token') || ''
}

import SettingMer from '@/utils/settingMer'

export async function streamChat({ store, messages, onDelta, onError, signal }) {
  const token = getAuthToken(store)
  const url = (SettingMer.apiBaseURL || '').replace(/\/+$/, '') + '/ai/chat/stream'
  const resp = await fetch(url, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      token: token
    },
    body: JSON.stringify({ messages, stream: true }),
    signal
  })

  if (!resp.ok) {
    const text = await resp.text()
    throw new Error(text || `HTTP ${resp.status}`)
  }

  const reader = resp.body.getReader()
  const decoder = new TextDecoder('utf-8')
  let buffer = ''

  while (true) {
    const { value, done } = await reader.read()
    if (done) break
    buffer += decoder.decode(value, { stream: true })

    const parts = buffer.split('\n')
    buffer = parts.pop() || ''

    for (const line of parts) {
      const trimmed = line.trim()
      if (!trimmed) continue

      if (trimmed.startsWith('event:')) {
        continue
      }
      if (!trimmed.startsWith('data:')) {
        continue
      }
      const data = trimmed.slice(5).trim()
      if (!data) continue
      if (data === '[DONE]') return
      if (onDelta) onDelta(data.replace(/\\n/g, '\n'))
    }
  }
}
