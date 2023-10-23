import { MPlugin } from "@/plugin"
import { App } from "@capacitor/app"
import { ref } from "vue"

export const input_cls = ref(["ion-touched"])
export const input_text = ref(`digraph {
    node[shape=box style=filled color=blue  fillcolor=yellow]
    a -> { b c }
}`)
export const input_error = ref('')
export const input_style = ref({ height: "100%" })

getIntent()

App.addListener("resume", async () => {
    getIntent()
})
async function getIntent() {
    if (!MPlugin) return
    const res = await MPlugin.getIntentViewText()
    if (res.data) input_text.value = res.data
}