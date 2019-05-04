import {AfterViewInit, Component, EventEmitter, Input, Output, ViewChild} from '@angular/core';
import 'brace/index';
import 'brace/theme/dracula';
import 'brace/mode/python';
import 'brace/mode/java';
import 'brace/mode/plain_text';

@Component({
  selector: 'app-terminal-code',
  templateUrl: './terminal-code.component.html',
  styleUrls: ['./terminal-code.component.css']
})

export class TerminalCodeComponent implements AfterViewInit {

  @ViewChild('editor') editor;
  @Input() code: string;
  @Input() type: string;
  @Output() new = new EventEmitter<string>();

  onChange(code) {
    this.new.emit(code);
    this.code = code;
  }

  ngAfterViewInit() {
    this.editor.setTheme('dracula');
  }
}
