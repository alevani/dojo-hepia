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

  assert = 'Assert that will be tested with the chosen language';

  display_button = false;

  help = '';

  status = 2;
  error = {
    'background-color': 'blue',
  }


  update(event: any): void {
    this.type = event.target.value;
    this.display_button = true;
    if (this.type === 'python') {

      this.placeholer = 'def easyline(n):';
      this.help = 'Le but ici est de jouer avec les tableaux en python. dans cet exercice, il faut multiplier tous les éléments d\'un tableau par 2.\neasyline([2,4]) donnera par exemple [4,8]';
      this.assert = 'from assertpy import assert_that\n' +
        'import sample as m\n' +
        '\n' +
        'assert_that(m.easyline([1,2])).is_equal_to([2,4])\n' +
        'assert_that(m.easyline([45,53,12])).is_equal_to([90,106,24])\n' +
        'assert_that(m.easyline([3])).is_equal_to([6])';
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

  compile(stream: string, assert: string): void {

    const response = $.parseJSON(this.compilation.compile(this.type, stream, assert));

    if (response.exit === 0) {
      this.status = 0;
      this.result = response.output + '\nExercise passed';
    } else {
      this.status = 1;
      this.result = response.error;
    }

    this.result += '\nExecuted in : ' + response.time + 'ms';
  }

  ngOnInit() {
  }

}
