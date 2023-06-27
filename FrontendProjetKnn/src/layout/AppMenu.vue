<script setup>
import { ref } from 'vue';

import AppMenuItem from './AppMenuItem.vue';

const model = ref([
    {
        label: 'Home',
        items: [{ label: 'Ajouter Dataset', icon: 'pi pi-fw pi-file', to: '/Ajouter' }]
    },
    {
        label: 'Autre',
        items: [
            { label: 'Visualiser Data', icon: 'pi pi-fw pi-chart-bar', to: '/visualiser' },
            { label: 'Resultat', icon: 'pi pi-fw pi-check-square', to: '/Resultat' },
            { label: 'K valeurs', icon: 'pi pi-fw pi-list', to: '/k' },
            { label: 'Analyse des sentiments', icon: 'pi pi-fw pi-circle', to: '/analyse' },

        ]
    }
]);
</script>

<template>
    <ul class="layout-menu">
        <template v-for="(item, i) in model" :key="item">
            <app-menu-item v-if="!item.separator" :item="item" :index="i"></app-menu-item>
            <li v-if="item.separator" class="menu-separator"></li>
        </template>
       
    </ul>
</template>

<style lang="scss" scoped></style>
