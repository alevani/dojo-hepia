import { TestBed } from '@angular/core/testing';

import { KataSubscriptionService } from './kata-subscription.service';

describe('KataSubscriptionService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: KataSubscriptionService = TestBed.get(KataSubscriptionService);
    expect(service).toBeTruthy();
  });
});
