import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-exo-python-sample',
  templateUrl: './exo-python-sample.component.html',
  styleUrls: ['./exo-python-sample.component.css']
})
export class ExoPythonSampleComponent implements OnInit {

  constructor() {
  }

  exoTitle = 'python';
  exoSenseiName = 'Orestis Pileas Malaspinas';
  exoDescription = 'blahbalh';

  kataPython: Kata = {exoTitle,this.exoSenseName,exoDescription};

  ngOnInit() {
  }

}

export class Kata {
  private exoTitle: string;
  private exoSenseName: string;
  private exoDescription: string;

  constructor(title: string, senseiName: string, desc: string) {
    this.exoTitle = title;
    this.exoSenseName = senseiName;
    this.exoDescription = desc;
  }
}
