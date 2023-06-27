<template>
  <div class="grid">
    <div class="col-12">
      <div class="card">
        <form @submit.prevent="senddata()">
          <h5>Commencer une nouvelle classification</h5>
          <div class="p-fluid formgrid grid">
            <div class="field col-12 md:col-3">
              <label for="firstname2">Choisir Votre K</label>
              <InputText id="firstname2" type="text" v-model="k" />
            </div>
            <div class="field col-12 md:col-3">
              <label for="state">Les Options de test</label>
              <Dropdown id="state" @change="displaySelectedValueCode" v-model="dropdownItem" :options="dropdownItems"
                optionLabel="name" placeholder="Sélectionnez une option"></Dropdown>
            </div>
            <div v-if="selectedValueCode === '1'" class="field col-12 md:col-3">
              <label for="address">Nombre de folds</label>
              <InputText id="city" type="text" v-model="nbrfolds" />
            </div>
            <div v-if="selectedValueCode === '2'" class="field col-12 md:col-3">
              <label for="city">Pourcentage split pour le test</label>
              <InputText id="city" type="text" v-model="prsplit" />
            </div>
            <div class="field col-12 md:col-3">
              <label for="state">Les attributs sous forme de :</label>
              <Dropdown id="state" @change="displaySelectedValueCode" v-model="dropdownItem2" :options="dropdownItems2"
                optionLabel="name" placeholder="Sélectionnez une option"></Dropdown>
            </div>
            <div class="field col-12 md:col-3">
              <label for="city">Entrez votre dataset</label>
              <input id="city" type="file" style=".file-input {
  position: relative;
  display: inline-block;
}
.input-file {
  position: absolute;
  left: -9999px;
}
.custom-file-label {
  display: inline-block;
  padding: 10px 10px;
  background-color: #e9e9e9;
  border: none;
  cursor: pointer;
}
" @change="handleFileUpload" accept=".csv,.arff" />
              <p v-if="isInvalidFile">Veuillez sélectionner un fichier CSV ou ARFF.</p>
            </div>
          </div>
          <div class="field col-12 md:col-3">
            <Button type="submit" label="Submit" class="mr-2 mb-2"></Button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>
  
<script>

import axios from 'axios';
export default {
  data() {
    return {
      dropdownItems: [
        { name: 'Cross Validation', code: '1' },
        { name: 'Pourcentage split', code: '2' }
      ],
      dropdownItems2: [
        { name: ' Phrases', code: '1' },
        { name: 'Mots', code: '2' }
      ],
      dropdownItem: null,
      dropdownItem2: null,
      selectedValueCode: '',
      selectedValueCode2: '',
      done: '',
      selectedFile: null,
      k: '',
      prsplit: '',
      nbrfolds: ''
    };
  },
  methods: {
    displaySelectedValueCode() {
      this.selectedValueCode = this.dropdownItem.code;
      this.selectedValueCode2 = this.dropdownItem2.code;
      console.log(this.selectedValueCode);
    },
    handleFileUpload(event) {
      this.selectedFile = event.target.files[0];
    },
    senddata() {
      const file = this.selectedFile;
      const fileType = file.name.split(".").pop();
      if (fileType === "csv") {
        // Traiter le fichier CSV
        this.process(file);
      } else if (fileType === "arff") {
        // Traiter le fichier ARFF
        this.process(file);
      } else {
        alert("Veuillez sélectionner un fichier CSV ou ARFF.");
      }
    },
    process(file) {
      const formData = new FormData();
      formData.append('file', file);
      console.log("hi");
      axios.post('http://localhost:8080/upload', formData)
        .then(response => {
          let nbr = 0;
          if (this.selectedValueCode == 1)
            nbr = this.nbrfolds;
          else
            nbr = this.prsplit;
            
          console.log('Réponse du serveur :', response.data);
          localStorage.setItem('Knn', JSON.stringify({
            k: this.k,
            chois: this.selectedValueCode,
            nbr: nbr,
            type:this.selectedValueCode2,
            chemin:file.name
          }));
          this.$router.push("/visualiser")
          // Traiter la réponse du serveur ici
        })
        .catch(error => {
          console.error('Erreur :', error);
          // Gérer les erreurs ici
        });
    },
  }
};

</script>
  