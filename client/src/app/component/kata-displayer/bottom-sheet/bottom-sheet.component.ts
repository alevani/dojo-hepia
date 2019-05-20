import {Component, Inject, OnInit} from '@angular/core';
import {MAT_BOTTOM_SHEET_DATA, MatBottomSheetRef} from '@angular/material';

export interface BottomsheetKata {
  title: string;
  id: string;
}

@Component({
  selector: 'app-bottom-sheet',
  templateUrl: './bottom-sheet.component.html',
  styleUrls: ['./bottom-sheet.component.css']
})
export class BottomSheetComponent implements OnInit {

  constructor(private bottomSheetRef: MatBottomSheetRef<BottomSheetComponent>, @Inject(MAT_BOTTOM_SHEET_DATA) public data: BottomsheetKata) {
  }

  deactivate(): void {
    console.log(this.data.id);
    alert('deactivate touched');
    this.bottomSheetRef.dismiss();
  }

  delete(): void {
    alert('delete touched');
    this.bottomSheetRef.dismiss();
  }

  ngOnInit() {
  }

}
