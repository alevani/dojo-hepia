import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ExoPythonSampleComponent} from './exo-python-sample/exo-python-sample.component';

const routes: Routes = [
  {path: 'python_sample', component: ExoPythonSampleComponent}
];

@NgModule({
  declarations: [],

  exports: [RouterModule],
  imports: [RouterModule.forRoot(routes)],

})
export class AppRoutingModule {
}
