<template>
  <div class="login-container">
    <!-- 自然动态背景 -->
    <div class="nature-bg">
      <div class="gradient-layer"></div>
      <div class="sun-glow"></div>
      <div class="leaf-particles">
        <span v-for="n in 15" :key="`leaf-${n}`" :style="getParticleStyle(n)"></span>
      </div>
    </div>
    
    <div class="login-content">
      <!-- 左侧品牌区 -->
      <div class="brand-section">
        <div class="brand-glass">
          <div class="brand-text-wrapper">
            <h1 class="brand-title">CARBON<br/>SYSTEM</h1>
            <div class="brand-line"></div>
            <p class="brand-slogan">
              <span class="highlight">守护</span> 绿色地球<br/>
              共创 <span class="highlight">低碳</span> 未来
            </p>
          </div>
          <div class="eco-circle">
            <div class="circle-inner"></div>
            <div class="circle-leaves">
              <i class="el-icon-grape"></i>
              <i class="el-icon-watermelon"></i>
              <i class="el-icon-cherry"></i>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧表单区 -->
      <div class="form-section">
        <div class="form-header">
          <h2 class="form-title">欢迎登录</h2>
          <p class="form-subtitle">请输入您的账号信息</p>
        </div>

        <el-form 
          ref="loginForm" 
          :model="loginForm" 
          :rules="loginRules" 
          class="nature-form" 
          autocomplete="on">
          
          <el-form-item prop="account">
            <div class="input-group">
              <i class="el-icon-user input-icon"></i>
              <el-input
                ref="account"
                v-model="loginForm.account"
                placeholder="用户名"
                name="account"
                type="text"
                tabindex="1"
                autocomplete="on"
                class="nature-input"
              />
            </div>
          </el-form-item>

          <el-form-item prop="pwd">
            <div class="input-group">
              <i class="el-icon-lock input-icon"></i>
              <el-input
                :key="passwordType"
                ref="pwd"
                v-model="loginForm.pwd"
                :type="passwordType"
                placeholder="密码"
                name="pwd"
                tabindex="2"
                autocomplete="on"
                @keyup.enter.native="handleLogin"
                class="nature-input"
              >
              </el-input>
              <span class="show-pwd-icon" @click="showPwd">
                <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
              </span>
            </div>
          </el-form-item>

          <div class="form-options">
            <el-checkbox v-model="loginForm.rememberMe" class="nature-checkbox">记住我</el-checkbox>
            <span class="forgot-link" @click="clickforgetPassWord">忘记密码?</span>
          </div>

          <button class="nature-btn" @click.prevent="handleLogin">
            <span class="btn-content">
              登 录
            </span>
            <div class="btn-wave"></div>
          </button>

          <div class="form-footer">
            <span>还没有账号?</span>
            <span class="register-link" @click="clickReg">立即注册</span>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import { validUsername } from "@/utils/validate";
import { getLoginPicApi, captchaApi, login } from "@/api/user";
import { getStoreStaff } from "@/libs/public";
import { getWXCodeByUrl, loginByWxCode } from "@/libs/wechat";
import { getWechatConfig } from "@/api/wxApi";
import { getToken, removeToken, setToken } from "@/utils/auth";
import Cookies from "js-cookie";

export default {
  name: "Login",
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!validUsername(value)) {
        callback(new Error("请输入正确的用户名"));
      } else {
        callback();
      }
    };
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6 || value.length > 20) {
        callback(new Error("密码长度应在6-20位之间"));
      } else {
        callback();
      }
    };
    return {
      loginForm: {
        account: "",
        pwd: "",
        rememberMe: false,
      },
      loginRules: {
        account: [{ required: true, trigger: "blur", message: "请输入用户名" }],
        pwd: [{ required: true, trigger: "blur", validator: validatePassword }],
      },
      passwordType: "password",
      loading: false,
      redirect: undefined,
      otherQuery: {},
    };
  },
  watch: {
    $route: {
      handler: function (route) {
        const query = route.query;
        if (query) {
          this.redirect = query.redirect;
          this.otherQuery = this.getOtherQuery(query);
        }
      },
      immediate: true,
    },
  },
  mounted() {
    if (this.loginForm.account === "") {
      this.$refs.account.focus();
    } else if (this.loginForm.pwd === "") {
      this.$refs.pwd.focus();
    }
    const rememberMe = localStorage.getItem('login-remember-me');
    if (rememberMe) {
        this.loginForm = JSON.parse(rememberMe);
        this.loginForm.rememberMe = true;
    }
  },
  methods: {
    getParticleStyle(n) {
      return {
        left: Math.random() * 100 + '%',
        top: Math.random() * 100 + '%',
        animationDelay: Math.random() * 10 + 's',
        opacity: Math.random() * 0.4 + 0.1,
        transform: `scale(${Math.random() * 0.5 + 0.5})`
      }
    },
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.pwd.focus()
      })
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          this.$store.dispatch('user/login', this.loginForm)
            .then(() => {
              if (this.loginForm.rememberMe) {
                 localStorage.setItem('login-remember-me', JSON.stringify(this.loginForm));
              } else {
                 localStorage.removeItem('login-remember-me');
              }
              this.$router.push({ path: this.redirect || '/', query: this.otherQuery });
              this.loading = false;
            })
            .catch((err) => {
              this.loading = false;
              console.error(err);
            });
        } else {
          return false;
        }
      });
    },
    clickReg() {
      this.$router.push({ path: "/reg" });
    },
    clickforgetPassWord() {
      this.$router.push({ path: "/forgetPassWord" });
    },
    getOtherQuery(query) {
      return Object.keys(query).reduce((acc, cur) => {
        if (cur !== "redirect") {
          acc[cur] = query[cur];
        }
        return acc;
      }, {});
    },
  },
};
</script>

<style lang="scss" scoped>
/* 绿色环保主题变量 */
$eco-green: #2E7D32;
$eco-light: #4CAF50;
$eco-pale: #E8F5E9;
$text-dark: #1B5E20;
$text-grey: #546E7A;
$white: #ffffff;

.login-container {
  min-height: 100vh;
  width: 100%;
  background-color: #f1f8e9;
  position: relative;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: 'PingFang SC', 'Helvetica Neue', Arial, sans-serif;
}

/* 自然动态背景 */
.nature-bg {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
  
  .gradient-layer {
    width: 100%;
    height: 100%;
    background: linear-gradient(135deg, #e8f5e9 0%, #c8e6c9 100%);
  }
  
  .sun-glow {
    position: absolute;
    top: -20%;
    left: -10%;
    width: 800px;
    height: 800px;
    background: radial-gradient(circle, rgba(255, 235, 59, 0.2) 0%, transparent 70%);
    filter: blur(50px);
    animation: sunMove 20s ease-in-out infinite alternate;
  }
  
  .leaf-particles {
    span {
      position: absolute;
      width: 10px;
      height: 10px;
      background: rgba(76, 175, 80, 0.2);
      border-radius: 50% 0 50% 0;
      animation: floatLeaf 15s linear infinite;
    }
  }
}

@keyframes sunMove {
  0% { transform: translate(0, 0); }
  100% { transform: translate(50px, 50px); }
}

@keyframes floatLeaf {
  0% { transform: translateY(100vh) rotate(0deg); opacity: 0; }
  20% { opacity: 0.6; }
  80% { opacity: 0.6; }
  100% { transform: translateY(-100px) rotate(360deg); opacity: 0; }
}

/* 主内容卡片 */
.login-content {
  position: relative;
  z-index: 10;
  width: 1000px;
  height: 600px;
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(15px);
  border-radius: 20px;
  display: flex;
  overflow: hidden;
  box-shadow: 0 20px 60px rgba(46, 125, 50, 0.15);
  border: 1px solid rgba(255, 255, 255, 0.8);
}

/* 左侧品牌区 */
.brand-section {
  flex: 1.2;
  position: relative;
  background-image: url('https://images.unsplash.com/photo-1542601906990-b4d3fb778b09?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80'); /* 备用森林图，实际可用CSS渐变替代 */
  background: linear-gradient(135deg, $eco-green 0%, $eco-light 100%);
  overflow: hidden;
  
  .brand-glass {
    width: 100%;
    height: 100%;
    background: rgba(255, 255, 255, 0.1);
    backdrop-filter: blur(5px);
    padding: 60px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    position: relative;
  }
  
  .brand-title {
    font-size: 56px;
    font-weight: 800;
    line-height: 1;
    color: $white;
    margin-bottom: 20px;
    letter-spacing: 2px;
    text-shadow: 0 4px 10px rgba(0,0,0,0.1);
  }
  
  .brand-line {
    width: 60px;
    height: 4px;
    background: $white;
    margin-bottom: 30px;
    border-radius: 2px;
  }
  
  .brand-slogan {
    font-size: 18px;
    color: rgba(255, 255, 255, 0.9);
    line-height: 1.8;
    
    .highlight {
      color: #FFEB3B;
      font-weight: bold;
    }
  }
  
  /* 装饰圆环 */
  .eco-circle {
    position: absolute;
    bottom: -60px;
    right: -60px;
    width: 240px;
    height: 240px;
    
    .circle-inner {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      width: 100%;
      height: 100%;
      border: 40px solid rgba(255, 255, 255, 0.05);
      border-radius: 50%;
    }
    
    .circle-leaves {
      position: absolute;
      top: 40px;
      left: 40px;
      font-size: 24px;
      color: rgba(255, 255, 255, 0.3);
      animation: spin 20s linear infinite;
      
      i {
        position: absolute;
        &:nth-child(1) { top: 0; left: 80px; }
        &:nth-child(2) { top: 120px; left: 0; }
        &:nth-child(3) { top: 120px; left: 160px; }
      }
    }
  }
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

/* 右侧表单区 */
.form-section {
  flex: 1;
  padding: 60px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  background: rgba(255, 255, 255, 0.8);
  
  .form-header {
    margin-bottom: 40px;
    
    .form-title {
      font-size: 32px;
      color: $text-dark;
      font-weight: 700;
      margin-bottom: 8px;
    }
    
    .form-subtitle {
      color: $text-grey;
      font-size: 14px;
    }
  }
}

/* 清新风输入框 */
.nature-form {
  .input-group {
    position: relative;
    margin-bottom: 20px;
    
    .input-icon {
      position: absolute;
      left: 15px;
      top: 14px;
      color: #A5D6A7;
      font-size: 18px;
      z-index: 2;
      transition: color 0.3s;
    }
    
    .nature-input {
      ::v-deep .el-input__inner {
        background: #F1F8E9;
        border: 1px solid transparent;
        border-radius: 12px;
        padding-left: 45px;
        color: $text-dark;
        height: 48px;
        transition: all 0.3s;
        
        &:focus {
          background: $white;
          border-color: $eco-light;
          box-shadow: 0 4px 12px rgba(76, 175, 80, 0.1);
        }
        
        &::placeholder {
          color: #B0BEC5;
        }
      }
    }
    
    &:focus-within {
      .input-icon { color: $eco-green; }
    }
  }
  
  .show-pwd-icon {
    position: absolute;
    right: 15px;
    top: 14px;
    color: #CFD8DC;
    cursor: pointer;
    &:hover { color: $text-grey; }
  }
}

.form-options {
  display: flex;
  justify-content: space-between;
  margin-bottom: 30px;
  
  .nature-checkbox {
    ::v-deep .el-checkbox__label { color: $text-grey; }
    ::v-deep .el-checkbox__inner {
      border-radius: 4px;
      border-color: #CFD8DC;
    }
    ::v-deep .el-checkbox__input.is-checked .el-checkbox__inner {
      background: $eco-green;
      border-color: $eco-green;
    }
    ::v-deep .el-checkbox__input.is-checked + .el-checkbox__label {
      color: $eco-green;
    }
  }
  
  .forgot-link {
    color: $text-grey;
    cursor: pointer;
    font-size: 14px;
    &:hover { color: $eco-green; text-decoration: underline; }
  }
}

/* 清新按钮 */
.nature-btn {
  position: relative;
  width: 100%;
  height: 50px;
  background: linear-gradient(135deg, $eco-light 0%, $eco-green 100%);
  border: none;
  border-radius: 12px;
  color: $white;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  overflow: hidden;
  transition: all 0.3s;
  box-shadow: 0 6px 15px rgba(46, 125, 50, 0.2);
  
  .btn-content {
    position: relative;
    z-index: 2;
  }
  
  .btn-wave {
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent, rgba(255,255,255,0.3), transparent);
    transition: left 0.6s;
  }
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 20px rgba(46, 125, 50, 0.3);
    
    .btn-wave {
      left: 100%;
    }
  }
  
  &:active {
    transform: scale(0.98);
  }
}

.form-footer {
  margin-top: 30px;
  text-align: center;
  color: $text-grey;
  font-size: 14px;
  
  .register-link {
    color: $eco-green;
    margin-left: 8px;
    cursor: pointer;
    font-weight: bold;
    
    &:hover {
      text-decoration: underline;
    }
  }
}

@media screen and (max-width: 900px) {
  .brand-section { display: none; }
  .login-content { width: 90%; max-width: 450px; }
}
</style>
