<!--
  * @description:
  * @author: angeya
  * @date: 2021/8/10 9:57
 -->
<template>
  <div class="container">
    <header class="header-container">
      <div class="welcome-container">
        <h2>&emsp;欢迎使用电子书共享平台！</h2>
      </div>
      <div class="user-info-container">
        <el-popover placement="bottom" trigger="hover">
          <el-button type="success" @click="applyStorage" class="logout-btn">申请上传空间</el-button>
          <el-button type="info" @click="logout" class="logout-btn">注销登录</el-button>
          <div slot="reference" class="user-info">
            <h4>{{ userInfo.name }}</h4>
            <h4>身份：{{ userTypes[userInfo.role] }}</h4>
          </div>
        </el-popover>
      </div>
      <div class="user-storage-container">
        <div class="storage-detail">
          <p>已用空间: {{ storageInfo.usedSpace }}</p>
          <p>全部空间: {{ storageInfo.totalSpace }}</p>
        </div>
        <el-progress :text-inside="true" :stroke-width="18" :percentage="storageInfo.percentage"
                     status="success">ok
        </el-progress>
      </div>
    </header>
    <main class="main-container">
      <el-menu
          :default-active="activeTab"
          @select="handleSelect">
        <el-menu-item v-for="(item, key) in menuItems" :key="key" :index="item.name">
          <em :class="item.iconClass"></em>
          <span>{{ item.label }}</span>
        </el-menu-item>
      </el-menu>
      <router-view></router-view>
    </main>
    <div class="dialog-container">
      <el-dialog
          title="提示"
          :visible.sync="isShowDialog"
          width="30%">
        <p>功能暂未上线，可联系管理员（sunnyajie@163.com）增加存储空间！</p>
        <p>(限制上传空间的目的是为了防止多度上传导致服务器存储空间不足，给您带来不便，敬请谅解！)</p>
        <span slot="footer" class="dialog-footer">
                    <el-button @click="isShowDialog = false">取 消</el-button>
                    <el-button type="primary" @click="isShowDialog = false">got it</el-button>
                </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
const MENU_ITEMS = [
  {
    name: 'bookList',
    label: '列表浏览',
    iconClass: 'el-icon-document'
  }, {
    name: 'bookUpload',
    label: '上传电子书',
    iconClass: 'el-icon-upload'
  }, {
    name: 'bookManagement',
    label: '电子书管理',
    iconClass: 'el-icon-menu'
  }
]

import consts from '@/js/constant/consts.js'
import tools from '@/js/tools.js'

export default {
  name: "Main",
  data() {
    return {
      userInfo: {
        name: '',
        role: 0
      },
      isShowDialog: false,
      menuItems: MENU_ITEMS,
      userTypes: consts.USER_TYPES,
      storageInfo: {
        usedSpace: '',
        totalSpace: '',
        percentage: 0
      },
      activeTab: MENU_ITEMS[0].name
    }
  },
  methods: {
    /**
     * 菜单选择
     */
    handleSelect(name) {
      this.$router.push({
        name
      })
    },
    /**
     * 注销登录
     */
    logout() {
      const that = this
      this.axios({
        url: '/user/logout',
        method: 'get'
      }).then((res) => {
        if (res.data.code === consts.REQUEST_SUCCESS) {
          that.toLoginPage()
        }
      })
    },
    /**
     * 检查登录状态
     */
    checkLogin() {
      const that = this
      this.axios({
        url: '/user/isLogin',
        method: 'get'
      }).then((res) => {
        if (res.data !== null && res.data.code === consts.REQUEST_SUCCESS) {
          this.userInfo = this.$store.getters.getUserInfo
          this.getStorageInfo()
        } else {
          that.toLoginPage()
        }
      })
    },
    /**
     * 跳转到登录页面
     */
    toLoginPage() {
      this.$router.push({
        name: "login"
      })
    },
    /**
     * 设置当前活动的导航页面
     */
    setActiveTab() {
      this.activeTab = this.$route.name
    },
    /**
     * 获取存储空间信息
     */
    getStorageInfo() {
      const that = this
      this.axios({
        url: '/user/getStorageInfo',
        method: 'get'
      }).then((res) => {
        const data = res.data
        if (data.code === consts.REQUEST_SUCCESS) {
          that.storageInfo.usedSpace = tools.kToMFormat(data.data.usedSpace)
          that.storageInfo.totalSpace = tools.kToMFormat(data.data.totalSpace)
          that.storageInfo.percentage = Number((data.data.usedSpace / data.data.totalSpace * 100).toFixed(1))
          that.$store.commit('saveStorageInfo', data.data)
        }
      })
    },
    /**
     * 申请存储空间
     */
    applyStorage() {
      this.isShowDialog = true
    }
  },
  created() {
    this.checkLogin()
    this.setActiveTab()
    this.em.bind(consts.UPDATE_STORAGE_INFO, this.getStorageInfo)
  },
  beforeDestroy() {
    this.em.unbind(consts.UPDATE_STORAGE_INFO, this.getStorageInfo)
  }
}
</script>

<style scoped lang="stylus">
.container
  height 100%

  .user-info-container
    display flex
    flex-direction row-reverse

  .header-container
    height 79px
    display flex
    flex-direction row
    border-bottom 1px #ccc solid

    .welcome-container
      display flex
      align-items center
      width calc(100% - 350px)

    .user-info-container
      display flex
      flex-direction column
      justify-content center

      .user-info
        width 200px
        cursor pointer
        text-align center

    .user-storage-container
      display flex
      flex-direction column
      justify-content center
      width 150px
      margin-right 10px

      .storage-detail
        font-size 14px

  .main-container
    height calc(100% - 80px)
    display flex
    flex-direction row
</style>
