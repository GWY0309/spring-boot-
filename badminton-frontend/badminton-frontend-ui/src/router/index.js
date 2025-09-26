import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import MainLayout from '../layout/MainLayout.vue'
import HomeView from '../views/HomeView.vue'
import { useUserStore } from '@/stores/user'
import CourtListView from '../views/CourtListView.vue'
import MyReservationsView from '../views/MyReservationsView.vue'
import MyRentalsView from '../views/MyRentalsView.vue'
import RacketRentalView from '../views/RacketRentalView.vue'
import AdminCourtView from '../views/AdminCourtView.vue'
import AdminRacketView from '../views/AdminRacketView.vue'
import AdminRentalView from '../views/AdminRentalView.vue'
import AdminReservationView from '../views/AdminReservationView.vue'



const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'login',
      component: LoginView
    },
    {
      path: '/home',
      component: MainLayout,
      redirect: '/home/dashboard',
      children: [
        {
          path: 'dashboard',
          name: 'home',
          component: HomeView
        },
        {
          path: 'courts',
          name: 'court-list',
          component: CourtListView
        },
        {
          path: 'my-reservations',
          name: 'my-reservations',
          component: MyReservationsView
        },
        {
          path: 'my-rentals',
          name: 'my-rentals',
          component: MyRentalsView
        },
        {
          path: 'rackets',
          name: 'racket-rental',
          component: RacketRentalView
        },
        {
          path: 'admin/courts',
          name: 'admin-court-management',
          component: AdminCourtView
        },
        {
          path: 'admin/rackets',
          name: 'admin-racket-management',
          component: AdminRacketView
        },
        {
          path: 'admin/rentals',
          name: 'admin-rental-management',
          component: AdminRentalView
        },
        {
          path: 'admin/reservations',
          name: 'admin-reservation-management',
          component: AdminReservationView
        }
      ]
    }
  ]
})

// 全局前置路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  const token = userStore.token
  if (to.name !== 'login' && !token) {
    next({ name: 'login' })
  } else {
    next()
  }
})

export default router
