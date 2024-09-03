import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { IonicModule } from '@ionic/angular';
@Component({
  selector: 'app-root',
  templateUrl: 'account.component.html',
  standalone: true,
  imports: [RouterOutlet, IonicModule]
})
export class AccountComponent{

}
