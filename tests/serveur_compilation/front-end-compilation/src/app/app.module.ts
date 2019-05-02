import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HighlightModule } from 'ngx-highlightjs';
import { AppComponent } from './app.component';
import { CompilateurComponent } from './compilateur/compilateur.component';
import {FormsModule} from '@angular/forms';

import python from 'highlight.js/lib/languages/python';

export function hljsLanguages() {
  return [
    {name: 'python', func: python}
  ];
}

@NgModule({
  declarations: [
    AppComponent,
    CompilateurComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HighlightModule.forRoot({ languages: hljsLanguages })
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
