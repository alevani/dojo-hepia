import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {TerminalCodeComponent} from './component/terminal-code/terminal-code.component';
import {TerminalAssertComponent} from './component/terminal-assert/terminal-assert.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MainLeftSideNavComponent} from './component/main-left-side-nav/main-left-side-nav.component';
import {LayoutModule} from '@angular/cdk/layout';
import {MatToolbarModule, MatButtonModule, MatSidenavModule, MatIconModule, MatListModule, MatCardModule} from '@angular/material';
import {AppRoutingModule} from './app-routing.module';
import {RouterModule} from '@angular/router';
import {KataDisplayerComponent} from './component/kata-displayer/kata-displayer.component';
import {ProgramDisplayerComponent} from './component/program-displayer/program-displayer.component';
import {KataComponent} from './component/kata/kata.component';
import {AceEditorModule} from 'ng2-ace-editor';
import {KataCreateComponent} from './component/kata-create/kata-create.component';
import {ProgramCreateComponent} from './component/program-create/program-create.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule, HTTP_INTERCEPTORS} from '@angular/common/http';
import {RNotFoundComponent} from './component/rnot-found/rnot-found.component';
import {NgxUiLoaderModule} from 'ngx-ui-loader';
import {AlertModule} from 'ngx-alerts';
import {LoginComponent} from './component/login/login.component';
import {JwtInterceptor} from './_helper/jwt.interceptor';
import {ErrorInterceptor} from './_helper/error.interceptor';
import { ProfilCardComponent } from './component/user/profil-card/profil-card.component';
import { CardDisplayerComponent } from './component/program-displayer/card-displayer/card-displayer.component';
import { SearchbyComponent } from './component/searchby/searchby.component';

@NgModule({
  declarations: [
    AppComponent,
    TerminalCodeComponent,
    TerminalAssertComponent,
    MainLeftSideNavComponent,
    KataDisplayerComponent,
    ProgramDisplayerComponent,
    KataComponent,
    KataCreateComponent,
    ProgramCreateComponent,
    RNotFoundComponent,
    LoginComponent,
    ProfilCardComponent,
    CardDisplayerComponent,
    SearchbyComponent
  ],
  imports: [

    BrowserModule,
    BrowserAnimationsModule,
    LayoutModule,
    MatToolbarModule,
    MatButtonModule,
    MatSidenavModule,
    MatIconModule,
    MatListModule,
    AppRoutingModule,
    RouterModule,
    AceEditorModule,
    FormsModule,
    HttpClientModule,
    NgxUiLoaderModule,
    AlertModule.forRoot({maxMessages: 5, timeout: 5000, position: 'right'}),
    ReactiveFormsModule,
    MatCardModule

  ],
  providers: [{provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true}, {
    provide: HTTP_INTERCEPTORS,
    useClass: ErrorInterceptor,
    multi: true
  }],

  bootstrap: [AppComponent]

})
export class AppModule {
}
