import {Component, OnInit} from '@angular/core';
import {FetchProgramService} from '../fetch-program.service';
import {Program} from './program';

@Component({
  selector: 'app-program-displayer',
  templateUrl: './program-displayer.component.html',
  styleUrls: ['./program-displayer.component.scss']
})
export class ProgramDisplayerComponent implements OnInit {


  programs: Program[];

  constructor(private fetchProgramService: FetchProgramService) {
  }

  getProgram(): void {
    this.fetchProgramService.getPrograms().subscribe((data: Program[]) => this.programs = data);
  }

  ngOnInit() {
    this.getProgram();
  }

}
