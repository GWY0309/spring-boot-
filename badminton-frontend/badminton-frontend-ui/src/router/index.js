import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import MainLayout from '../layout/MainLayout.vue' // 导入主布局
import HomeView from '../views/HomeView.vue'     // 导入主页
import { useUserStore } from '@/stores/user' // 导入 Pinia store
import CourtListView from '../views/CourtListView.vue';
import MyReservationsView from '../views/MyReservationsView.vue';
import MyRentalsView from '../views/MyRentalsView.vue';
import RacketRentalView from '../views/RacketRentalView.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'login',
      component: LoginView
    },
    {
      path: '/home', // 登录后所有页面的父路径
      component: MainLayout, // 使用主布局
      redirect: '/home/dashboard', // 访问 /home 时自动重定向到 /home/dashboard
      children: [
        {
          path: 'dashboard',
          name: 'home',
          component: HomeView
        },
        // --- 在这里添加新的路由 ---
        {
          path: 'courts', // 访问路径是 /home/courts
          name: 'court-list',
          component: CourtListView
        },
        {
          path: 'courts',
          name: 'court-list',
          component: CourtListView
        },
        {
          path: 'my-reservations', // 访问路径是 /home/my-reservations
          name: 'my-reservations',
          component: MyReservationsView
        },
        {
          path: 'my-rentals', // 访问路径是 /home/my-rentals
          name: 'my-rentals',
          component: MyRentalsView
        },
        {
          path: 'rackets', // 访问路径是 /home/rackets
          name: 'racket-rental',
          component: RacketRentalView
        }
      ]
    }
  ]
})

// 全局前置路由守卫 (The Guard)
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  const token = userStore.token

  // 如果用户尝试访问的不是登录页，并且没有 token
  if (to.name !== 'login' && !token) {
    // 将用户重定向到登录页
    next({ name: 'login' })
  } else {
    // 否则，正常放行
    next()
  }
})

export default router
