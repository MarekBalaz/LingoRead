package com.translatorApp.app;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import androidx.core.app.ActivityCompat;

import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {

  private SpeechRecognitionService service;
  private boolean bound = false;

  private ServiceConnection connection = new ServiceConnection() {

    @Override
    public void onServiceConnected(ComponentName className, IBinder serviceBinder) {
      SpeechRecognitionService.LocalBinder binder = (SpeechRecognitionService.LocalBinder) serviceBinder;
      service = binder.getService();
      bound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName arg0) {
      bound = false;
    }
  };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Request RECORD_AUDIO permission if it has not been granted
        ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.POST_NOTIFICATIONS},
                    0);

        Intent serviceIntent = new Intent(this, SpeechRecognitionService.class);
        serviceIntent.setAction("START");
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        startForegroundService(serviceIntent);
      }
    }
    public void muteSpeechReco(){
      if(bound){
        service.mute();
      }
    }
    public void unmuteSpeechReco(){
      if(bound){
        service.unmute();
      }
    }
}
