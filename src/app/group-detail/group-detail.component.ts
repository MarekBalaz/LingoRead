import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-group-detail',
  templateUrl: './group-detail.component.html',
  standalone: true,
  styleUrls: ['./group-detail.component.scss'],
  imports: [IonicModule]
})
export class GroupDetailComponent{

  constructor(private router: Router) { }

  onCardClick(card: String) {
    setTimeout(() => {
      this.router.navigate([card]);
    }, 150)
  }

}
