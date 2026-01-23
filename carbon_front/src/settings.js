// 请求接口地址 如果没有配置自动获取当前网址路径
const VUE_APP_API_URL = process.env.VUE_APP_BASE_API || `${location.origin}/api/`;
const AI_ASSISTANT_ENABLED_RAW = String(process.env.VUE_APP_ENABLE_AI_ASSISTANT || '').trim().toLowerCase()
const AI_ASSISTANT_ENABLED = AI_ASSISTANT_ENABLED_RAW
  ? (AI_ASSISTANT_ENABLED_RAW === 'true' || AI_ASSISTANT_ENABLED_RAW === '1' || AI_ASSISTANT_ENABLED_RAW === 'yes')
  : true
module.exports = {
  // 接口请求地址
  apiBaseURL: VUE_APP_API_URL,

  title: '碳信使资产管理平台',

  enableAiAssistant: AI_ASSISTANT_ENABLED,

  /**
   * @type {boolean} true | false
   * @description Whether show the settings right-panel
   */
  showSettings: false,

  /**
   * @type {boolean} true | false
   * @description Whether need tagsView
   */
  tagsView: true,

  /**
   * @type {boolean} true | false
   * @description Whether fix the header
   */
  fixedHeader: false,

  /**
   * @type {boolean} true | false
   * @description Whether show the logo in sidebar
   */
  sidebarLogo: true,

  /**
   * @type {string | array} 'production' | ['production', 'development']
   * @description Need show err logs component.
   * The default is only used in the production env
   * If you want to also use it in dev, you can pass ['production', 'development']
   */
  errorLog: 'production'
}
