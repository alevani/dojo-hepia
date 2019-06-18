import {AfterViewInit, Component, Input, ViewChild} from '@angular/core';
import 'brace';
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

  @ViewChild('editor') editor: any;
  @Input() code = '';
  @Input() type = '';

  ngAfterViewInit() {
    this.editor.setTheme('dracula');
    this.editor.getEditor().setOptions({
      enableBasicAutocompletion: true,
      highlightActiveLine: false
    });
  }
}
