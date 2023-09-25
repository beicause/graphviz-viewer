import { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'indie.haozi.graphvizviewer',
  appName: 'graphviz-viewer',
  webDir: 'dist',
  server: {
    androidScheme: 'https'
  }
};

export default config;
