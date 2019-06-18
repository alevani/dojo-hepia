import {Component, Input, OnInit} from '@angular/core';
import {Program} from '../program';
import {FetchProgramByTypeService} from '../../../services/program/search/fetch-program-by-type.service';

@Component({
  selector: 'app-card-displayer',
  templateUrl: './card-displayer.component.html',
  styleUrls: ['./card-displayer.component.css']
})
export class CardDisplayerComponent implements OnInit {

  constructor(private FetchProgramByQueryService: FetchProgramByTypeService) {
  }

  @Input() programs: any;
  filter = false;
  filterValue = '';
  filterType = '';

  querySearch(type: string, res: string) {
    this.FetchProgramByQueryService.getPrograms(type, res).subscribe((data: Program[]) => {
      this.filterType = type;
      this.filterValue = res;
      this.filter = true;
      this.programs = data;
    });
  }

  ngOnInit() {
  }

}
