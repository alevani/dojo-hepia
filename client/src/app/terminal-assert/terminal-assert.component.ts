import {AfterViewInit, Component, Input, ViewChild} from '@angular/core';
import 'brace/index';
import 'brace/theme/dracula';
import 'brace/mode/python';
import 'brace/mode/java';
import 'brace/mode/plain_text';

@Component({
  selector: 'app-terminal-assert',
  templateUrl: './terminal-assert.component.html',
  styleUrls: ['./terminal-assert.component.scss']
})
export class TerminalAssertComponent implements AfterViewInit {

  @ViewChild('editor') editor;
  @Input() code: string;
  @Input() type: string;

  ngAfterViewInit() {
    this.editor.setTheme('dracula');
    this.editor.getEditor().setOptions({
      enableBasicAutocompletion: true,
      highlightActiveLine: false
    });
  }
}
