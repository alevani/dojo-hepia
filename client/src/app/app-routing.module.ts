import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ProgramDisplayerComponent} from './program-displayer/program-displayer.component';

const routes: Routes = [
  {path: 'program_test', component: ProgramDisplayerComponent}
];

@NgModule({
  declarations: [],

  exports: [RouterModule],
  imports: [RouterModule.forRoot(routes)],

})
export class AppRoutingModule {
}
