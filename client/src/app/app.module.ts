import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {TerminalCodeComponent} from './component/terminal-code/terminal-code.component';
import {TerminalAssertComponent} from './component/terminal-assert/terminal-assert.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MainLeftSideNavComponent} from './component/main-left-side-nav/main-left-side-nav.component';
import {LayoutModule} from '@angular/cdk/layout';
import {
  MatToolbarModule,
  MatButtonModule,
  MatSidenavModule,
  MatIconModule,
  MatListModule,
  MatCardModule,
  MatInputModule,
  MatFormFieldModule,
  MatExpansionModule,
  MatGridListModule,
  MatSnackBarModule,
  MatCheckboxModule,
  MatBadgeModule,
  MatProgressSpinnerModule, MatBottomSheetModule, MatMenuModule, MatRadioModule
} from '@angular/material';

import {MatDialogModule} from '@angular/material/dialog';
import {AppRoutingModule} from './app-routing.module';
import {RouterModule} from '@angular/router';
import {
  DeleteProgramDialogComponent, DuplicateProgramDialogComponent,
  KataDisplayerComponent, PromptPasswordDialogComponent
} from './component/kata-displayer/kata-displayer.component';
import {ProgramDisplayerComponent} from './component/program-displayer/program-displayer.component';
import {KataComponent, KataSurrenderDialogComponent} from './component/kata/kata.component';
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
import {ProfilCardComponent} from './component/user/profil-card/profil-card.component';
import {CardDisplayerComponent} from './component/program-displayer/card-displayer/card-displayer.component';
import {SearchbyComponent} from './component/searchby/searchby.component';
import {SubscriptionComponent} from './component/subscription/subscription.component';
import {MineComponent} from './component/mine/mine.component';
import {CardNoneallDisplayerComponent} from './component/program-displayer/card-noneall-displayer/card-noneall-displayer.component';
import {SigninComponent} from './component/signin/signin.component';
import {TokenComponent} from './component/token/token.component';
import {ProgramEditComponent} from './component/program-edit/program-edit.component';
import {MarkdownModule} from 'ngx-markdown';
import {KataEditComponent} from './component/kata-edit/kata-edit.component';
import {GoalComponent} from './component/kata-create/goal/goal.component';
import {PdfViewerModule} from 'ng2-pdf-viewer';

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
    SearchbyComponent,
    SubscriptionComponent,
    MineComponent,
    CardNoneallDisplayerComponent,
    SigninComponent,
    TokenComponent,
    DeleteProgramDialogComponent,
    KataSurrenderDialogComponent,
    DuplicateProgramDialogComponent,
    PromptPasswordDialogComponent,
    ProgramEditComponent,
    KataEditComponent,
    GoalComponent,

  ],
  imports: [

    BrowserModule,
    MatIconModule,
    BrowserAnimationsModule,
    LayoutModule,
    MatToolbarModule,
    MatButtonModule,
    MatSidenavModule,
    MatIconModule,
    MatInputModule,
    MatFormFieldModule,
    MatExpansionModule,
    MatCheckboxModule,
    MatGridListModule,
    MatSnackBarModule,
    MatListModule,
    AppRoutingModule,
    RouterModule,
    AceEditorModule,
    FormsModule,
    HttpClientModule,
    NgxUiLoaderModule,
    AlertModule.forRoot({maxMessages: 5, timeout: 5000, position: 'right'}),
    MarkdownModule.forRoot(),
    ReactiveFormsModule,
    MatCardModule,
    MatBadgeModule,
    MatProgressSpinnerModule,
    MatDialogModule,
    MatBottomSheetModule,
    MatMenuModule,
    MatRadioModule,
    PdfViewerModule,
  ],
  entryComponents: [DeleteProgramDialogComponent, KataSurrenderDialogComponent, DuplicateProgramDialogComponent, PromptPasswordDialogComponent, KataDisplayerComponent],
  providers: [{provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true}, {
    provide: HTTP_INTERCEPTORS,
    useClass: ErrorInterceptor,
    multi: true
  }],

  bootstrap: [AppComponent]

})
export class AppModule {
}
