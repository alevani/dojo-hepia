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

  type = 'help';

  assert = '';

  display_button = false;

  help = 'plain_text';

  status = 2;

  code = '';

  update(event: any): void {
    this.type = event.target.value;
    this.display_button = true;
    if (this.type === 'python') {

      this.code = 'def easyline(n):';
      this.help = 'Le but ici est de jouer avec les tableaux en python. dans cet exercice, il faut multiplier tous les éléments d\'un tableau par 2.\neasyline([2,4]) donnera par exemple [4,8]';
      this.assert = 'from assertpy import assert_that\n' +
        'import sample as m\n' +
        '\n' +
        'assert_that(m.easyline([1,2])).is_equal_to([2,4])\n' +
        'assert_that(m.easyline([45,53,12])).is_equal_to([90,106,24])\n' +
        'assert_that(m.easyline([3])).is_equal_to([6])';
    } else if (this.type === 'java') {
      this.code = 'public class kata {\n' +
        '\n' +
        '    public static int[] bytwo(int[] input) {\n' +
        '        int[] output = new int[input.length];\n' +
        '\n' +
        '        for (int i = 0; i < input.length; i++)\n' +
        '            output[i] = input[i] * 2;\n' +
        '        return output;\n' +
        '    }\n' +
        '\n' +
        '}\n';
      this.help = 'Le but ici est de jouer avec les tableaux en java. dans cet exercice, il faut multiplier tous les éléments d\'un tableau par 2.\nbytwo([2,4]) donnera par exemple [4,8]';
      this.assert = 'import static org.junit.Assert.*;\n' +
        '\n' +
        'public class Main {\n' +
        '\n' +
        '    public static void main(String[] args) {\n' +
        '        assertArrayEquals(kata.bytwo(new int[]{2, 3, 5}), new int[]{4, 6, 10});\n' +
        '        assertArrayEquals(kata.bytwo(new int[]{12, 34, 52}), new int[]{24, 68, 104});\n' +
        '    }\n' +
        '}';
    } else {
      this.display_button = false;
    }
  }

  OnNewEvent(event: any): void {
    this.code = event.toString();
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
