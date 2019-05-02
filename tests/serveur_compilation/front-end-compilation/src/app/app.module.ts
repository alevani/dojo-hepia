import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { CompilateurComponent } from './compilateur/compilateur.component';
import {FormsModule} from '@angular/forms';
import { AceEditorModule } from 'ng2-ace-editor';
import { AceComponent } from './ace/ace.component';
import { AceAssertComponent } from './ace-assert/ace-assert.component';

@NgModule({
  declarations: [
    AppComponent,
    CompilateurComponent,
    AceComponent,
    AceAssertComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AceEditorModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
