import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import { ValidationProvider, ValidationObserver, extend } from "vee-validate";
import { required, max } from "vee-validate/dist/rules";


Vue.config.productionTip = false

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
  router,
  store,
  render: h => h(App)
}).$mount('#app')
