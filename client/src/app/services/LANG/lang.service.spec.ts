import { TestBed } from '@angular/core/testing';

import { LANGService } from './lang.service';

describe('LANGService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: LANGService = TestBed.get(LANGService);
    expect(service).toBeTruthy();
  });
});
