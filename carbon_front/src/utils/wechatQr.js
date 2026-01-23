import defaultQr from '@/assets/icon/icon_qrcode.jpeg'

export function getWeChatQrUrl() {
  const fromLocal = window.localStorage.getItem('wechatQrUrl')
  const fromEnv = process.env.VUE_APP_WECHAT_QR_URL
  const v = fromLocal || fromEnv
  return v && String(v).trim() ? String(v).trim() : defaultQr
}

