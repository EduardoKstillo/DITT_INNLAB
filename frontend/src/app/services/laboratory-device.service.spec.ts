import { TestBed } from '@angular/core/testing';

import { LaboratoryDeviceService } from './laboratory-device.service';

describe('LaboratoryDeviceService', () => {
  let service: LaboratoryDeviceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LaboratoryDeviceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
