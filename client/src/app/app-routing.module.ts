import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ProgramDisplayerComponent} from './component/program-displayer/program-displayer.component';
import {KataDisplayerComponent} from './component/kata-displayer/kata-displayer.component';
import {KataComponent} from './component/kata/kata.component';
import {ProgramCreateComponent} from './component/program-create/program-create.component';
import {KataCreateComponent} from './component/kata-create/kata-create.component';
import {LoginComponent} from './component/login/login.component';
import {AuthGuard} from './_guard/auth.guard';
import {SearchbyComponent} from './component/searchby/searchby.component';
import {Role} from './_helper/_models/roles';
import {SubscriptionComponent} from './component/subscription/subscription.component';
import {MineComponent} from './component/mine/mine.component';
import {SigninComponent} from './component/signin/signin.component';
import {TokenComponent} from './component/token/token.component';
import {ProgramEditComponent} from './component/program-edit/program-edit.component';

const routes: Routes = [
  {path: '', component: ProgramDisplayerComponent, canActivate: [AuthGuard], data: {roles: [Role.shodai, Role.monji, Role.sensei]}},

  {path: 'user/login', component: LoginComponent},
  {path: 'user/signin', component: SigninComponent},
  {path: 'program/all', component: ProgramDisplayerComponent, canActivate: [AuthGuard], data: {roles: [Role.shodai, Role.monji, Role.sensei]}},
  {path: 'token', component: TokenComponent, canActivate: [AuthGuard], data: {roles: [Role.shodai, Role.sensei]}},
  {path: 'program/subscriptions', component: SubscriptionComponent, canActivate: [AuthGuard], data: {roles: [Role.shodai, Role.monji, Role.sensei]}},
  {path: 'program/mine', component: MineComponent, canActivate: [AuthGuard], data: {roles: [Role.shodai, Role.sensei]}},
  {path: 'program/search', component: SearchbyComponent, canActivate: [AuthGuard], data: {roles: [Role.shodai, Role.monji, Role.sensei]}},
  {path: 'kata-displayer/:id', component: KataDisplayerComponent, canActivate: [AuthGuard], data: {roles: [Role.shodai, Role.monji, Role.sensei]}},
  {path: 'kata/:prid/:id', component: KataComponent, canActivate: [AuthGuard], data: {roles: [Role.shodai, Role.monji, Role.sensei]}},
  {path: 'program/create', component: ProgramCreateComponent, canActivate: [AuthGuard], data: {roles: [Role.shodai, Role.sensei]}},
  {path: 'program/edit/:id', component: ProgramEditComponent, canActivate: [AuthGuard], data: {roles: [Role.shodai, Role.sensei]}},
  {path: 'kata_create/:id/:language', component: KataCreateComponent, canActivate: [AuthGuard], data: {roles: [Role.shodai, Role.sensei]}},
  {path: '**', redirectTo: ''}
];

@NgModule({
  declarations: [],

  exports: [RouterModule],
  imports: [RouterModule.forRoot(routes)],

})
export class AppRoutingModule {
}
