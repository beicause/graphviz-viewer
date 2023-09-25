import { watch, ref } from 'vue';
import { Preferences } from '@capacitor/preferences'

const theme = ref("system");

Preferences.get({ key: 'themeMode' }).then(res => theme.value = res.value || "system")

const prefersDark = window.matchMedia('(prefers-color-scheme: dark)');
function toggleDarkTheme(shouldAdd: boolean) {
    document.body.classList.toggle('dark', shouldAdd);
};
prefersDark.addEventListener('change', (mediaQuery) => theme.value == "system" && toggleDarkTheme(mediaQuery.matches));

watch(theme, async (value: string, _) => {
    await Preferences.set({ key: 'themeMode', value });
    switch (value) {
        case "system":
            toggleDarkTheme(prefersDark.matches);
            break;
        case "dark":
            toggleDarkTheme(true);
            break;
        case "light":
            toggleDarkTheme(false);
            break;
    }
}, { immediate: true })

export { theme, toggleDarkTheme, prefersDark }