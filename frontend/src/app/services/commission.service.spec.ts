import { TestBed } from '@angular/core/testing';

import { CommissionService } from './commission.service';

describe('CommissionService', () => {
  let service: CommissionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CommissionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
