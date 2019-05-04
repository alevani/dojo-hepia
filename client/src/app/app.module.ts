import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { TerminalCodeComponent } from './terminal-code/terminal-code.component';
import { TerminalAssertComponent } from './terminal-assert/terminal-assert.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MainLeftSideNavComponent } from './main-left-side-nav/main-left-side-nav.component';
import { LayoutModule } from '@angular/cdk/layout';
import { MatToolbarModule, MatButtonModule, MatSidenavModule, MatIconModule, MatListModule } from '@angular/material';
import { AppRoutingModule } from './app-routing.module';
import {RouterModule} from '@angular/router';
import { KataDisplayerComponent } from './kata-displayer/kata-displayer.component';
import { ProgramDisplayerComponent } from './program-displayer/program-displayer.component';
import { KataComponent } from './kata/kata.component';
import {AceEditorModule} from 'ng2-ace-editor';


@NgModule({
  declarations: [
    AppComponent,
    TerminalCodeComponent,
    TerminalAssertComponent,
    MainLeftSideNavComponent,
    KataDisplayerComponent,
    ProgramDisplayerComponent,
    KataComponent
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
    AceEditorModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
