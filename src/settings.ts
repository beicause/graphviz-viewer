import { Preferences } from "@capacitor/preferences";
import { watch } from "vue";
import { reactive } from "vue";

export const path = reactive({
    svg: "graphviz_viewer/svg",
    png: "graphviz_viewer/png",
    gv: "graphviz_viewer/gv"
})

Preferences.get({ key: 'storagePath' }).then(res => {
    if (res.value === null) {
        return
    }
    const { svg, png, gv } = JSON.parse(res.value)
    path.gv = gv
    path.png = png
    path.svg = svg
})

watch(path, (value, _) => {
    Preferences.set({ key: "storagePath", value: JSON.stringify(value) })
})