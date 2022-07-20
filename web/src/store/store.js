/**
 * @desc: Vuex管理单一状态树
 * @author: WangAnJie
 * @date: 2021/8/10 19:15
 */
import Vue from 'vue'
import Vuex from 'vuex'
import localState from '@/store/modules/local-state.js'
import sessionState from '@/store/modules/session-state.js'

Vue.use(Vuex)

export default new Vuex.Store({
    modules: {
        localState,
        sessionState
    }
})
