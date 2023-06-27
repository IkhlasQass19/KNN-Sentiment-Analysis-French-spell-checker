import { createRouter, createWebHashHistory } from 'vue-router';
import AppLayout from '@/layout/AppLayout.vue';

const router = createRouter({
    history: createWebHashHistory(),
    routes: [
        {
            path: '/',
            name: 'home',
            component: () => import('@/views/home.vue')
        },
        {
            path: '/action',
            component: AppLayout,
            children: [
                {
                    path: '/Ajouter',
                    name: 'Ajouter',
                    component: () => import('@/views/Ajouter.vue')
                },
                {
                    path: '/visualiser',
                    name: 'Visualiser',
                    component: () => import('@/views/VisualiseData.vue')
                },
                {
                    path: '/Resultat',
                    name: 'Resultat',
                    component: () => import('@/views/Resultat.vue')
                },
                {
                    path: '/Analyse',
                    name: 'Analyse sentiment',
                    component: () => import('@/views/AnalyseSentiment.vue')
                },
                {
                    path:'/K',
                    name: 'Kvisialiser',
                    component: () => import('@/views/Kvisialiser.vue')
                },             
            ]
        },
  
   
    ]
});

export default router;
