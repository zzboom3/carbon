<template>
  <div class="navbar">
    <hamburger id="hamburger-container" :is-active="sidebar.opened" class="hamburger-container"
      @toggleClick="toggleSideBar" />

    <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />

    <div class="right-menu">
      <template v-if="device !== 'mobile'">
        <img @click="openMoreQuestion" style="cursor:pointer" src="@/assets/imgs/icon_help.png" class="help-icon" />
        <div class="deliver" />
      </template>
      <el-dropdown class="avatar-container" trigger="click" style="cursor:pointer">
        <div>
          <div class="avatar-wrapper">
            <p class="name">{{ name }}</p>
            <i class="el-icon-caret-bottom" />
            <img v-if="avatar" :src="avatar" class="user-avatar">
            <img v-else src="@/assets/imgs/bg-img.png" class="user-avatar">
          </div>
        </div>
        <el-dropdown-menu slot="dropdown" style="cursor:pointer">
          <router-link to="/">
            <el-dropdown-item>
              <div class="dropdown-item-content">
                <i class="el-icon-s-home" style="color: #2E7D32;"></i>
                <span>首页</span>
              </div>
            </el-dropdown-item>
          </router-link>
          <router-link :to="{ path: '/account/info' }" v-if="!isPhone">
            <el-dropdown-item>
              <div class="dropdown-item-content">
                <i class="el-icon-user" style="color: #2E7D32;"></i>
                <span>基本信息</span>
              </div>
            </el-dropdown-item>
          </router-link>
          <template v-if="!isPhone">
            <el-dropdown-item>
              <div class="dropdown-item-content" @click="goUpddatePwd">
                <i class="el-icon-lock" style="color: #2E7D32;"></i>
                <span>修改密码</span>
              </div>
            </el-dropdown-item>
          </template>
          <router-link :to="{ path: '/account/companyInfo' }" v-if="!isPhone">
            <el-dropdown-item>
              <div class="dropdown-item-content">
                <i class="el-icon-office-building" style="color: #2E7D32;"></i>
                <span>企业信息</span>
              </div>
            </el-dropdown-item>
          </router-link>
          <router-link :to="{ path: '/trade/account/open' }" v-if="!isPhone">
            <el-dropdown-item>
              <div class="dropdown-item-content">
                <i class="el-icon-bank-card" style="color: #2E7D32;"></i>
                <span>交易账户</span>
              </div>
            </el-dropdown-item>
          </router-link>

          <el-dropdown-item divided @click.native="logout">
            <div class="dropdown-item-content justify-center">
              <i class="el-icon-switch-button" style="color: #E53935;"></i>
              <span>退出</span>
            </div>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    
    <el-dialog title="修改密码" :visible.sync="padShow" width="40%" custom-class="nature-dialog">
      <el-form label-position="left" :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px"
        class="demo-ruleForm">
        <el-form-item label="当前密码" prop="oldPassword">
          <el-input type="password" v-model="ruleForm.oldPassword" autocomplete="off" placeholder="请输入原密码"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input type="password" v-model="ruleForm.newPassword" autocomplete="off" placeholder="请输入新密码"></el-input>
        </el-form-item>
        <el-form-item label="重复新密码" prop="isCheckPass">
          <el-input type="password" v-model.number="ruleForm.isCheckPass" placeholder="请确认新密码"></el-input>
        </el-form-item>
        <el-form-item>
          <div class="btn-box">
            <div></div>
            <div class="btn-close" @click="goUpddatePwd()">取消</div>
            <div class="btn-true" @click="putAccountPwd()">保存</div>
          </div>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
import ErrorLog from '@/components/ErrorLog'
import Screenfull from '@/components/Screenfull'
import { unbindApi } from '@/api/wxApi'
import Cookies from 'js-cookie'
import { openUrlInNewWindow } from '@/libs/OpenHelper'
import { putAccountUpdatePassword } from "@/api/tenant";
import md5 from 'js-md5'
import { getAccoutBaseInfo } from "@/api/tenant";
import { logout } from "@/api/user";

export default {
  components: {
    Breadcrumb,
    Hamburger,
    ErrorLog,
    Screenfull,
  },
  data() {
    var oldPassword = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入原密码"));
      }
      callback();
    };
    var newPassword = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入新密码"));
      }
      callback();
    };
    var isCheckPass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value != this.ruleForm.newPassword) {
        callback(new Error("两次输入密码不一致!"));
      }
      callback();
    };
    return {
      padShow: false,
      ruleForm: {
        id: 0,
        oldPassword: "",
        newPassword: "",
        isCheckPass: "",
      },
      isPhone: this.$wechat.isPhone(),
      JavaInfo: '',
      moreQuestUrl: 'https://carbonmsger.feishu.cn/drive/folder/fldcn9rj1BekKj2OvJvzTF7VMRd?from=space_persnoal_filelist',
      rules: {
        oldPassword: [{ validator: oldPassword, trigger: "blur" }],
        newPassword: [{ validator: newPassword, trigger: "blur" }],
        isCheckPass: [{ validator: isCheckPass, trigger: "blur" }],
      },
    }
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'device',
      'name'
    ])
  },
  mounted() {
    this.getBaseInfo();
    this.JavaInfo = JSON.parse(Cookies.get('JavaInfo'))
    let accountName;
    if (this.JavaInfo.accountName) {
      accountName = this.JavaInfo.accountName;
    }
    if (localStorage.getItem("accountName")) {
      accountName = localStorage.getItem("accountName");
    }
    this.$store.dispatch('user/updateName', accountName);
  },
  methods: {
    getBaseInfo() {
      let info = JSON.parse(Cookies.get('JavaInfo'))
      getAccoutBaseInfo(parseInt(info.accountId)).then((res) => {
        let avatar;
        if (!res.avatar || res.avatar == '' || (res.avatar).match(/^[ ]*$/)) {
          avatar = require("@/assets/imgs/icon_accoun_logo.jpg")
        } else {
          avatar = res.avatar;
        }
        localStorage.setItem("avatar", avatar);
        this.$store.dispatch('user/upAvatar', avatar);

      })
        .catch((err) => { });
    },
    onUnbundling() {
      this.$modalSure('解绑微信吗').then(() => {
        unbindApi().then(() => {
          this.$message.success('解绑成功')
        })
      })
    },
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    logout() {
      logout().then(res => {
        this.$router.push(`/login?redirect=${this.$route.fullPath}`)
      })
      this.$store.dispatch('user/logout')
    },
    openMoreQuestion() {
      openUrlInNewWindow(this.moreQuestUrl);
    },
    goUpddatePwd() {
      this.ruleForm.oldPassword = null;
      this.ruleForm.newPassword = null;
      this.ruleForm.isCheckPass = null;
      this.padShow = !this.padShow;
    },
    putAccountPwd() {
      if (!this.ruleForm.oldPassword) {
        return this.$message.error("密码不能为空");
      }
      if (!this.ruleForm.newPassword) {
        return this.$message.error("新密码不能为空");
      }
      if (this.ruleForm.isCheckPass != this.ruleForm.newPassword) {
        return this.$message.error("两次输入密码不一致!");
      }
      let datas = {
        id: this.JavaInfo.accountId,
        newPassword: md5(this.ruleForm.newPassword),
        oldPassword: md5(this.ruleForm.oldPassword),
      };
      putAccountUpdatePassword(datas).then((res) => {
        this.$message({
          message: '修改成功',
          type: 'success'
        });
        this.padShow = !this.padShow;
      });
    },
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 64px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, .08);

  .hamburger-container {
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
    height: 100%;
    display: flex;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .dropdown-item-content {
    display: flex;
    align-items: center;
    margin: 8px 0;
    
    i {
      font-size: 18px;
      margin-right: 8px;
    }
    
    span {
      font-size: 14px;
      color: #5a5e66;
    }
    
    &.justify-center {
      justify-content: center;
    }
  }

  .btn-box {
    display: flex;
    text-align: center;
    line-height: 32px;
    justify-content: flex-end;

    .btn-close {
      width: 80px;
      height: 32px;
      border-radius: 4px;
      border: 1px solid #D0D5DE;
      margin: 0 10px;
      cursor: pointer;
      background: #fff;
      transition: all 0.3s;
      
      &:hover {
        border-color: #2E7D32;
        color: #2E7D32;
      }
    }

    .btn-true {
      width: 80px;
      height: 32px;
      background: #2E7D32;
      border-radius: 4px;
      color: white;
      cursor: pointer;
      transition: all 0.3s;
      
      &:hover {
        background: #1B5E20;
      }
    }
  }

  .right-menu {
    display: flex;
    flex-direction: row;
    float: right;
    height: 100%;
    padding-right: 17px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .help-icon {
      width: 24px;
      height: 24px;
      vertical-align: middle;
      margin: auto;
      transition: transform 0.3s;
      
      &:hover {
        transform: scale(1.1);
      }
    }

    .deliver {
      align-self: center;
      margin-left: 20px;
      margin-right: 20px;
      width: 1px;
      height: 34px;
      border: 1px solid #E3E7EB;
    }

    .avatar-container {
      padding-left: 12px;
      min-width: 134px;
      height: 38px;
      vertical-align: middle;
      background: #F1F8E9;
      border-radius: 19px;
      margin: auto;
      transition: all 0.3s;
      
      &:hover {
        background: #E8F5E9;
        box-shadow: 0 2px 8px rgba(46, 125, 50, 0.15);
      }

      .avatar-wrapper {
        display: flex;
        flex-direction: row;
        align-items: center;
        padding-right: 5px;
      }

      .name {
        margin: auto;
        cursor: pointer;
        color: #2E7D32;
        font-weight: 500;
        margin-right: 5px;
      }

      .el-icon-caret-bottom {
        margin: auto;
        color: #2E7D32;
      }

      .user-avatar {
        cursor: pointer;
        width: 32px;
        height: 32px;
        border-radius: 50%;
        border: 2px solid #fff;
        margin-left: 10px;
      }
    }
  }
}
</style>
