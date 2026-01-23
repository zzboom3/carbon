<template>
  <div id="app">
    <router-view />
    <ai-assistant v-if="enableAiAssistant" />
  </div>
</template>

<script>
import AiAssistant from "@/components/AiAssistant";
import { mapState } from "vuex";
export default {
  name: "App",
  components: {
    AiAssistant,
  },
  computed: {
    ...mapState({
      enableAiAssistant: (state) => state.settings.enableAiAssistant,
    }),
  },
  mounted() {
    // var myToken = window.localStorage.getItem("carbonToken");
    // var myCookie = window.localStorage.getItem("carbonCookie");
    // if (myToken && myCookie) {
    //   Cookies.set("JavaInfo", JSON.stringify(myCookie));
    //   setToken(JSON.stringify(myToken));
    //   this.$store.dispatch("user/setToken", myToken);
    // }
    var loginData = localStorage.getItem("login-form");
    if (loginData) {
      this.$store.dispatch("user/login", JSON.parse(loginData)).then(() => {
        this.$router.push({
          path: this.redirect || "/",
          query: this.otherQuery,
        });
      });
    }
  },
  //   methods:{
  //     async layout(e){
  //       if(!this.$store.state.user.isRemember){
  //         await this.$store.dispatch("user/logout");
  //         window.close()
  //       }
  //     }
  //   }
  // }
};
</script>
