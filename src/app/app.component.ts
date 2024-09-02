import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { IonicModule } from '@ionic/angular';
import { TabsComponent } from './tabs/tabs.component';
@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  standalone: true,
  imports: [RouterOutlet, IonicModule, TabsComponent]
})
export class AppComponent{

}