import { Component } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { IonicModule } from '@ionic/angular';
@Component({
  selector: 'app-root',
  templateUrl: 'account.component.html',
  standalone: true,
  imports: [RouterOutlet, IonicModule]
})
export class AccountComponent{

  constructor(private router: Router){

  }
  
  onCardClick(card: String) {
    setTimeout(() => {
      this.router.navigate([card]);
    }, 120)
  }

}
