import { ref } from "vue"

export const input_cls = ref(["ion-touched"])
export const input_text = ref(`digraph {
    node[shape=box style=filled color=blue  fillcolor=yellow]
    a -> { b c }
}`)
export const input_error = ref('')
export const input_style = ref({ height: "100%" })