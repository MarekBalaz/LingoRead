import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { AccountComponent } from './account/account.component';
import { TranslatorComponent } from './translator/translator.component';
import { TabsComponent } from './tabs/tabs.component';

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
                component: HomeComponent,
                path: "**"
            }
        ] 
    }
];
