import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {FetchProgramByTypeService} from '../../services/program/search/fetch-program-by-type.service';
import {Program} from '../program-displayer/program';

@Component({
  selector: 'app-searchby',
  templateUrl: './searchby.component.html',
  styleUrls: ['./searchby.component.css']
})
export class SearchbyComponent implements OnInit {

  constructor(private formBuilder: FormBuilder, private searchProgramService: FetchProgramByTypeService) {
    this.SearchForm = this.formBuilder.group({
      search: ['', Validators.required],
    });
  }

  SearchForm: FormGroup;

  loading = false;
  submitted = false;

  // @ts-ignore
  programs: Program[];

  error = '';

  onSubmit() {
    this.submitted = true;

    // stop here if form is invalid
    if (this.SearchForm.invalid) {
      return;
    }

    this.searchProgramService.getPrograms('title', this.f.search.value).subscribe((data: Program[]) => {
      this.programs = data;
      this.error = '';
    }, (error1: any) => {
      this.programs = [];
      this.error = error1.error;
    });
  }

  get f() {
    return this.SearchForm.controls;
  }

  ngOnInit() {

  }

}
