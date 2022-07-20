<!--
  * @description: 登录注册界面
  * @author: angeya
  * @date: 2021/8/9 17:19
 -->
<template>
  <div class="container">
    <h2>欢迎使用电子书共享平台</h2>
    <div class="login-panel">
      <h4>{{ currentAction }}</h4>
      <el-form :model="formData" class="login-form" status-icon :rules="rules" ref="form" label-width="80px">
        <el-form-item label="邮箱" prop="mail">
          <el-input type="email" v-model="formData.mail" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="验证码" prop="verifyCode" v-if="!isLogin">
          <el-input type="text" v-model="formData.verifyCode" autocomplete="off" class="verify-code"></el-input>
          <el-button type="success" size="medium" plain :disabled="codeTime !== 0"
                     class="verify-code-btn" @click="getverifyCode">{{verifyCodeBtnText}}</el-button>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="formData.password" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="password2" v-if="!isLogin">
          <el-input type="password" v-model="formData.password2" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="loginAfterRegister" v-show="!isLogin" label="">注册成功后登录</el-checkbox>
        </el-form-item>
        <el-form-item>
          <el-button class="operate-btn" type="primary" @click="submitForm">{{ currentAction }}</el-button>
          <el-button class="operate-btn" @click="switchPanel">{{ switchAction }}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
const MAIL_PATTERN = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/
const verify_CODE_PATTERN = /^\d{4}$/
export default {
  name: "Login",
  data() {
    let validateMail = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入邮箱'));
      } else if (!MAIL_PATTERN.test(value)) {
        callback(new Error('邮箱格式不正确'));
      } else {
        callback();
      }
    };
    let validateVerifyCode = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入验证码'));
      } else if (!verify_CODE_PATTERN.test(value)) {
        callback(new Error('验证码应为4位数字'));
      } else {
        callback();
      }
    };
    let validatePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else {
        if (this.formData.password !== '') {
          this.$refs.form.validateField('validatePassword2');
        }
        callback();
      }
    };
    let validatePassword2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请确认密码'));
      } else if (value !== this.formData.password) {
        callback(new Error('两次密码不一致'));
      } else {
        callback();
      }
    };
    return {
      isLogin: true, // true-登录，false注册
      loginAfterRegister: true, // 登录成功后注册
      isGetCode: true, // 获取验证码时不需要校验密码等，只需要校验邮箱
      codeTime: 0,
      formData: {
        mail: '',
        verifyCode: '',
        password: '',
        password2: ''
      },
      rules: {
        mail: [
          {validator: validateMail, trigger: 'blur'}
        ],
        verifyCode: [
          {validator: validateVerifyCode, trigger: 'blur'}
        ],
        password: [
          {validator: validatePassword, trigger: 'blur'}
        ],
        password2: [
          {validator: validatePassword2, trigger: 'blur'}
        ]
      }
    };
  },
  computed: {
    currentAction() {
      return this.isLogin ? "登录" : "注册"
    },
    switchAction() {
      return this.isLogin ? "去注册" : "去登录"
    },
    verifyCodeBtnText() {
      return this.codeTime === 0 ? '获取验证码' : `${this.codeTime} s后可重试`
    }
  },
  methods: {
    submitForm() {
      if (this.isLogin) {
        this.login()
      } else {
        this.register()
      }
    },
    /**
     * 登录
     */
    login() {
      let that = this
      this.$refs['form'].validate((valid) => {
        if (valid) {
          this.axios({
            url: 'user/login',
            data: {
              mail: that.formData.mail,
              password: that.formData.password
            },
            method: 'post'
          }).then(res => {
            if (res.data.code === 0) {
              that.$notify.successEx({
                title: '登录成功',
                content: ''
              })
              that.$store.commit('saveUserInfo', {
                userInfo: res.data.data
              })
              this.$router.push({
                name: 'main'
              })
            } else {
              that.$notify.errorEx({
                title: '登录失败',
                content: '用户名或者密码错误！'
              })
            }
          })
        } else {
          return false;
        }
      })
    },
    /**
     * 注册
     */
    register() {
      let that = this
      this.$refs['form'].validate((valid) => {
        if (valid) {
          this.axios({
            url: 'user/createUser',
            data: {
              mail: that.formData.mail,
              verifyCode: that.formData.verifyCode,
              password: that.formData.password
            },
            method: 'post'
          }).then(res => {
            if (res.data.code === 0) {
              that.$notify.successEx({
                title: '注册成功',
                content: ''
              })
              if (this.loginAfterRegister) {
                that.login()
              }
            } else if (res.data.code === 100) {
              that.$notify.errorEx({
                title: '注册失败',
                content: '用户名已经存在！'
              })
            } else if (res.data.code >= 300 ) {
              that.$notify.errorEx({
                title: '注册失败',
                content: res.data.msg
              })
            }
          })
        } else {
          return false;
        }
      });
    },
    /**
     * 获取验证码
     */
    getverifyCode() {
      const that = this
      that.isGetCode = false
      // 验证部分字段，回调参数：验证提示的信息
      this.$refs['form'].validateField('mail', (checkMsg) => {
        if (!checkMsg) {
          this.axios({
            url: 'user/getVerifyCode',
            data: that.formData.mail.replaceAll('@', '.._..'),
            method: 'post'
          }).then(res => {
            if (res.data.code === 0) {
              that.countDown()
            } else {
              that.$notify.errorEx({
                title: '获取验证码失败'
              })
            }
          })
        }
      })
    },
    /**
     * 倒计时
     */
    countDown () {
      this.codeTime = 60
      const timer = setInterval(() => {
        if (this.codeTime > 0) {
          this.codeTime --
        } else {
          clearInterval(timer)
        }
      }, 1000)
    },
    /**
     * 切换登录-注册 面板
     */
    switchPanel() {
      this.isLogin = !this.isLogin
      // this.$refs[formName].resetFields();
    },
  }
}
</script>

<style scoped lang="stylus">
.container
  width 100%
  height: 100%
  display flex
  flex-direction column
  justify-content center
  align-items center
  background-image url("../../assets/book.jpg")

  .login-panel
    background rgba(255, 255, 255, 0.8)
    padding 20px
    border-radius 10px

    .login-form
      width: 360px

      .verify-code
        width 120px
      .verify-code-btn
        margin-left 20px

      .operate-btn
        width: 100px
</style>
