import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DeviceCreatePage } from './device-create.page';

describe('DeviceCreatePage', () => {
  let component: DeviceCreatePage;
  let fixture: ComponentFixture<DeviceCreatePage>;

  beforeEach(() => {
    fixture = TestBed.createComponent(DeviceCreatePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
