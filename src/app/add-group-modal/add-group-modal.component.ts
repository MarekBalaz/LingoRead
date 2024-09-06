import { Component, OnInit } from '@angular/core';
import {IonicModule, ModalController} from "@ionic/angular";
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-add-group-modal',
  templateUrl: './add-group-modal.component.html',
  standalone: true,
  styleUrls: ['./add-group-modal.component.scss'],
  imports: [IonicModule, FormsModule]
})
export class AddGroupModalComponent {

  itemName: string = '';

  constructor(private modalCtrl: ModalController) {}

  // Dismiss modal without action
  dismiss() {
    this.modalCtrl.dismiss();
  }

  // Add the item and close modal
  addItem() {
    if (this.itemName.trim()) {
      this.modalCtrl.dismiss({ item: this.itemName });
    }
  }

}
