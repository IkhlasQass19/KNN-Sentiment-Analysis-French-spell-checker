<script setup>
import axios from 'axios';
import { ref, onBeforeMount, reactive, watch } from 'vue';
const customers = ref([]);
const knnData = JSON.parse(localStorage.getItem('Knn'));
const loading = ref(true);
const idFrozen = ref(false);
let i = 1;
let exist = false;
const kArray = [];
const exactitudeArray = [];
const precizionArray = [];
const rappelArray = [];
const f_mesureArray = [];
onBeforeMount(async () => {
    if (knnData.chemin !== null) {
        exist = true;
        await axios.get('http://localhost:8080/getHistorique?chemin=' + knnData.chemin)
            .then(response => {
                const data = response.data;
                console.log("hi");
                customers.value = response.data;
                loading.value = false;
                data.forEach(item => {
                    kArray.push(item.k);
                    exactitudeArray.push(item.exactitude);
                    precizionArray.push(item.precizion);
                    rappelArray.push(item.rappel);
                    f_mesureArray.push(item.f_mesure);
                });

                console.log("kArray:", kArray);
                console.log("exactitudeArray:", exactitudeArray);
                console.log("precizionArray:", precizionArray);
                console.log("rappelArray:", rappelArray);
                console.log("f_mesureArray:", f_mesureArray);
            })
            .catch(error => {
                console.log(error);
            });
    }
    else {
        exist = false;
    }

});

const lineData = reactive({
    labels: kArray.sort((a, b) => a - b),
    datasets: [
        {
            label: 'Exactitude',
            data: exactitudeArray,
            fill: false,
            backgroundColor: '#2f4860',
            borderColor: '#2f4860',
            tension: 0.4
        },
        {
            label: 'Précision',
            data: precizionArray,
            fill: false,
            backgroundColor: '#00bb7e',
            borderColor: '#00bb7e',
            tension: 0.4
        }
        ,  
        {
            label: 'Rappel',
            data: rappelArray,
            fill: false,
            backgroundColor: 'yellow',
            borderColor: 'yellow',
            tension: 0.4
        },   {
            label: 'F_mesure',
            data: f_mesureArray,
            fill: false,
            backgroundColor: 'red',
            borderColor: 'red',
            tension: 0.4
        }
    ]
});

const lineOptions = ref(null);



const applyLightTheme = () => {
    lineOptions.value = {
        plugins: {
            legend: {
                labels: {
                    color: '#495057'
                }
            }
        },
        scales: {
            x: {
                ticks: {
                    color: '#495057'
                },
                grid: {
                    color: '#ebedef'
                },
            },
            y: {
                ticks: {
                    color: '#495057'
                },
                grid: {
                    color: '#ebedef'
                }
            }
            
        }
    };
};


watch(
  () => [kArray, exactitudeArray, precizionArray, rappelArray, f_mesureArray],
  () => {
    applyLightTheme();
  },
  { immediate: true }
);
</script>

<template>
    <div class="grid">

        <div class="col-12">
            <div class="card">
                <DataTable :value="customers" :scrollable="true" scrollHeight="400px" :loading="loading2"
                    scrollDirection="both" class="mt-3">
                    <Column field="name" header="Nom de dataset" :style="{ width: '150px' }" frozen>
                        <template #body="{ data }">
                            <span style="margin-left: 0.5em; vertical-align: middle" class="image-text">{{ data.cheminDS
                            }}</span>
                        </template>
                    </Column>
                    <Column field="id" header="k" :style="{ width: '100px' }" :frozen="idFrozen">
                        <template #body="{ data }">
                            <span style="margin-left: 0.5em; vertical-align: middle" class="image-text">{{ data.k
                            }}</span>
                        </template>
                    </Column>
                    <Column field="country.name" header="Précision" :style="{ width: '200px' }">
                        <template #body="{ data }">
                            <span style="margin-left: 0.5em; vertical-align: middle" class="image-text">{{ data.precizion
                            }}</span>
                        </template>
                    </Column>
                    <Column field="date" header="Rappel" :style="{ width: '200px' }">
                        <template #body="{ data }">
                            <span style="margin-left: 0.5em; vertical-align: middle" class="image-text">{{ data.rappel
                            }}</span>
                        </template>
                    </Column>
                    <Column field="company" header="F_mesure" :style="{ width: '200px' }"> <template #body="{ data }">
                            <span style="margin-left: 0.5em; vertical-align: middle" class="image-text">{{ data.f_mesure
                            }}</span>
                        </template>
                    </Column>
                    <Column field="status" header="Exactitude" :style="{ width: '200px' }">
                        <template #body="{ data }">
                            <span :class="'customer-badge status-' + data.status">{{ data.exactitude }}</span>
                        </template>
                    </Column>

                </DataTable>
            </div>


        </div>
        <div class="col-12 xl:col-12">
            <div class="card">
                <h5>Les indicateurs de perfermance en fonction de K </h5>
                <Chart type="line" :data="lineData" :options="lineOptions" />
            </div>
        </div>
    </div>
</template>

<style scoped lang="scss">
@import '@/assets/demo/styles/badges.scss';

::v-deep(.p-datatable-frozen-tbody) {
    font-weight: bold;
}

::v-deep(.p-datatable-scrollable .p-frozen-column) {
    font-weight: bold;
}
</style>
