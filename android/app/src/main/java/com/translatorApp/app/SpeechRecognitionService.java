package com.translatorApp.app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Binder;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.Voice;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SpeechRecognitionService extends Service implements OnInitListener {
    private static final String CHANNEL_ID = "SpeechRecognitionServiceChannel";
    private SpeechRecognizer speechRecognizer;
    private Intent recognizerIntent;
    private TextToSpeech textToSpeech;
    private OkHttpClient client = new OkHttpClient();
    private boolean isListening = false;
    private final IBinder binder = new LocalBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        initializeSpeechRecognizer();
        initializeTextToSpeech();
    }
    @Override
    public void onInit(int status){
//      if(status == TextToSpeech.SUCCESS){
//        initializeTextToSpeech();
//      }
//      else{
//        onDestroy();
//      }
    }
    private void initializeTextToSpeech(){
      textToSpeech = new TextToSpeech(this, this);
      textToSpeech.setLanguage(new Locale("es", "ES"));
      textToSpeech.setPitch(0.8f);
    }
    private void initializeSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
              isListening = true;
            }

            @Override
            public void onBeginningOfSpeech() {}

            @Override
            public void onRmsChanged(float v) {}

            @Override
            public void onBufferReceived(byte[] bytes) {}

            @Override
            public void onEndOfSpeech() {
            }

            @Override
            public void onError(int i) {
              if(isListening){
                isListening = false;
                speechRecognizer.stopListening();
                speechRecognizer.startListening(recognizerIntent);
              }
            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> result = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (result != null && !result.isEmpty()) {
                    String recognizedText = result.get(0);
                    translate(recognizedText);
                }
                speechRecognizer.stopListening();
                speechRecognizer.startListening(recognizerIntent);
              if(isListening){
                isListening = false;
                speechRecognizer.stopListening();
                speechRecognizer.startListening(recognizerIntent);
              }
            }

            @Override
            public void onPartialResults(Bundle bundle) {}

            @Override
            public void onEvent(int i, Bundle bundle) {}
        });

        speechRecognizer.startListening(recognizerIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
      Log.e("ACTION", intent.getAction());
      if(intent != null && intent.getAction() != null && intent.getAction().equals("MUTE_ACTION")) {
        mute();
      }
      else if (intent != null && intent.getAction() != null && intent.getAction().equals("UNMUTE_ACTION")){
        unmute();
      }
      else{
        start();
      }

      return super.onStartCommand(intent, flags, startId);
    }
    private void start(){
//      RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.custom_notification);
//
//      // Set initial icon and background color
//      notificationLayout.setImageViewResource(R.id.micButton, isMute ? R.drawable.baseline_mic_off_24 : R.drawable.baseline_mic_24);
//      notificationLayout.setInt(R.id.micButton, "setColorFilter", ContextCompat.getColor(this, com.getcapacitor.android.R.color.colorPrimary));
//
//      notificationLayout.setImageViewResource(R.id.notificationIcon, R.mipmap.logo_notif_round);
//      notificationLayout.setTextViewText(R.id.notificationText, "Listening");
//
//      // Intent for button click
//      Intent toggleIntent = new Intent(this, SpeechRecognitionService.class);
//      toggleIntent.setAction("MUTE_ACTION");
//      PendingIntent pendingToggleIntent = PendingIntent.getService(this, 0, toggleIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
//      notificationLayout.setOnClickPendingIntent(R.id.micButton, pendingToggleIntent);
//
//      Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
//        .setSmallIcon(R.mipmap.logo_notif_round)
//        .setCustomContentView(notificationLayout)
//        .setPriority(NotificationCompat.PRIORITY_HIGH)
//        .build();
      Intent muteIntent = new Intent(this, SpeechRecognitionService.class);
      muteIntent.setAction("MUTE_ACTION");

      PendingIntent mutePendingIntent = PendingIntent.getService(
        this, 0, muteIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
      );

      Intent unmuteIntent = new Intent(this, SpeechRecognitionService.class);
      unmuteIntent.setAction("UNMUTE_ACTION");

      PendingIntent unmutePendingIntent = PendingIntent.getService(
        this, 0, unmuteIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
      );

      Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
        .setContentTitle("Lingo")
        .setContentText("Listening...")
        .setColor(Color.rgb(23,150,95))
        .setSmallIcon(R.drawable.baseline_mic_24)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setOngoing(true)
        .addAction(R.drawable.baseline_mic_24, "Mute", mutePendingIntent)
        .addAction(R.drawable.baseline_mic_off_24, "Unmute", unmutePendingIntent)
        .build();

      startForeground(1, notification);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (speechRecognizer != null) {
            speechRecognizer.stopListening();
            speechRecognizer.cancel();
            speechRecognizer.destroy();
        }
        if(textToSpeech != null){
          textToSpeech.stop();
          textToSpeech.shutdown();
        }
    }

    public class LocalBinder extends Binder {
      SpeechRecognitionService getService() {
        return SpeechRecognitionService.this;
      }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Speech Recognition Service Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

  public void translate(String text) {
    String url = "https://translation.googleapis.com/language/translate/v2?key=AIzaSyDTAKU5BuWq_aeT_KJa2G9yLNlVWQggyMg";
    String json = "{\"q\":\"" + text +"\",\"target\":\"es\",\"source\":\"en\"}";
    RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
    Request request = new Request.Builder()
      .url(url)
      .post(body)
      .build();

    client.newCall(request).enqueue(new Callback() {
      @Override
      public void onFailure(Call call, IOException e) {
        e.printStackTrace(); // Handle the error
      }

      @Override
      public void onResponse(Call call, Response response) throws IOException {
        if (!response.isSuccessful()) {
          throw new IOException("Unexpected code " + response);
        }

        // Handle the response
        ObjectMapper mapper = new ObjectMapper();
        String responseData = response.body().string();
        TranslationsDTO translationsDTO = mapper.readValue(responseData, TranslationsDTO.class);
        if(translationsDTO.data.translations.size() == 0){
          speak("No translations found");
        }
        else{
          Log.e("Data", translationsDTO.data.translations.get(0).translatedText);
          speak(translationsDTO.data.translations.get(0).translatedText);
        }
      }
    });
  }

    private void speak(String text){
      textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }
    public void mute(){
      if(isListening){
        speechRecognizer.stopListening();
        isListening = false;
      }
    }
    public void unmute(){
      Log.e("Listen", "Unmuting: " + isListening);
      if(!isListening){
        speechRecognizer.startListening(recognizerIntent);
        isListening = true;
        Log.e("Listen", "Unmuted");
      }
    }
}

