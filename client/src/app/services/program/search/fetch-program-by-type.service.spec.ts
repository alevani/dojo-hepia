import { TestBed } from '@angular/core/testing';

import { FetchProgramByTypeService } from './fetch-program-by-type.service';

describe('FetchProgramByTypeService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: FetchProgramByTypeService = TestBed.get(FetchProgramByTypeService);
    expect(service).toBeTruthy();
  });
});
