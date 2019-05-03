import {AfterViewInit, Component, EventEmitter, Input, Output, ViewChild} from '@angular/core';
import 'brace/index';
import 'brace/theme/dracula';
import 'brace/mode/python';
import 'brace/mode/plain_text';

@Component({
  selector: 'app-ace',
  templateUrl: './ace.component.html',
  styleUrls: ['./ace.component.css']
})
export class AceComponent implements AfterViewInit {

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
/*
    this.editor.getEditor().setOptions({

    });*/

/*
    this.editor.getEditor().commands.addCommand({
      name: 'showOtherCompletions',
      bindKey: 'Ctrl-.',
      exec(editor) {
alert("");
      }
    });*/
  }

}
