import Vue from 'vue'
import Router from 'vue-router'
import Home from './views/admin/Home.vue'

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
      name: 'homeAdmin',
      component: Home
    }
  ]
})
