import { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'indie.haozi.gvviewer',
  appName: 'gv-viewer',
  webDir: 'dist',
  server: {
    androidScheme: 'https'
  }
};

export default config;
