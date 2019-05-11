import { TestBed } from '@angular/core/testing';

import { CreateSubscriptionService } from './create-subscription.service';

describe('CreateSubscriptionService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CreateSubscriptionService = TestBed.get(CreateSubscriptionService);
    expect(service).toBeTruthy();
  });
});
