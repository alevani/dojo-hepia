import { TestBed } from '@angular/core/testing';

import { FetchProgramIdService } from './fetch-program-id.service';

describe('FetchProgramIdService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: FetchProgramIdService = TestBed.get(FetchProgramIdService);
    expect(service).toBeTruthy();
  });
});
