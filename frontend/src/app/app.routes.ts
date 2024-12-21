import { Routes } from '@angular/router';
import {CreateSubjectComponent} from './pages/create-subject/create-subject.component';
import {DashboardComponent} from './pages/dashboard/dashboard.component';
import {NotFoundComponent} from './pages/not-found/not-found.component';
import {ConnexionComponent} from './pages/connexion/connexion.component';
import {InscriptionComponent} from './pages/inscription/inscription.component';
import {HomePageComponent} from './pages/home-page/home-page.component';
import {ListSubjectComponent} from './pages/list-subject/list-subject.component';
import {adminGuardGuard} from './guard/admin-guard.guard';
import {AproposComponent} from './pages/apropos/apropos.component';

export const routes: Routes = [
  { path: 'sujet', component: CreateSubjectComponent, canActivate: [adminGuardGuard] },
  { path: 'sujet/:id', component: CreateSubjectComponent,canActivate: [adminGuardGuard] },
  { path: 'dashboard', component: DashboardComponent,canActivate: [adminGuardGuard] },
  {
    path: "",
    redirectTo :"home",
    pathMatch: 'full'
  },
  {path:"home",component:HomePageComponent},
  {path:"login",component:ConnexionComponent},
  {path:"register",component:InscriptionComponent},
  {path:"subjects",component:ListSubjectComponent},
  {path:"a-propos", component:AproposComponent},
  {path: "**", component: NotFoundComponent},


];
