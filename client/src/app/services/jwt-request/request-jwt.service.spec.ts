import { TestBed } from '@angular/core/testing';

import { RequestJwtService } from './request-jwt.service';

describe('RequestJwtService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: RequestJwtService = TestBed.get(RequestJwtService);
    expect(service).toBeTruthy();
  });
});
