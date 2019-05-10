import { TestBed } from '@angular/core/testing';

import { CreateKataService } from './create-kata.service';

describe('CreateKataService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CreateKataService = TestBed.get(CreateKataService);
    expect(service).toBeTruthy();
  });
});
