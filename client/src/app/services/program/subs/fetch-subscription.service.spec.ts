import { TestBed } from '@angular/core/testing';

import { FetchSubscriptionService } from './fetch-subscription.service';

describe('FetchSubscriptionService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: FetchSubscriptionService = TestBed.get(FetchSubscriptionService);
    expect(service).toBeTruthy();
  });
});
