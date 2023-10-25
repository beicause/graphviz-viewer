import { watch, ref } from 'vue';
import { Preferences } from '@capacitor/preferences'
import { App } from "@capacitor/app";
import { MPlugin } from './plugin';

export const themeMode = ref("system");

const prefersDark = async () => {
    if (MPlugin && MPlugin.getSystemTheme) return (await MPlugin.getSystemTheme()).isDark
    return window.matchMedia('(prefers-color-scheme: dark)').matches;
}
function toggleDarkTheme(shouldAdd: boolean) {
    document.body.classList.toggle('dark', shouldAdd);
};

async function setTheme(mode: string) {
    switch (mode) {
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
}
watch(themeMode, async (value: string, _) => {
    await Preferences.set({ key: 'themeMode', value });
    await setTheme(value)
})

App.addListener("resume", async () => {
    themeMode.value === "system" && toggleDarkTheme(await prefersDark());
})

Preferences.get({ key: 'themeMode' }).then(async (res) => {
    themeMode.value = res.value || "system"
    await setTheme(themeMode.value)
})