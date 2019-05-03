
import {AfterViewInit, Component, Input, ViewChild} from '@angular/core';
import 'brace/index';
import 'brace/theme/dracula';
import 'brace/mode/python';
import 'brace/mode/java';
import 'brace/mode/plain_text';

@Component({
  selector: 'app-ace-assert',
  templateUrl: './ace-assert.component.html',
  styleUrls: ['./ace-assert.component.css']
})
export class AceAssertComponent implements AfterViewInit {
  @ViewChild('editor') editor;
  @Input() code: string;
  @Input() type: string;

  ngAfterViewInit() {
    this.editor.setTheme('dracula');
    this.editor.getEditor().setOptions({
      enableBasicAutocompletion: true,
      highlightActiveLine : false
    });
/*
    this.editor.getEditor().commands.addCommand({
      name: 'showOtherCompletions',
      bindKey: 'Ctrl-.',
      exec(editor) {

      }
    });*/
  }

}
