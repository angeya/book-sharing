import Vue from 'vue'
import App from './App.vue'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import router from './router/router'
import axios from 'axios'
import notification from './js/notification.js'
import em from './js/event-manager.js'
import store from './store/store'

Vue.config.productionTip = false

Vue.use(ElementUI);
notification.register(Vue)

// 如果是开发环境，则加上api,触发代理
Vue.prototype.axios = axios.create({
  baseURL: process.env.NODE_ENV !== 'production' ? '/api/' : '',
  crossDomain: true,
});
Vue.prototype.em = em
new Vue({
  store,
  router,
  render: h => h(App),
}).$mount('#app');
