import Vue from 'vue'
import Router from 'vue-router'
import Admin from './views/admin/Admin.vue'
import SysProp from "./views/admin/sysprop/SysProp.vue";
import Entries from "./views/admin/entries/Entries.vue";
import Users from "./views/admin/users/Users.vue";

Vue.use(Router)

export default new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: "/admin/index.html",
      redirect: "/admin"
    },
    {
      path: '/admin',
      name: 'admin',
      component: Admin,
      children: [
        {
          path: "",
          redirect: "entries"
        },
        {
          path: "entries",
          name: "entries",
          component: Entries
        },
        {
          path: "users",
          name: "users",
          component: Users
        },
        {
          path: "sysprop",
          name: "sysprop",
          component: SysProp
        }
      ]
    }
  ]
})
