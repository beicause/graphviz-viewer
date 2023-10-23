import { registerPlugin } from "@capacitor/core";
import { isPlatform } from "@ionic/vue";

export interface MPlugin {
    getSystemTheme: () => Promise<{ isDark: boolean }>;
    getIntentViewText: () => Promise<{ data: string | null }>
    showInterstitialAd: () => Promise<void>
}

export const MPlugin = isPlatform("android") ? registerPlugin<MPlugin>("M") : null 
