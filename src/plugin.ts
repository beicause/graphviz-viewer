import { registerPlugin,Capacitor } from "@capacitor/core";

export interface MPlugin {
    getSystemTheme: () => Promise<{ isDark: boolean }>;
    getIntentViewText: () => Promise<{ data: string | null }>
    showInterstitialAd: () => Promise<void>
}

export const MPlugin = Capacitor.isPluginAvailable("M") ? registerPlugin<MPlugin>("M") : null 
