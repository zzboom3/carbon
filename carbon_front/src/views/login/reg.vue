<template>
  <div class="register-container">
    <!-- 自然动态背景 -->
    <div class="nature-bg">
      <div class="gradient-layer"></div>
      <div class="sun-glow"></div>
      <div class="leaf-particles">
        <span v-for="n in 15" :key="`leaf-${n}`" :style="getParticleStyle(n)"></span>
      </div>
    </div>

    <div class="register-card">
      <div class="card-header">
        <h2 class="title">创建新账户</h2>
        <div class="header-line"></div>
        <p class="subtitle">加入碳资产管理，开启绿色未来</p>
      </div>

      <el-form :model="regForm" ref="regForm" :rules="loginRules" class="nature-form">
        <!-- 用户名 -->
        <el-form-item prop="accountName">
          <div class="input-group">
            <i class="el-icon-user input-icon"></i>
            <el-input 
              v-model="regForm.accountName" 
              placeholder="请输入用户名" 
              @blur="getIsAccountName"
              class="nature-input">
            </el-input>
          </div>
        </el-form-item>

        <el-form-item prop="email">
          <div class="input-group">
            <i class="el-icon-message input-icon"></i>
            <el-input
              v-model="regForm.email"
              placeholder="请输入邮箱"
              class="nature-input"
            >
            </el-input>
          </div>
        </el-form-item>

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
                placeholder="邮箱验证码" 
                class="nature-input">
              </el-input>
            </div>
            <button 
              class="code-btn" 
              :disabled="!sendAuthCode" 
              @click.prevent="handlecode">
              {{ sendAuthCode ? '获取邮箱验证码' : `${auth_time}s` }}
            </button>
          </div>
        </el-form-item>

        <!-- 密码 -->
        <el-form-item prop="password">
          <div class="input-group">
            <i class="el-icon-lock input-icon"></i>
            <el-input 
              :type="passwordType" 
              v-model="regForm.password" 
              placeholder="请输入6-16位密码" 
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
              placeholder="请再次输入密码" 
              show-password
              class="nature-input">
            </el-input>
          </div>
        </el-form-item>

        <!-- 注册按钮 -->
        <button class="nature-btn" @click.prevent="handleNext">
          <span class="btn-content">立即注册</span>
          <div class="btn-wave"></div>
        </button>

        <!-- 底部链接 -->
        <div class="footer-links">
          <span>已有账户？</span>
          <span class="login-link" @click="handleLogin">立即登录</span>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import { register, regCode, getAuthCenterAuthVerify } from '@/api/user'
import { MessageBox } from 'element-ui'

export default {
  name: 'Register',
  data() {
    return {
      regForm: {
        mobile: '',
        email: '',
        verificationCode: '',
        password: '',
        confirmPassword: '',
        company: '',
        accountName: '',
        verifyAccountName: ''
      },
      loginRules: {
         mobile: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
         accountName: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
         email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }],
         verificationCode: [
           { required: true, message: '请输入邮箱验证码', trigger: 'blur' },
           { pattern: /^\d{6}$/, message: '验证码为6位数字', trigger: 'blur' }
         ],
         password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
         confirmPassword: [{ required: true, message: '请确认密码', trigger: 'blur' }]
      },
      passwordType: 'password',
      loading: false,
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
    getAuthCode: function() {
      this.sendAuthCode = false
      this.auth_time = 60
      var timetimer = setInterval(() => {
        this.auth_time--
        if (this.auth_time <= 0) {
          this.sendAuthCode = true
          clearInterval(timetimer)
        }
      }, 1000)
    },
    verifyPhoneNumberFormat(str){
      var myreg = /^(((1[0-9][0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
      return myreg.test(str);
    },
    getIsAccountName(){
      if(!this.regForm.accountName || (this.regForm.accountName == this.verifyAccountName)){
        return
      }
      let param = this.regForm.accountName;
      getAuthCenterAuthVerify(param).then((res)=>{
        if(res && res.code == 200){
          this.$message.error("用户名已被注册，请重新输入用户名")
        }else{
          this.verifyAccountName = this.regForm.accountName
        }
      })
    },
    handlecode() {
      if (this.regForm.email !== '') {
        this.getAuthCode()
        regCode(this.regForm.email).then((res, err) => {
          if (err) {
             this.$message.error(err)
          } else {
            this.$message.success(res.msg)
          }
        }).catch(err => {
          this.$message.error(err.msg || '发送失败')
        })
      } else {
        return this.$message.error('请输入邮箱')
      }
    },
    handleNext() {
      this.$refs.regForm.validate(valid => {
        if (valid) {
          if (this.regForm.password !== this.regForm.confirmPassword) {
            this.$message.error("两次输入的密码不一致，请重新输入！")
            return
          }
          var data = {
            "accountName": this.regForm.accountName,
            "code":this.regForm.verificationCode,
            "password": this.regForm.password,
            "confirmPassword": this.regForm.confirmPassword,
            "phone": this.regForm.mobile,
            "email": this.regForm.email
          }
          this.loading = true;
          register(data)
            .then((res) => {
              this.loading = false;
              if (res.code === 200) {
                 MessageBox.alert('恭喜您注册成功，请直接登录。', '提示', {
                  confirmButtonText: '确定',
                  callback: action => {
                    this.$router.push({ path: '/login' })
                  }
                })
              } else {
                 this.$message.error(res.msg || '注册失败');
              }
            })
            .catch((err) => {
              this.loading = false;
              console.error(err)
            })
        } else {
          console.log('error submit!!')
          return false
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

.register-container {
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

.register-card {
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

@media screen and (max-width: 600px) {
  .register-card {
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
