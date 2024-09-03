import { Component, OnInit } from '@angular/core';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  standalone: true,
  imports: [IonicModule]
})
export class HomeComponent  implements OnInit {

  constructor() { }

  ngOnInit() {}

}
