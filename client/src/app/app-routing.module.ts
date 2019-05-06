import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ProgramDisplayerComponent} from './program-displayer/program-displayer.component';
import {KataDisplayerComponent} from './kata-displayer/kata-displayer.component';
import {KataComponent} from './kata/kata.component';
import {ProgramCreateComponent} from './program-create/program-create.component';
import {KataCreateComponent} from './kata-create/kata-create.component';

const routes: Routes = [
  {path: 'program_test', component: ProgramDisplayerComponent},
  {path: 'kata-displayer/:id/:title/:language/:sensei', component: KataDisplayerComponent},
  {path: 'kata/:id', component: KataComponent},
  {path: 'program_create', component: ProgramCreateComponent},
  {path: 'kata_create/:id/:language', component: KataCreateComponent},
];

@NgModule({
  declarations: [],

  exports: [RouterModule],
  imports: [RouterModule.forRoot(routes)],

})
export class AppRoutingModule {
}
