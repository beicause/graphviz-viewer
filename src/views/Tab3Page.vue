<template>
  <ion-page style="max-width: 1136px;margin: auto;">
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
          <ion-radio-group v-model="themeMode"
            style="display: grid;grid-template-columns: 1fr 1fr 1fr;column-gap: 4px;font-size: 10px;">
            <ion-radio justify="end" label-placement="end" value="system">System</ion-radio>
            <ion-radio justify="end" label-placement="end" value="light">Light</ion-radio>
            <ion-radio justify="end" label-placement="end" value="dark">Dark</ion-radio>
          </ion-radio-group>
        </ion-item>
      </ion-list>

      <div v-if="Capacitor.isNativePlatform()">
        <ion-list-header>Storage</ion-list-header>
        <ion-list :inset="true">
          <ion-item>
            <ion-input label="svg:&nbsp;documents&nbsp;/" v-model="path.svg"></ion-input>
          </ion-item>
          <ion-item>
            <ion-input label="png:&nbsp;documents&nbsp;/" v-model="path.png"></ion-input>
          </ion-item>
          <ion-item>
            <ion-input label="gv:&nbsp;&nbsp;&nbsp;documents&nbsp;/" v-model="path.gv"></ion-input>
          </ion-item>
        </ion-list>
      </div>

    </ion-content>
  </ion-page>
</template>

<script setup>
import { path } from '@/settings';
import { themeMode } from '@/theme';
import { Capacitor } from '@capacitor/core';
</script>
