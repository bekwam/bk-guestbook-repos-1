import Vue from "vue";
import Login from "./LoginErrorApp.vue";

Vue.config.productionTip = false;

new Vue({
  render: h => h(Login)
}).$mount("#app");

