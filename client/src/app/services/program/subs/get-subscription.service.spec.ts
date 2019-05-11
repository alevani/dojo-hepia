import { TestBed } from '@angular/core/testing';

import { GetSubscriptionService } from './get-subscription.service';

describe('GetSubscriptionService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: GetSubscriptionService = TestBed.get(GetSubscriptionService);
    expect(service).toBeTruthy();
  });
});
