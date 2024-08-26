import type { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'com.translatorApp.app',
  appName: 'translatorApp',
  webDir: 'www',
  plugins: {
    SpeechMutePlugin: {
      package: "com.translatorapp.plugins.speechmute"
    }
  }
};

export default config;
