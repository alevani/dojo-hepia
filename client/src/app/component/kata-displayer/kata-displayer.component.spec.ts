import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { KataDisplayerComponent } from './kata-displayer.component';

describe('KataDisplayerComponent', () => {
  let component: KataDisplayerComponent;
  let fixture: ComponentFixture<KataDisplayerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ KataDisplayerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(KataDisplayerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
