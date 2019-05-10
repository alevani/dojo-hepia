import { TestBed } from '@angular/core/testing';

import { FetchKataService } from './fetch-kata.service';

describe('FetchKataService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: FetchKataService = TestBed.get(FetchKataService);
    expect(service).toBeTruthy();
  });
});
