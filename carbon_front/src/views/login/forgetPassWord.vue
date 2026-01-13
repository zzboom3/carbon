<template>
  <div class="forgot-container">
    <!-- 自然动态背景 -->
    <div class="nature-bg">
      <div class="gradient-layer"></div>
      <div class="sun-glow"></div>
      <div class="leaf-particles">
        <span v-for="n in 15" :key="`leaf-${n}`" :style="getParticleStyle(n)"></span>
      </div>
    </div>

    <div class="forgot-card">
      <div class="card-header">
        <h2 class="title">重置密码</h2>
        <div class="header-line"></div>
        <p class="subtitle">找回您的账号密码，重新连接绿色世界</p>
      </div>

      <div v-if="!nextShow">
        <el-form :model="regForm" ref="regForm" :rules="loginRules" class="nature-form">
          <!-- 手机号 -->
          <el-form-item prop="mobile">
            <div class="input-group">
              <i class="el-icon-mobile-phone input-icon"></i>
              <el-input 
                v-model="regForm.mobile" 
                placeholder="请输入11位手机号" 
                maxlength="11"
                class="nature-input">
              </el-input>
            </div>
          </el-form-item>

          <!-- 验证码 -->
          <el-form-item prop="verificationCode">
            <div class="code-wrapper">
              <div class="input-group" style="flex:1">
                <i class="el-icon-message input-icon"></i>
                <el-input 
                  v-model="regForm.verificationCode" 
                  placeholder="验证码" 
                  class="nature-input">
                </el-input>
              </div>
              <button 
                class="code-btn" 
                :disabled="!sendAuthCode" 
                @click.prevent="handlecode">
                {{ sendAuthCode ? '获取验证码' : `${auth_time}s` }}
              </button>
            </div>
          </el-form-item>

          <!-- 新密码 -->
          <el-form-item prop="password">
            <div class="input-group">
              <i class="el-icon-lock input-icon"></i>
              <el-input 
                :type="passwordType" 
                v-model="regForm.password" 
                placeholder="请输入6-16位新密码" 
                show-password
                class="nature-input">
              </el-input>
            </div>
          </el-form-item>

          <!-- 确认密码 -->
          <el-form-item prop="confirmPassword">
            <div class="input-group">
              <i class="el-icon-lock input-icon"></i>
              <el-input 
                :type="passwordType" 
                v-model="regForm.confirmPassword" 
                placeholder="请确认新密码" 
                show-password
                class="nature-input">
              </el-input>
            </div>
          </el-form-item>

          <!-- 确认按钮 -->
          <button class="nature-btn" @click.prevent="handleNext">
            <span class="btn-content">确认重置</span>
            <div class="btn-wave"></div>
          </button>

          <!-- 底部链接 -->
          <div class="footer-links">
            <span>想起密码了？</span>
            <span class="login-link" @click="handleLogin">立即登录</span>
          </div>
        </el-form>
      </div>

      <!-- 成功状态 -->
      <div v-else class="success-state">
        <div class="success-icon">
          <i class="el-icon-check"></i>
        </div>
        <h3>重置成功</h3>
        <p>您的密码已成功重置，请使用新密码登录</p>
        <button class="nature-btn" @click="handleLogin">
          <span class="btn-content">去登录</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { regForgotPasswordCode, putForgotPassword } from '@/api/user'

export default {
  name: 'ForgetPassword',
  data() {
    return {
      regForm: {
        mobile: '',
        verificationCode: '',
        password: '',
        confirmPassword: ''
      },
      loginRules: {
        mobile: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
        verificationCode: [{ required: true, message: '请输入验证码', trigger: 'blur' }],
        password: [{ required: true, message: '请输入新密码', trigger: 'blur' }],
        confirmPassword: [{ required: true, message: '请确认新密码', trigger: 'blur' }]
      },
      passwordType: 'password',
      loading: false,
      nextShow: false,
      sendAuthCode: true,
      auth_time: 60,
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
    getAuthCode() {
      this.sendAuthCode = false
      this.auth_time = 60
      const timer = setInterval(() => {
        this.auth_time--
        if (this.auth_time <= 0) {
          this.sendAuthCode = true
          clearInterval(timer)
        }
      }, 1000)
    },
    verifyPhoneNumberFormat(str) {
      const myreg = /^(((1[0-9][0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
      return myreg.test(str);
    },
    handlecode() {
      if (this.regForm.mobile) {
        if (!this.verifyPhoneNumberFormat(this.regForm.mobile)) {
          return this.$message.error('手机格式错误！')
        }
        this.getAuthCode()
        regForgotPasswordCode(this.regForm.mobile).then((res) => {
           this.$message.success(res.msg || '验证码已发送')
        }).catch(err => {
          this.$message.error(err.msg || '发送失败')
        })
      } else {
        this.$message.error('请输入手机号')
      }
    },
    handleNext() {
      this.$refs.regForm.validate(valid => {
        if (valid) {
          if (this.regForm.password !== this.regForm.confirmPassword) {
             this.$message.error('两次输入的密码不一致')
             return
          }
          
          const data = {
            "confirmPassword": this.regForm.confirmPassword,
            "code": this.regForm.verificationCode,
            "password": this.regForm.password,
            "phone": this.regForm.mobile
          }
          
          this.loading = true
          putForgotPassword(data)
            .then((res) => {
              this.loading = false
              if (res.code === 200) {
                this.$message.success('密码重置成功')
                this.nextShow = true
              } else {
                this.$message.error(res.msg || '重置失败')
              }
            })
            .catch((err) => {
              this.loading = false
              console.error(err)
            })
        }
      })
    },
    handleLogin() {
      this.$router.push({ path: '/login' })
    }
  }
}
</script>

<style lang="scss" scoped>
/* 绿色环保主题变量 */
$eco-green: #2E7D32;
$eco-light: #4CAF50;
$eco-pale: #E8F5E9;
$text-dark: #1B5E20;
$text-grey: #546E7A;
$white: #ffffff;

.forgot-container {
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

.forgot-card {
  position: relative;
  z-index: 1;
  width: 500px;
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(15px);
  border-radius: 24px;
  padding: 50px;
  box-shadow: 0 20px 60px rgba(46, 125, 50, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.6);
  
  .card-header {
    text-align: center;
    margin-bottom: 40px;
    
    .title {
      font-size: 32px;
      color: $text-dark;
      font-weight: 700;
      margin-bottom: 10px;
    }
    
    .header-line {
      width: 40px;
      height: 4px;
      background: $eco-light;
      margin: 0 auto 10px;
      border-radius: 2px;
    }
    
    .subtitle {
      font-size: 14px;
      color: $text-grey;
    }
  }
}

.nature-form {
  .input-group {
    position: relative;
    margin-bottom: 10px;
    
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
}

.code-wrapper {
  display: flex;
  gap: 15px;
  align-items: flex-start;
  
  .code-btn {
    height: 48px;
    width: 120px;
    background: $white;
    border: 1px solid $eco-light;
    color: $eco-green;
    font-weight: bold;
    border-radius: 12px;
    cursor: pointer;
    transition: all 0.3s;
    
    &:hover {
      background: $eco-pale;
      box-shadow: 0 4px 10px rgba(76, 175, 80, 0.15);
    }
    
    &:disabled {
      opacity: 0.6;
      cursor: not-allowed;
      background: #f5f5f5;
      color: #999;
      border-color: #ddd;
    }
  }
}

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
  margin-top: 20px;
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

.footer-links {
  margin-top: 30px;
  text-align: center;
  color: $text-grey;
  font-size: 14px;
  
  .login-link {
    color: $eco-green;
    margin-left: 8px;
    cursor: pointer;
    font-weight: bold;
    
    &:hover {
      text-decoration: underline;
    }
  }
}

.success-state {
  text-align: center;
  padding: 20px 0;
  
  .success-icon {
    width: 80px;
    height: 80px;
    background: $eco-pale;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 auto 30px;
    color: $eco-green;
    font-size: 40px;
    animation: popIn 0.5s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  }
  
  h3 {
    font-size: 24px;
    color: $text-dark;
    margin-bottom: 15px;
  }
  
  p {
    color: $text-grey;
    margin-bottom: 40px;
  }
}

@keyframes popIn {
  0% { transform: scale(0); opacity: 0; }
  100% { transform: scale(1); opacity: 1; }
}

@media screen and (max-width: 600px) {
  .forgot-card {
    width: 100%;
    height: 100vh;
    border-radius: 0;
    padding: 30px 20px;
    display: flex;
    flex-direction: column;
    justify-content: center;
  }
}
</style>
