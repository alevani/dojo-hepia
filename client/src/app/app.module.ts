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
import { ExoPythonSampleComponent } from './exo-python-sample/exo-python-sample.component';
import { KataDisplayerComponent } from './kata-displayer/kata-displayer.component';

@NgModule({
  declarations: [
    AppComponent,
    TerminalCodeComponent,
    TerminalAssertComponent,
    MainLeftSideNavComponent,
    ExoPythonSampleComponent,
    KataDisplayerComponent
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
    RouterModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
