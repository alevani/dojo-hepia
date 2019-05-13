import { TestBed } from '@angular/core/testing';

import { KataService } from './kata.service';

describe('KataService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: KataService = TestBed.get(KataService);
    expect(service).toBeTruthy();
  });
});
