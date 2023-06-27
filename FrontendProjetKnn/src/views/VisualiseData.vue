<script setup>
import { ref, watch } from 'vue';
import { useLayout } from '@/layout/composables/layout';
import axios from 'axios';
const { layoutConfig } = useLayout();
let documentStyle = getComputedStyle(document.documentElement);
let textColor = documentStyle.getPropertyValue('--text-color');
let textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary');
let surfaceBorder = documentStyle.getPropertyValue('--surface-border');
const datasize = ref('');
const classesize = ref('');
const attributesize = ref('');
const pieData = ref(null);
const attributeList = ref([]);
const classeList = ref([]);
const labels = ref([]);
const dataset = ref([]);
const backgroundClr = ref([]);
const pieOptions = ref(null);
const hbackgroundClr = ref([]);
const exist = ref('');
const setColorOptions = () => {
    documentStyle = getComputedStyle(document.documentElement);
    textColor = documentStyle.getPropertyValue('--text-color');
    textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary');
    surfaceBorder = documentStyle.getPropertyValue('--surface-border');
    const value = localStorage.getItem("Knn");
    if (value === null) {
        exist.value = 1;
    } else {
        exist.value = 2;
    }
};

const setChart = async () => {
    await axios.get('http://localhost:8080/getliststat').then(response => {
        const list = response.data;
        console.log(list);
        let i = 0;
        const color = ['pink', 'purple', 'red', 'blue', 'indigo', 'deepPurple', 'teal', 'cyan', 'lightBlue', 'green', 'blueGrey', 'brown'];
        list.forEach(item => {
            labels.value.push(item.classe);
            dataset.value.push(item.poursentagetotal);
            console.log(color[i]);
            backgroundClr.value.push(documentStyle.getPropertyValue('--' + color[i] + '-500'));
            hbackgroundClr.value.push(documentStyle.getPropertyValue('--' + color[i] + '-400'));
            i++;
        })
    })
        .catch(error => {
            console.log(error);
        });
    await axios.get('http://localhost:8080/getAttrebutandclasse')
        .then(response => {
            const data = response.data;
            console.log("hi");
            // Mettre à jour les valeurs des références avec les données récupérées
            attributeList.value.push(Object.keys(data)[0]);
            classeList.value = data[Object.keys(data)[0]];
            console.log(attributeList)
        })
        .catch(error => {
            console.log(error);
        });
    await axios.get('http://localhost:8080/getnbr').then(response => {
        const result = response.data;
        console.log(result);
        datasize.value = result[0];
        classesize.value = result[1];
        attributesize.value = result[2];
        console.log(datasize.value);
        console.log(classesize);
        console.log(attributesize);
    }).catch(error => {
        console.log(error);
    });
    await axios.get('http://localhost:8080/getnbr').then(response => {
        const result = response.data;
        console.log(result);
        datasize.value = result[0];
        classesize.value = result[1];
        attributesize.value = result[2];
        console.log(datasize.value);
        console.log(classesize);
        console.log(attributesize);
    }).catch(error => {
        console.log(error);
    });

    pieData.value = {
        labels: labels,
        datasets: [
            {
                data: dataset,
                backgroundColor: backgroundClr,
                hoverBackgroundColor: hbackgroundClr
            }
        ]
    };

    pieOptions.value = {
        plugins: {
            legend: {
                labels: {
                    usePointStyle: true,
                    color: textColor
                }
            }
        }
    };



};

watch(
    layoutConfig.theme,
    () => {
        setColorOptions();
        setChart();
    },
    { immediate: true }
);
</script>

<template>
    <div v-if="exist == 1">
        <div class="surface-ground flex align-items-center justify-content-center min-h-screen min-w-screen overflow-hidden">
        <div class="flex flex-column align-items-center justify-content-center">
            <img src="/demo/images/landing/4.png" alt="Sakai logo" class="mb-5 w-6rem flex-shrink-0" />
            <div style="border-radius: 56px; padding: 0.3rem; background: linear-gradient(180deg, rgba(33, 150, 243, 0.4) 10%, rgba(33, 150, 243, 0) 30%)">
                <div class="w-full surface-card py-8 px-5 sm:px-8 flex flex-column align-items-center" style="border-radius: 53px">
                    <span class="text-blue-500 font-bold text-3xl">404</span>
                    <h1 class="text-900 font-bold text-3xl lg:text-5xl mb-2">Not Found</h1>
                    
                    <router-link to="/" class="w-full flex align-items-center py-5 border-300 border-bottom-1">
                        <span class="flex justify-content-center align-items-center bg-cyan-400 border-round" style="height: 3.5rem; width: 3.5rem">
                            <i class="text-50 pi pi-fw pi-table text-2xl"></i>
                        </span>
                        <span class="ml-4 flex flex-column">
                            <span class="text-900 lg:text-xl font-medium mb-0 block">Avant de consluter cette page</span>
                            <span class="text-600 lg:text-xl">Vous devrier Entrer votre dataset.</span>
                        </span>
                    </router-link>

                </div>
            </div>
        </div>
    </div>
    </div>
    <div v-if="exist == 2">
        <h1>Visualiser votre dataset:</h1>
        <div class="grid p-fluid">
            <div class="col-12 xl:col-6">
                <div class="card ">
                    <h5>Nombre d'instance : {{ datasize }}</h5>
                    <h5>Nombre de classe: {{ classesize }}</h5>
                    <h5>Nombbre d'attribut:{{ attributesize }}</h5>
                </div>

                <div class="card">
                    <h5>Liste des attribut:</h5>
                    <div v-for="attribut in attributeList">
                        <h6>{{ attribut }}</h6>
                    </div>
                    <h5>Liste des Classes</h5>
                    <div v-for="classe in classeList">
                        <h6>{{ classe }}</h6>
                    </div>
                </div>

            </div>
            <div class="col-12 xl:col-6">
                <div class="card flex flex-column align-items-center">
                    <h5 class="text-left w-full">Diagramme circulaire</h5>
                    <Chart type="pie" :data="pieData" :options="pieOptions"></Chart>
                </div>
            </div>

    </div>
</div></template>
