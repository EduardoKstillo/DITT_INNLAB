import type { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'app.ditt.innlab',
  appName: 'frontend',
  webDir: 'www',
  server: {
    url: "http://192.168.1.6:8080",
    cleartext: true, // Permitir tráfico HTTP
  },
  plugins: {
    PushNotifications: {
      presentationOptions: ["badge", "sound", "alert"],
    },
  },
};

export default config;
