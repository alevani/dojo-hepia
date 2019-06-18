import {AfterViewInit, Component, EventEmitter, Input, Output, ViewChild} from '@angular/core';
import 'brace';
import 'brace/theme/dracula';
import 'brace/mode/python';
import 'brace/mode/java';
import 'brace/mode/plain_text';

@Component({
  selector: 'app-terminal-code',
  templateUrl: './terminal-code.component.html',
  styleUrls: ['./terminal-code.component.scss']
})

export class TerminalCodeComponent implements AfterViewInit {

  @ViewChild('editor') editor: any;
  @Input() code = '';
  @Input() type = '';
  @Output() new = new EventEmitter<string>();

  onChange(code: string) {
    this.new.emit(code);
    this.code = code;
  }

  ngAfterViewInit() {
    this.editor.setTheme('dracula');
  }
}
