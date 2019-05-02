import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {MocComponent} from './moc/moc.component';

const routes: Routes = [
  {path: 'moc', component: MocComponent}
];

@NgModule({
  exports: [RouterModule],
  imports: [ RouterModule.forRoot(routes) ],
})

export class AppRoutingModule {

}
