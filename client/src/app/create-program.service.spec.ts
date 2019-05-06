import { TestBed } from '@angular/core/testing';

import { CreateProgramService } from './create-program.service';

describe('CreateProgramService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CreateProgramService = TestBed.get(CreateProgramService);
    expect(service).toBeTruthy();
  });
});
