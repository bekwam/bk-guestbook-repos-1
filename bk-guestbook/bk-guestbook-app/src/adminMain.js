import Vue from "vue";
import Admin from "./AdminApp.vue";
import adminRouter from './adminRouter'
import store from "./store/index.js";
import {extend, ValidationObserver, ValidationProvider} from "vee-validate";
import {max, required} from "vee-validate/dist/rules";
import { library } from "@fortawesome/fontawesome-svg-core";
import {
    faEdit,
    faTrash
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

library.add(faEdit);
library.add(faTrash);

Vue.config.productionTip = false;
Vue.config.devtools = true;

Vue.component("validation-observer", ValidationObserver);
Vue.component("validation-provider", ValidationProvider);
Vue.component("font-awesome-icon", FontAwesomeIcon);

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

