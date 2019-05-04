import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ProgramDisplayerComponent} from './program-displayer/program-displayer.component';
import {KataDisplayerComponent} from './kata-displayer/kata-displayer.component';
import {KataComponent} from './kata/kata.component';

const routes: Routes = [
  {path: 'program_test', component: ProgramDisplayerComponent},
  {path: 'kata-displayer/:id/:title/:language/:sensei', component: KataDisplayerComponent},
  {path: 'kata/:id', component: KataComponent}
];

@NgModule({
  declarations: [],

  exports: [RouterModule],
  imports: [RouterModule.forRoot(routes)],

})
export class AppRoutingModule {
}
