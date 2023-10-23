<template>
  <div style="background: black;width: 100%;height: 100%;position: absolute;">
    <ion-app style="max-width: 1136px;margin: auto;">
      <ion-router-outlet />
    </ion-app>
  </div>
</template>

<script setup lang="ts">
import { IonApp, IonRouterOutlet } from '@ionic/vue';
</script>

<style>
/* This sets a different background and item background in light mode on ios */
.ios body {
  --ion-background-color: #f2f2f6;
  --ion-toolbar-background: var(--ion-background-color);
  --ion-item-background: #fff;
}

/* This sets a different background and item background in light mode on md */
.md body {
  --ion-background-color: #f9f9f9;
  --ion-toolbar-background: var(--ion-background-color);
  --ion-item-background: #fff;
}

/* This is added for the flashing that happens when toggling between themes */
ion-item {
  --transition: none;
}
</style>