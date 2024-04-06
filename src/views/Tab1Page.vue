<template>
  <ion-page>
    <ion-header>
      <ion-toolbar>
        <ion-buttons slot="end">
          <ion-button id="trigger-export" slot="label">
            <ion-icon slot="icon-only" :icon="saveOutline"></ion-icon>
          </ion-button>
          <ion-popover dismissOnSelect trigger="trigger-export" trigger-action="click">
            <ion-list>
              <ion-item button @click="exportDot('svg')">Save as svg</ion-item>
              <ion-item button @click="exportDot('png')">Save as png</ion-item>
              <ion-item button @click="exportDot('gv')">Save as gv(dot)</ion-item>
            </ion-list>

          </ion-popover>
        </ion-buttons>
        <ion-title>Graphviz Viewer</ion-title>
      </ion-toolbar>
    </ion-header>
    <ion-content :fullscreen="true" style="padding: 4px;">
      <div style="height: 100%;display: grid;grid-template-rows: 50% 50%;padding: 0 8px;">
          <ion-textarea :placeholder="'Enter Graphviz DOT language'" :class="input_cls"
            v-model="input_text" fill="solid" :rows="textarea_rows" :error-text="input_error" :style="input_style" />
        <div ref="el_graph"></div>
      </div>
    </ion-content>
  </ion-page>
</template>

<script setup>
import { computed, onMounted } from 'vue';
import { ref } from 'vue';
import * as d3 from 'd3'
import "d3-graphviz"
import { watchThrottled, useElementSize } from '@vueuse/core'
import { saveOutline } from "ionicons/icons"
import { Canvg, presets } from "canvg"
import { Filesystem, Directory } from "@capacitor/filesystem"
import { Encoding } from '@capacitor/filesystem';
import { Toast } from '@capacitor/toast';
import { path } from '@/settings';
import { input_cls, input_error, input_style, input_text } from "./state";
import { MPlugin } from '@/plugin';
import { Capacitor } from '@capacitor/core';

/** @type {import('vue').Ref<HTMLDivElement>} */
const el_graph = ref(null)
const { height } = useElementSize(el_graph)
const font_size = ref(0)
const textarea_rows = computed(() => {
  return Math.ceil(height.value / font_size.value)-7
})
let svgNode = null
let gv = null
const preset = presets.offscreen()

function renderDot(value) {
  const div = d3.select(el_graph.value)
  gv.renderDot(value, () => {
    svgNode = div.node().querySelector("svg")
    const s = d3.select(div.node().querySelector("svg"));
    s.attr("width", "100%").attr("height", "100%")

    input_style.value.height = "100%"
    input_cls.value.length === 2 && input_cls.value.pop()
    input_error.value = ''
  }).onerror((err) => {
    input_style.value.height = "calc(100% - 16px)"
    input_cls.value.length === 1 && input_cls.value.push("ion-invalid")
    input_error.value = err
  });
}

watchThrottled(input_text, (value, _) => {
  value && renderDot(value)
}, { throttle: 1000 })

onMounted(() => {
  if (!gv) gv = d3.select(el_graph.value).graphviz({ useWorker: false }).scale(0.5)
  renderDot(input_text.value)
  font_size.value = parseInt(window.getComputedStyle(el_graph.value, null).getPropertyValue('font-size'));
})

function genPath(format) {
  const _path = path[format] + `/${new Date().getTime().toString()}.${format}`
  return _path
}
async function exportDot(type) {
  if (MPlugin) await MPlugin.showInterstitialAd()
  const size = svgNode.getAttribute("viewBox").split(" ").map(v => v / 2)
  const node = svgNode.cloneNode(true)
  node.setAttribute("viewBox", size.join(" "))
  node.setAttribute("width", `${size[2]}pt`)
  node.setAttribute("height", `${size[3]}pt`)
  const xml = new XMLSerializer()
  const svg = xml.serializeToString(node)
  let writeRes = null
  const writeFile = async (_path, _data, encoding = Encoding.UTF8) => {
    if (Capacitor.isNativePlatform()) {
      writeRes = await Filesystem.writeFile({
        path: _path, data: _data, directory: Directory.Documents, encoding
        , recursive: true
      })
    } else {
      let filename = _path.split("/")
      filename = filename[filename.length - 1]
      const file = new File([_data], filename)
      const url = URL.createObjectURL(file)
      const tmpLink = document.createElement("a");
      tmpLink.href = url;
      tmpLink.download = filename;
      document.body.appendChild(tmpLink);
      tmpLink.click();
      document.body.removeChild(tmpLink);
      URL.revokeObjectURL(url);
    }
  }
  if (type === "svg") {
    await writeFile(genPath("svg"), svg)
  } else if (type === "gv") {
    await writeFile(genPath("gv"), input_text.value)
  }
  else if (type === "png") {
    const blob = await svg2PngBlob(size[2], size[3], svg)
    if (Capacitor.isNativePlatform()) {
      const reader = new FileReader()
      reader.readAsDataURL(blob)
      await new Promise((resolve, reject) => {
        reader.onloadend = async (e) => {
          await writeFile(genPath("png", e.target.result), undefined)
          resolve()
        }
      })
    } else {
      await writeFile(genPath("png"), blob, undefined)
    }
  }
  writeRes && await Toast.show({ text: writeRes.uri, duration: 6000 })
}

async function svg2PngBlob(width, height, svg) {
  const canvas = new OffscreenCanvas(width, height)
  const ctx = canvas.getContext('2d')
  const v = await Canvg.fromString(ctx, svg, preset)

  // Render only first frame, ignoring animations and mouse.
  await v.render()

  const blob = await canvas.convertToBlob()
  return blob
}
</script>
