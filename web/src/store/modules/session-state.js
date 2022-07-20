/**
 * @desc:
 * @author: WangAnJie
 * @date: 2021/8/10 19:20
 */

const KEY_SESSION_STORAGE = 'session-storage'

const sessionState = {
    state: {
        userInfo: null,
        classify: null,
        storageInfo: null
    },
    mutations: {
        saveUserInfo(state, payload) {
            state.userInfo = payload.userInfo
        },
        saveClassify(state, payload) {
            state.classify = payload
        },
        saveStorageInfo(state, payload) {
            state.storageInfo = payload
        }
    },
    getters: {
        getUserInfo(state) {
            return state.userInfo
        },
        getClassify(state) {
            return state.classify
        },
        getStorageInfo(state) {
            return state.storageInfo
        },
    }
}

const strState = sessionStorage.getItem(KEY_SESSION_STORAGE)
if (strState && strState.length > 0) {
    Object.assign(sessionState.state, JSON.parse(strState))
}

/**
 * sessionStorage持久化
 */
function save () {
    // 保存数据后注销事件监听器
    window.removeEventListener('beforeunload', save)
    sessionStorage.setItem(KEY_SESSION_STORAGE, JSON.stringify(sessionState.state))
}
// 加载时加事件监听器
window.addEventListener('beforeunload', save)

export default sessionState
