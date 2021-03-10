import Vue from "vue";
import Admin from "./AdminApp.vue";
import adminRouter from './adminRouter'
import store from "./store/index.js";
import {extend, ValidationObserver, ValidationProvider} from "vee-validate";
import {max, required} from "vee-validate/dist/rules";

Vue.config.productionTip = false;
Vue.config.devtools = true;

Vue.component("validation-observer", ValidationObserver);
Vue.component("validation-provider", ValidationProvider);

extend("required", {
    ...required,
    message: "{_field_} is required"
});

extend("max", {
    ...max,
    message: "{_field_} must not be longer than {length} characters."
});

new Vue({
    router: adminRouter,
    store,
    render: h => h(Admin)
}).$mount("#app");

