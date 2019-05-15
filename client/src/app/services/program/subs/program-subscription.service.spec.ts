import { TestBed } from '@angular/core/testing';

import { ProgramSubscriptionService } from './program-subscription.service';

describe('ProgramSubscriptionService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ProgramSubscriptionService = TestBed.get(ProgramSubscriptionService);
    expect(service).toBeTruthy();
  });
});
