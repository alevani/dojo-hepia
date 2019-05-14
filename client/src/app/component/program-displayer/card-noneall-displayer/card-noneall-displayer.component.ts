import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-card-noneall-displayer',
  templateUrl: './card-noneall-displayer.component.html',
  styleUrls: ['./card-noneall-displayer.component.css']
})
export class CardNoneallDisplayerComponent implements OnInit {

  constructor() {
  }

  @Input() programs;
  filter = false;
  filterValue: string;
  filterType: string;

  programNonFiltered: object;

  querySearch(type: string, res: string) {

    this.programs = this.programNonFiltered;
    this.filterType = type;
    this.filterValue = res;
    this.filter = true;
    this.programs = this.programs.filter((x) => {
      return x[type] === res;
    });
  }

  ngOnInit() {
    this.programNonFiltered = this.programs;

  }

}
