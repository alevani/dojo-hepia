import { TestBed } from '@angular/core/testing';

import { CompilationService } from './compilation.service';

describe('CompilationService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CompilationService = TestBed.get(CompilationService);
    expect(service).toBeTruthy();
  });
});
