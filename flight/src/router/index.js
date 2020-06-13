import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Flight from "../components/Flight";
import test from "../components/test";
Vue.use(VueRouter)

  const routes = [
  {
    path: '/',
    name: 'Home',
    redirect: '/flight',
    component: Home
  },
    {
      path: '/test',
      name:'test',
      component: test
    },
  {
    path: '/flight/:dep?/:arr?/:date?',
    name: 'flight',
    component: Flight
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
