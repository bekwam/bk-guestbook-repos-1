import Vue from "vue";
import Admin from "./AdminApp.vue";
import adminRouter from './adminRouter'
import store from "./store/index.js";

Vue.config.productionTip = false;
Vue.config.devtools = true;

new Vue({
    router: adminRouter,
    store,
    render: h => h(Admin)
}).$mount("#app");

