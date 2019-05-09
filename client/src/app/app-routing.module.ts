import {NgModule} from '@angular/core';
import {ExtraOptions, RouterModule, Routes} from '@angular/router';
import {ProgramDisplayerComponent} from './program-displayer/program-displayer.component';
import {KataDisplayerComponent} from './kata-displayer/kata-displayer.component';
import {KataComponent} from './kata/kata.component';
import {ProgramCreateComponent} from './program-create/program-create.component';
import {KataCreateComponent} from './kata-create/kata-create.component';
import {TestJwtComponent} from './test-jwt/test-jwt.component';

const routerOptions: ExtraOptions = {
  useHash: false,
  anchorScrolling: 'enabled'
};

const routes: Routes = [
  { path: '', redirectTo: '/token', pathMatch: 'full' },

  {path: 'programs', component: ProgramDisplayerComponent},
  {path: 'kata-displayer/:id', component: KataDisplayerComponent},
  {path: 'kata/:prid/:id', component: KataComponent},
  {path: 'program_create', component: ProgramCreateComponent},
  {path: 'kata_create/:id/:language', component: KataCreateComponent},
  {path: 'token', component: TestJwtComponent},
];

@NgModule({
  declarations: [],

  exports: [RouterModule],
  imports: [RouterModule.forRoot(routes, routerOptions)],

})
export class AppRoutingModule {
}
