import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  standalone: true,
  imports: [IonicModule]
})
export class HomeComponent {

  constructor(private router: Router) { }

  onCardClick(card: String) {
    setTimeout(() => {
      this.router.navigate([card]);
    }, 150)
  }

}
