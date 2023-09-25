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
              <ion-item button @click="exportDot('svg')">Share as svg</ion-item>
              <ion-item button @click="exportDot('png')">Share as png</ion-item>
              <ion-item button @click="exportDot('gv')">Share as gv(dot)</ion-item>
            </ion-list>

          </ion-popover>
        </ion-buttons>
        <ion-title>Graphviz Viewer</ion-title>
      </ion-toolbar>
    </ion-header>
    <ion-content :fullscreen="true">
      <div style="height: 100%;display: grid;grid-template-rows: 50% 50%;">
        <div><ion-textarea :placeholder="'Enter Graphviz DOT language'" :class="input_cls" v-model="input_text"
            :error-text="input_error" :style="input_style" />
        </div>
        <div id="graph"></div>
      </div>
    </ion-content>
  </ion-page>
</template>

<script setup>
import { onMounted } from 'vue';
import { ref } from 'vue';
import * as d3 from 'd3'
import "d3-graphviz"
import { watchThrottled } from '@vueuse/core'
import { saveOutline } from "ionicons/icons"
import { Canvg, presets } from "canvg"
import { Filesystem, Directory } from "@capacitor/filesystem"
import { Encoding } from '@capacitor/filesystem';
import { Toast } from '@capacitor/toast';

const input_cls = ref(["ion-touched"])
const input_text = ref(`digraph {
  a -> { b c }
}`)
const input_error = ref('')
const input_style = ref({ height: "100%" })

let svgNode = null
const preset = presets.offscreen()

function renderDot(value) {
  const div = d3.select("#graph")
  div.graphviz().scale(0.5)
    .renderDot(value, () => {
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
  renderDot(input_text.value)
})

function genPath(format) {
  const path = `graphviz_viewer/${format}/${new Date().getTime().toString()}.${format}`
  return path
}
async function exportDot(type) {
  const size = svgNode.getAttribute("viewBox").split(" ").map(v => v / 2)
  const node = svgNode.cloneNode(true)
  node.setAttribute("viewBox", size.join(" "))
  const xml = new XMLSerializer()
  const svg = xml.serializeToString(node)
  let [writeRes, path] = [null, null]
  if (type === "svg") {
    path = genPath("svg")
    writeRes = await Filesystem.writeFile({
      path, data: svg, directory: Directory.Documents, encoding: Encoding.UTF8
      , recursive: true
    })
  } else if (type === "gv") {
    path = genPath("gv")
    writeRes = await Filesystem.writeFile({
      path, data: input_text.value, directory: Directory.Documents, encoding: Encoding.UTF8
      , recursive: true
    })
  }
  else if (type === "png") {
    const blob = await svg2PngBlob(size[2], size[3], svg)
    const reader = new FileReader()
    reader.readAsDataURL(blob)
    await new Promise((resolve, reject) => {
      reader.onloadend = async (e) => {
        path = genPath("png")
        writeRes = await Filesystem.writeFile({
          path, data: e.target.result, directory: Directory.Documents, recursive: true
        })
        resolve()
      }
    })

  }
  await Toast.show({ text: writeRes.uri, duration: 6000 })
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
