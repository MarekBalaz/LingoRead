import { Component, OnInit } from '@angular/core';
import { IonicModule } from '@ionic/angular';

@Component({
  selector: 'app-tabs',
  templateUrl: './tabs.component.html',
  standalone: true,
  styleUrls: ['./tabs.component.scss'],
  imports: [IonicModule]
})
export class TabsComponent  implements OnInit {

  constructor() { }

  ngOnInit() {}

}
