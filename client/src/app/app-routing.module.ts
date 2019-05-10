import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ProgramDisplayerComponent} from './component/program-displayer/program-displayer.component';
import {KataDisplayerComponent} from './component/kata-displayer/kata-displayer.component';
import {KataComponent} from './component/kata/kata.component';
import {ProgramCreateComponent} from './component/program-create/program-create.component';
import {KataCreateComponent} from './component/kata-create/kata-create.component';
import {LoginComponent} from './component/login/login.component';
import {AuthGuard} from './_guard/auth.guard';

const routes: Routes = [
  {path: '', component: ProgramDisplayerComponent, canActivate: [AuthGuard]},

  {path: 'login', component: LoginComponent},
  {path: 'programs', component: ProgramDisplayerComponent, canActivate: [AuthGuard]},
  {path: 'kata-displayer/:id', component: KataDisplayerComponent, canActivate: [AuthGuard]},
  {path: 'kata/:prid/:id', component: KataComponent, canActivate: [AuthGuard]},
  {path: 'program_create', component: ProgramCreateComponent, canActivate: [AuthGuard]},
  {path: 'kata_create/:id/:language', component: KataCreateComponent, canActivate: [AuthGuard]},
  {path: '**', redirectTo: ''}
];

@NgModule({
  declarations: [],

  exports: [RouterModule],
  imports: [RouterModule.forRoot(routes)],

})
export class AppRoutingModule {
}
