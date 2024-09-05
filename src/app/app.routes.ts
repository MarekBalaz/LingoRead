import { Routes } from '@angular/router';
import {AccountComponent} from "./account/account.component";
import { TabsComponent } from './tabs/tabs.component';
import { HomeComponent } from './home/home.component';
import { TranslatorComponent } from './translator/translator.component';
import { SavedComponent } from './saved/saved.component';
import { GroupDetailComponent } from './group-detail/group-detail.component';
// import { HomeComponent } from './home/home.component';
// import { AccountComponent } from './account/account.component';
// import { TranslatorComponent } from './translator/translator.component';
// import { TabsComponent } from './tabs/tabs.component';

export const routes: Routes = [
    {
        path: '',
        component: TabsComponent,
        children: [
            {
                component: HomeComponent,
                path: "home"
            },
            {
                component: AccountComponent,
                path: "account"
            },
            {
                component: TranslatorComponent,
                path: "translator"
            },
            {
                component: SavedComponent,
                path: "saved"
            },
            {
                component: GroupDetailComponent,
                path: "detail"
            },
            {
                component: HomeComponent,
                path: "**"
            }
        ]
    }
];
