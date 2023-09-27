import { createApp } from 'vue'
import App from './App.vue'
import router from './router';
import { App as NApp } from "@capacitor/app";
import { IonicVue } from '@ionic/vue';

/* Core CSS required for Ionic components to work properly */
import '@ionic/vue/css/core.css';

/* Basic CSS for apps built with Ionic */
import '@ionic/vue/css/normalize.css';
import '@ionic/vue/css/structure.css';
import '@ionic/vue/css/typography.css';

/* Optional CSS utils that can be commented out */
import '@ionic/vue/css/padding.css';
import '@ionic/vue/css/float-elements.css';
import '@ionic/vue/css/text-alignment.css';
import '@ionic/vue/css/text-transformation.css';
import '@ionic/vue/css/flex-utils.css';
import '@ionic/vue/css/display.css';

/* Theme variables */
import './theme/variables.css';
import "@/theme"
import { Toast } from '@capacitor/toast';

const app = createApp(App)
  .use(IonicVue)
  .use(router);

router.isReady().then(() => {
  app.mount('#app');
});

let toExit = new Date()

NApp.addListener("backButton", async () => {
  if (new Date().getTime() - toExit.getTime() < 2000) {
    await NApp.exitApp()
  } else {
    await Toast.show({ text: "Press again to exit" })
    toExit = new Date()
  }
})