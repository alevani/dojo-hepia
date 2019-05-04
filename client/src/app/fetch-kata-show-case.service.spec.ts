import { TestBed } from '@angular/core/testing';

import { FetchKataShowCaseService } from './fetch-kata-show-case.service';

describe('FetchKataShowCaseService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: FetchKataShowCaseService = TestBed.get(FetchKataShowCaseService);
    expect(service).toBeTruthy();
  });
});
