<template>
  <ion-page>
    <ion-header>
      <ion-toolbar>
        <ion-title>Settings</ion-title>
      </ion-toolbar>
    </ion-header>
    <ion-content>
      <ion-list-header>Appearance</ion-list-header>
      <ion-list :inset="true">
        <ion-item>
          <ion-label>Theme</ion-label>
          <ion-radio-group v-model="theme"
            style="display: grid;grid-template-columns: 1fr 1fr 1fr;column-gap: 4px;font-size: 10px;">
            <ion-radio justify="end" label-placement="end" value="system">System</ion-radio>
            <ion-radio justify="end" label-placement="end" value="light">Light</ion-radio>
            <ion-radio justify="end" label-placement="end" value="dark">Dark</ion-radio>
          </ion-radio-group>
        </ion-item>
      </ion-list>
    </ion-content>
  </ion-page>
</template>

<script setup>
import { theme } from '@/theme';
</script>
