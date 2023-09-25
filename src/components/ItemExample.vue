<template>
    <ion-card>
        <ion-card-header>
            <ion-card-title>{{ title }}</ion-card-title>
        </ion-card-header>
        <ion-card-content>
            <p style="font-size: 16px;">{{ content }}</p>
            <ion-textarea style="font-size: 16px;" auto-grow :value="dot" v-model="input_text" :class="input_cls"
                :error-text="input_error"></ion-textarea>
        </ion-card-content>
        <div ref="graph"></div>
    </ion-card>
</template>

<script setup>
const props = defineProps({ title: String, content: String, dot: String })
import { watchThrottled } from '@vueuse/core';
import * as d3 from 'd3'
import 'd3-graphviz'
import { ref } from 'vue';
import { onMounted } from 'vue';

const graph = ref(null)

const input_cls = ref(["ion-touched"])
const input_text = ref(props.dot)
const input_error = ref('')

function renderDot(value) {
    const div = d3.select(graph.value)
    div.graphviz().zoom(false)
        .renderDot(value, () => {
            d3.select(div.node().querySelector("svg")).attr("width", "100%")
            input_cls.value.length === 2 && input_cls.value.pop()
            input_error.value = ''
        }).onerror((err) => {
            input_cls.value.length === 1 && input_cls.value.push("ion-invalid")
            input_error.value = err
        });
}

onMounted(() => {
    renderDot(props.dot)
})

watchThrottled(input_text, (value, _) => {
    value && renderDot(value)
}, { throttle: 1000 })

</script>