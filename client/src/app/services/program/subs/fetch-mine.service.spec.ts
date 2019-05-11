import { TestBed } from '@angular/core/testing';

import { FetchMineService } from './fetch-mine.service';

describe('FetchMineService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: FetchMineService = TestBed.get(FetchMineService);
    expect(service).toBeTruthy();
  });
});
