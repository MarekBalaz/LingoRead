package com.translatorApp.app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Binder;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

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
//      switch (intent.getAction()){
//        case "STOP": stopSelf();
//        break;
//        case "START": start();
//        break;
//      }
      start();
      return super.onStartCommand(intent, flags, startId);
    }
    private void start(){
      Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
        .setContentTitle("Speech Recognition Service")
        .setContentText("Listening for speech in the background")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
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
      speechRecognizer.stopListening();
    }
    public void unmute(){
      speechRecognizer.startListening(recognizerIntent);
    }
}

