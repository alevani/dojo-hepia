import { TestBed } from '@angular/core/testing';

import { FetchProgramService } from './fetch-program.service';

describe('FetchProgramService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: FetchProgramService = TestBed.get(FetchProgramService);
    expect(service).toBeTruthy();
  });
});
