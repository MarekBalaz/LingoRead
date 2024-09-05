import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-saved',
  templateUrl: './saved.component.html',
  standalone: true,
  styleUrls: ['./saved.component.scss'],
  imports: [IonicModule]
})
export class SavedComponent {


  private searchTerm: String = "";
  constructor(private router: Router){

  }
  
  onCardClick(card: String) {
    setTimeout(() => {
      this.router.navigate([card], {queryParams: {group: "spanish"}});
    }, 120)
  }

  filterItems($event: any){

  }

}
