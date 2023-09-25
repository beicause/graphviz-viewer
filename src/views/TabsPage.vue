<template>
  <ion-page>
    <ion-tabs>
      <ion-router-outlet></ion-router-outlet>
      <ion-tab-bar slot="bottom">
        <ion-tab-button tab="tab1" @click="route('/tabs/tab1')">
          <ion-icon aria-hidden="true" :icon="triangle" />
          <ion-label>Viewer</ion-label>
        </ion-tab-button>

        <ion-tab-button tab="tab2" @click="route('/tabs/tab2')">
          <ion-icon aria-hidden="true" :icon="ellipse" />
          <ion-label>Examples</ion-label>
        </ion-tab-button>

        <ion-tab-button tab="tab3" @click="route('/tabs/tab3')">
          <ion-icon aria-hidden="true" :icon="square" />
          <ion-label>Settings</ion-label>
        </ion-tab-button>
      </ion-tab-bar>
    </ion-tabs>
  </ion-page>
</template>

<script setup lang="ts">
import { useIonRouter } from '@ionic/vue';
import { ellipse, square, triangle } from 'ionicons/icons';
const router = useIonRouter()
function route(href: string) {
  router.replace(href)
}
</script>
