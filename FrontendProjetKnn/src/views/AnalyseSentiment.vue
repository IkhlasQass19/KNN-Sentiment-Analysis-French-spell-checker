<script>
import axios from 'axios';
export default {
  data() {
    return {
      dropdownItems: [
        { name: 'Par Vocale', code: '1' },
        { name: 'Par Phrase', code: '2' }
      ],
      dropdownItem: '',
      selectedValueCode: '',
      recording: false,
      transcript: '',
      recognition: null,
      phrase: '',
      feeling: '',
      positiveEmoji: '😊',
      negativeEmoji: '😔',
    }
  },
  mounted() {
    // Vérifie si le navigateur prend en charge l'API SpeechRecognition
    if ('webkitSpeechRecognition' in window) {
      this.recognition = new webkitSpeechRecognition();
      this.recognition.continuous = true;
      this.recognition.lang = 'en-US'; // Changer en fonction de la langue souhaitée

      this.recognition.onresult = (event) => {
        const result = event.results[event.results.length - 1];
        this.transcript = result[0].transcript;
        console.log(this.transcript);
        axios.get('http://localhost:8080/getFeeling?phrase=' + this.transcript)
          .then(response => {
            const data = response.data;
            if (data == 0)
              this.feeling = '<span class="negative">'+"Une phrase Negative" + this.negativeEmoji+'</span>';
            else
              this.feeling ='<span class="positive">' +"Une phrase Positive" + this.positiveEmoji+'</span>';

          });
      };
    } else {
      console.log("L'API SpeechRecognition n'est pas prise en charge par votre navigateur.");
    }
  },
  methods: {

    startRecording() {
      this.recording = true;
      this.transcript = '';
      this.feeling='',
      this.recognition.start();
    },
    stopRecording() {
      this.recording = false;
      this.recognition.stop();
    },
    senddata() {
      console.log(this.phrase);
      axios.get('http://localhost:8080/getFeeling?phrase=' + this.phrase)
        .then(response => {
          const data = response.data;
          if (data == 0)
            this.feeling = '<span class="negative">' +"Une phrase Negative" + this.negativeEmoji+'</span>';
          else
            this.feeling ='<span class="positive">'+ "Une phrase Positive" + this.positiveEmoji+'</span>';

        });
    },
    empty (){
      this.feeling='';
    }
  }
}
</script>
<style>
.card1 {
  background-image: url('../../../demo/images/landing/5.png');
  background-size: cover;
  background-position: center;
  text-align: center;
  padding: 20px;
  width: 1000px;
  /* Ajustez la largeur selon vos besoins */
  height: 530px;
  background-size: cover;
  background-position: center;
  text-align: center;
  display: flex;
  flex-direction: column;
  padding: 20px;
  color: white;
}

.custom-select {
  padding: 10px 20px;
  font-size: 16px;
  border: 2px solid #4CAF50;
  border-radius: 4px;
  background-color: #ffffff;
  color: #333;
  cursor: pointer;
}

.custom-select:focus {
  outline: none;
  border-color: #4CAF50;
  box-shadow: 0 0 0 2px rgba(76, 175, 80, 0.3);
}
  .positive {
    color: #4CAF50;
  }
  
  .negative {
    color:#f44336;
  }
.titre{
  color: #fffdfd;
  /* Couleur du texte de titre */
    font-weight: bold;
}
h1{
  color: #333;
  /* Couleur du texte de titre */
    font-weight: bold;
}

#firstname2 {
  margin: 0 auto;
  /* Centrer l'élément InputText horizontalement */
}

.record-button {
  background-color: #4CAF50;
  color: white;
  padding: 10px 20px;
  font-size: 16px;
  border: none;
  cursor: pointer;
  margin-right: 10px;
}

.custom-input {
  width: 400px;
  /* Ajustez la largeur selon vos besoins */
  padding: 10px;
  font-size: 16px;
  border: 2px solid #4CAF50;
  border-radius: 4px;
  background-color: #ffffff;
  color: #333;
}


.stop-button {
  background-color: #f44336;
  color: white;
  padding: 10px 20px;
  font-size: 16px;
  border: none;
  cursor: pointer;
}

.transcription-section {
  margin-top: 20px;
  padding: 10px;
  background-color: #f2f2f2;
}
</style>

<template>
  <div class="card card1">
  
    <br>
    <h2 class="titre">Analyse des Sentiments</h2>
    <br>
    <br>
    <div class="field col-12 md:col-3">
      <h5 class="titre" style="text-align: left;">Les Options de test</h5>
      <select id="state" v-model="dropdownItem" class="custom-select" @click="empty">
        <option value="" disabled selected>Sélectionnez une option</option>
        <option v-for="item in dropdownItems" :value="item.code">{{ item.name }}</option>
      </select>
    </div>

    <input v-if="dropdownItem == 2" id="firstname2" type="text" v-model="phrase" class="custom-input"
      @keyup.enter="senddata" @click="empty" />
   
    <div v-if="dropdownItem == 1">
      <button @click="startRecording" :disabled="recording" class="record-button">Commencer l'enregistrement</button>
      <button @click="stopRecording" :disabled="!recording" class="stop-button">Arrêter l'enregistrement</button>
    </div>
    <h1 v-html="this.feeling"></h1>
  </div>
</template>
