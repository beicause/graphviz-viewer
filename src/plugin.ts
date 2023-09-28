import { registerPlugin } from "@capacitor/core";

export interface MPlugin {
    getSystemTheme: () => Promise<{ isDark: boolean }>;
    getIntentViewText: () => Promise<{ data: string|null }>
}

export const MPlugin = registerPlugin<MPlugin>("M") 
