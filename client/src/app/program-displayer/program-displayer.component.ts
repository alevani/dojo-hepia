import {Component, OnInit} from '@angular/core';
import {FetchProgramService} from '../fetch-program.service';
import {Program} from './program';

@Component({
  selector: 'app-program-displayer',
  templateUrl: './program-displayer.component.html',
  styleUrls: ['./program-displayer.component.css']
})
export class ProgramDisplayerComponent implements OnInit {


  programs: Program[];

  constructor(private fetchProgramService: FetchProgramService) {
  }

  getProgram(sensei: string): void {
    this.programs = this.fetchProgramService.getPrograms(sensei);
  }

  ngOnInit() {
    this.getProgram('Orestis Pileas Malaspinas');
  }

}
