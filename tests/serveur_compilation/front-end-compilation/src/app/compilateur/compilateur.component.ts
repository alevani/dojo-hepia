import {Component, OnInit} from '@angular/core';
import {CompilationService} from '../compilation.service';
import * as $ from 'jquery';

@Component({
  selector: 'app-compilateur',
  templateUrl: './compilateur.component.html',
  styleUrls: ['./compilateur.component.css']
})
export class CompilateurComponent implements OnInit {

  constructor(private compilation: CompilationService) {
  }

  result = 'no code compiled';

  placeholer = 'Welcome to the compiler, please choose a language to begin !';

  type = 'help';

  display_button = false;

  update(event: any): void {
    this.type = event.target.value;
    this.display_button = true;
    if (this.type === 'python') {
      this.placeholer = 'def avg(marks):\n' +
        '  assert len(marks) != 0\n' +
        '  return sum(marks)/len(marks)\n' +
        '\n' +
        'mark1 = []\n' +
        'print("Average of mark1:",avg(mark1))';
    } else if (this.type === 'java') {
      this.placeholer = 'public class HelloWorld {\n' +
        '\n' +
        '    public static void main(String[] args) {\n' +
        '        // Prints "Hello, World" to the terminal window.\n' +
        '        System.out.println("Hello, World");\n' +
        '    }\n' +
        '\n' +
        '}';
    } else {
      this.display_button = false;
      this.placeholer = 'Welcome to the compiler, please choose a language to begin !';
    }
  }

  compile(stream: string): void {

    const response = $.parseJSON(this.compilation.compile(this.type, stream));
    console.log(response);
    if (response.exit === 0) {
      this.result = response.output + '\nExecuted in : ' + response.time + 'ms';
    } else {
      this.result = response.error;
    }
  }

  ngOnInit() {
  }

}
