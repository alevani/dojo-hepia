import { TestBed } from '@angular/core/testing';

import { ToggleSubscriptionService } from './toggle-subscription.service';

describe('ToggleSubscriptionService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ToggleSubscriptionService = TestBed.get(ToggleSubscriptionService);
    expect(service).toBeTruthy();
  });
});
