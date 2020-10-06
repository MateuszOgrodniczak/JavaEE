import { TestBed } from '@angular/core/testing';

import { SharedFlagsService } from './shared-flags.service';

describe('SharedFlagsService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: SharedFlagsService = TestBed.get(SharedFlagsService);
    expect(service).toBeTruthy();
  });
});
