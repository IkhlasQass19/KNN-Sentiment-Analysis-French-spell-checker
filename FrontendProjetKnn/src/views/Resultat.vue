<template>
    <div class="grid" v-if="exist">
        <div class="col-12">
            <h1>Visualiser votre resultats :</h1>
            <div class="card">
                <h5>Les performance indicators pour chaque ensemble </h5>
                <DataTable :value="customers.slice(0, customers.length - 1)" :scrollable="true" scrollHeight="400px"
                    :loading="loading">
                    <Column field="name" header="Ensemble" :style="{ width: '150px' }" frozen> <template #body="{ data }">
                            <span style="margin-left: 0.5em; vertical-align: middle" class="image-text">{{ i++ }}</span>
                        </template></Column>
                    <Column field="id" header="Exactitude" :style="{ width: '100px' }" :frozen="idFrozen"> <template
                            #body="{ data }">
                            <span style="margin-left: 0.5em; vertical-align: middle" class="image-text">{{ data.exactitude
                            }}</span>
                        </template></Column>
                    <Column field="country.name" header="Précision" :style="{ width: '200px' }">
                        <template #body="{ data }">
                            <span style="margin-left: 0.5em; vertical-align: middle" class="image-text">{{ data.precisionG
                            }}</span>
                        </template>
                    </Column>
                    <Column field="date" header="Rappel" :style="{ width: '200px' }"> <template #body="{ data }">
                            <span style="margin-left: 0.5em; vertical-align: middle" class="image-text">{{ data.rappelG
                            }}</span>
                        </template></Column>
                    <Column field="company" header="F_mesure" :style="{ width: '200px' }"> <template #body="{ data }">
                            <span style="margin-left: 0.5em; vertical-align: middle" class="image-text">{{ data.f_mesureG
                            }}</span>
                        </template></Column>
                </DataTable>
            </div>
        </div>
        <div class="col-12 xl:col-6">
            <div class="card" v-for="(item, index) in customers.slice(customers.length - 1, customers.length)" :key="index">
                <h5>Performance Indicators Globales</h5>
                <h6>Exactitude: {{ item.exactitude }}</h6>
                <h6>Précision: {{ item.precisionG }}</h6>
                <h6>Rappel: {{ item.rappelG }}</h6>
                <h6>F-mesure: {{ item.f_mesureG }}</h6>
            </div>
        </div>
        <div class="col-12 xl:col-6">
            <div class="card">
                <h5>Matrice de confusion</h5>
                <select v-model="selectedOption">
                    <option disabled>Choisir un ensemble</option>
                    <option v-for="index in customers.length - 1" :value="index">{{ 'Ensemble ' + index }}</option>
                    <option :value="customers.length">Matrice Globale</option>
                </select> <br>
                <h5>Matrice sélectionnée:</h5>
                <div class="center" v-if="selectedOption !== null">
                    <div v-for="(item, index) in getSelectedMatrices" :key="index"
                        class="flex flex-column align-items-center">
                        <table class="center">
                            <tr v-for="row in item.matrice">
                                <td v-for="value in row"><b>{{ value }}</b></td>
                            </tr>
                        </table>

                    </div>
                </div>
            </div>

        </div>
        <div class="col-12">
            <div class="card">
                <h5>Diagramme à bandes</h5>
                <select v-model="selectedOption2">
                    <option disabled>Choisir un ensemble</option>
                    <option v-for="index in customers.length - 1" :value="index">{{ 'Ensemble ' + index }}</option>
                    <option :value="customers.length">Matrice Globale</option>
                </select>
                <Chart type="bar" :data="barData" :options="barOptions"></Chart>
            </div>
        </div>
    </div>
    <div class="grid" v-if="!exist">
        <div class="col-12">
            <h1>Visualiser votre resultats :</h1>
            <div class="grid p-fluid">
                <div class="col-12 xl:col-6">
                    <div class="card" v-for="(item, index) in customers.slice(0, 1)" :key="index">
                        <h5>Performance Indicators Globales</h5>
                        <h6>Exactitude: {{ item.exactitude }}</h6>
                        <h6>Précision: {{ item.precisionG }}</h6>
                        <h6>Rappel: {{ item.rappelG }}</h6>
                        <h6>F-mesure: {{ item.f_mesureG }}</h6>
                    </div>
                    <div class="card flex flex-column align-items-center" v-for="(item, index) in customers.slice(0, 1)"
                        :key="index">
                        <h5>Matrice de confusion</h5>
                        <div class="center">
                            <table class="center">
                                <tbody>
                                    <tr v-for="(row, rowIndex) in item.matrice" :key="rowIndex">
                                        <td v-for="(value, colIndex) in row" :key="colIndex">
                                            <b> {{ value }}</b>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="col-12 xl:col-6">

                    <div class="card">
                        <h5>Diagramme à bandes</h5>
                        <Chart type="bar" :data="barData" :options="barOptions"></Chart>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
  
<script setup>
import axios from 'axios';
import { ref, onBeforeMount, computed, watch } from 'vue';
import { useLayout } from '@/layout/composables/layout';
const customers = ref([]);
const knnData = JSON.parse(localStorage.getItem('Knn'));
const loading = ref(true);
const idFrozen = ref(false);
const selectedOption = ref(null);
const selectedOption2 = ref(null);
let i = 1;
let exist = false;
onBeforeMount(async () => {

    if (knnData.chois == 1) {
        exist = true;
        console.log(exist);
    }
    else {
        exist = false;
        console.log(exist);
    }
    await axios.get('http://localhost:8080/getliststat');
    await axios.get('http://localhost:8080/getAttrebutandclasse')
        .then(response => {
            const data = response.data;
            console.log("hi");
            // Mettre à jour les valeurs des références avec les données récupérées

            labels.value = data[Object.keys(data)[0]];
            console.log(labels.value)
        })
        .catch(error => {
            console.log(error);
        });
    const response = await axios.get('http://localhost:8080/getresult?k='+knnData.k+'&choix='+knnData.chois+'&nbr='+knnData.nbr+'&type='+knnData.type);
    if (knnData.chois == 2) {
        try {

            customers.value = response.data;
            loading.value = false;


        } catch (error) {
            console.error('Error fetching customer data:', error);
        }
    }
    else {
        customers.value = response.data;
        loading.value = false;
    }
});
const getSelectedMatrices = computed(() => {
    console.log(selectedOption.value);
    if (selectedOption.value === customers.length) {
        console.log(customers.value[customers.length - 1]);
        return customers.value[customers.length - 1];
    } else if (selectedOption.value !== null) {
        // Return the selected ensemble matrice
        console.log(customers.value[selectedOption.value]);
        //selectedOption.value = null;
        return [customers.value[selectedOption.value - 1]];
    } else {
        // No selection, return empty array
        return [];
    }
});
const labels = ref([]);
const dataset = ref([]);
const { layoutConfig } = useLayout();
let documentStyle = getComputedStyle(document.documentElement);
let textColor = documentStyle.getPropertyValue('--text-color');
let textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary');
let surfaceBorder = documentStyle.getPropertyValue('--surface-border');

const barData = ref(null);

const barOptions = ref(null);

const setColorOptions = () => {
    documentStyle = getComputedStyle(document.documentElement);
    textColor = documentStyle.getPropertyValue('--text-color');
    textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary');
    surfaceBorder = documentStyle.getPropertyValue('--surface-border');
};

const setChart = () => {
    const result = [];
    const diagonalElements = [];
    if(knnData.chois==1){
    if (selectedOption2.value == null) {
        const selectedCustomer = customers.value[customers.length - 1];
        if (selectedCustomer && selectedCustomer.matrice) {
            const matrice = selectedCustomer.matrice;
            console.log("hiudud");
            for (let i = 0; i < matrice.length; i++) {
                for (let j = 0; j < matrice[i].length; j++) {
                    if (i === j) {
                        diagonalElements.push(matrice[i][j]);
                    }
                }
            }
            const lastColumn = matrice.map(row => row[row.length - 1]);

            for (let i = 0; i < lastColumn.length; i++) {
                const subtractedValue = lastColumn[i] - diagonalElements[i];
                result.push(subtractedValue);
            }
        }
    } else {
        const selectedCustomer = customers.value[selectedOption2.value - 1];
        if (selectedCustomer && selectedCustomer.matrice) {
            const matrice = selectedCustomer.matrice;
            for (let i = 0; i < matrice.length; i++) {
                for (let j = 0; j < matrice[i].length; j++) {
                    if (i === j) {
                        diagonalElements.push(matrice[i][j]);
                    }
                }
            }
            const lastColumn = matrice.map(row => row[row.length - 1]);

            for (let i = 0; i < lastColumn.length; i++) {
                const subtractedValue = lastColumn[i] - diagonalElements[i];
                result.push(subtractedValue);
            }
        }
    }}
    else {
        console.log("anahna");
        const selectedCustomer = customers.value[0];
        if (selectedCustomer && selectedCustomer.matrice) {
            const matrice = selectedCustomer.matrice;
            for (let i = 0; i < matrice.length; i++) {
                for (let j = 0; j < matrice[i].length; j++) {
                    if (i === j) {
                        diagonalElements.push(matrice[i][j]);
                    }
                }
            }
            const lastColumn = matrice.map(row => row[row.length - 1]);

            for (let i = 0; i < lastColumn.length; i++) {
                const subtractedValue = lastColumn[i] - diagonalElements[i];
                result.push(subtractedValue);
            }
        }
    }
    

    barData.value = {
        labels: labels,
        datasets: [
            {
                label: 'BIEN Classe',
                backgroundColor: documentStyle.getPropertyValue('--primary-500'),
                borderColor: documentStyle.getPropertyValue('--primary-500'),
                data: diagonalElements
            },
            {
                label: 'Mal classes',
                backgroundColor: documentStyle.getPropertyValue('--primary-200'),
                borderColor: documentStyle.getPropertyValue('--primary-200'),
                data: result
            }
        ]
    };
    barOptions.value = {
        plugins: {
            legend: {
                labels: {
                    fontColor: textColor
                }
            }
        },
        scales: {
            x: {
                ticks: {
                    color: textColorSecondary,
                    font: {
                        weight: 500
                    }
                },
                grid: {
                    display: false,
                    drawBorder: false
                }
            },
            y: {
                ticks: {
                    color: textColorSecondary
                },
                grid: {
                    color: surfaceBorder,
                    drawBorder: false
                }
            }
        }
    };
};
watch(setChart, selectedOption2, { immediate: true });

</script>
  
<style scoped lang="scss">
@import '@/assets/demo/styles/badges.scss';

::v-deep(.p-datatable-frozen-tbody) {
    font-weight: bold;
}

::v-deep(.p-datatable-scrollable .p-frozen-column) {
    font-weight: bold;
}
</style>
  