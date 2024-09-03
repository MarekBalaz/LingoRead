import { Component } from '@angular/core';
import { Plugins } from '@capacitor/core';
import { IonicModule } from '@ionic/angular';
import { IonRouterLink } from '@ionic/angular/standalone';
const { SpeechMutePlugin } = Plugins;
declare var webkitSpeechRecognition: any;

@Component({
  selector: 'app-translator',
  templateUrl: './translator.component.html',
  styleUrls: ['./translator.component.scss'],
  standalone: true,
  imports: [IonicModule]
})
export class TranslatorComponent{

  mute: boolean = false;

    // url = `https://translation.googleapis.com/language/translate/v2?key=AIzaSyDTAKU5BuWq_aeT_KJa2G9yLNlVWQggyMg`;
    // isMute = false;
    // recognition: typeof webkitSpeechRecognition;
    // isSpeechRecognitionActive = false;
    // isListening = true;
    // constructor(private http: HttpClient) {}
    
    // ngOnInit(){
    //     this.initializeSpeechRecognition()
    //     this.recognition.start()
    // }

    // ngOnDestroy(){
    //     this.recognition.stop()
    // }

    // initializeSpeechRecognition(){
    //     if (!('webkitSpeechRecognition' in window)) {
    //         console.error("webkitSpeechRecognition is not supported in this browser")
    //     } else {
    //         this.recognition = new webkitSpeechRecognition();
    //         this.recognition.continuous = true;
    //         this.recognition.lang = "en-US";
    //         this.recognition.onresult = (event: any) => {
    //             let final_transcript = '';
                
    //             for (let i = event.resultIndex; i < event.results.length; i++) {
    //                 final_transcript += event.results[i][0].transcript;
    //             }

    //             console.log(final_transcript)

    //             this.translate(final_transcript)
                
    //         };
    //         this.recognition.onend = () => {
    //             if(this.isListening) this.recognition.start();
    //           };
    //     }
    // }
    // changeListeningState(){
    //     if(this.isListening){
    //         this.recognition.stop()
    //         this.isListening = false
    //     }
    //     else{
    //         this.recognition.start()
    //         this.isListening = true
    //     }
        
    // }
    // translate(text: string){
    //     const body = {
    //         q: text,
    //         target: "es",
    //         source: "en"
    //     };
    //     this.http.post(this.url, body).subscribe(
    //         async (response: any) => {
    //             let translatedText = response.data.translations[0].translatedText;
    //             this.TTS(translatedText)
    //         },
    //         (error) => {
    //             console.error('Error translating text:', error);
    //         }
    //     );
    // }
    // async TTS(text: string, lang: string = 'es-ES', rate: number = 1.0, pitch: number = 1.0): Promise<void> {
    //     try {
    //       await TextToSpeech.speak({
    //         text: text,
    //         lang: lang,
    //         rate: rate,
    //         pitch: pitch,
    //         volume: 1.0,
    //         category: 'ambient'
    //       });
    //     } catch (error) {
    //       console.error('Error in TextToSpeech:', error);
    //     }
    // }

    onMicButtonClick(){
      let mic = document.getElementById("micIcon");
        if(this.mute){
          SpeechMutePlugin['muteInJava']();
          mic?.classList.add("fa-microphone-slash")
          mic?.classList.remove("fa-microphone")
          this.mute = false;
        }
        else{
          SpeechMutePlugin['unmuteInJava']();
          mic?.classList.add("fa-microphone")
          mic?.classList.remove("fa-microphone-slash")
          this.mute = true;
        }
    }

}
