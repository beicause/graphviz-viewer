import { watch, ref } from 'vue';
import { Preferences } from '@capacitor/preferences'
import { registerPlugin } from "@capacitor/core";
import { App } from "@capacitor/app";

export const themeMode = ref("system");
const M = registerPlugin("M") as any

Preferences.get({ key: 'themeMode' }).then(res => themeMode.value = res.value || "system")

const prefersDark = async () => M.getThemeMode ? (await M.getThemeMode()).isDark : window.matchMedia('(prefers-color-scheme: dark)').matches;
function toggleDarkTheme(shouldAdd: boolean) {
    document.body.classList.toggle('dark', shouldAdd);
};

watch(themeMode, async (value: string, _) => {
    await Preferences.set({ key: 'themeMode', value });
    switch (value) {
        case "system":
            toggleDarkTheme(await prefersDark());
            break;
        case "dark":
            toggleDarkTheme(true);
            break;
        case "light":
            toggleDarkTheme(false);
            break;
    }
}, { immediate: true })

App.addListener("resume", async () => {
    themeMode.value === "system" && toggleDarkTheme(await prefersDark());
})