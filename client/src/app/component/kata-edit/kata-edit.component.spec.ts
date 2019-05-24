import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { KataEditComponent } from './kata-edit.component';

describe('KataEditComponent', () => {
  let component: KataEditComponent;
  let fixture: ComponentFixture<KataEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ KataEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(KataEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
