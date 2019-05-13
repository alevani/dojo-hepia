import { TestBed } from '@angular/core/testing';

import { GetKataSubscriptionService } from './get-kata-subscription.service';

describe('GetKataSubscriptionService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: GetKataSubscriptionService = TestBed.get(GetKataSubscriptionService);
    expect(service).toBeTruthy();
  });
});
