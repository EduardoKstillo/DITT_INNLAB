import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DeviceUpdatePage } from './device-update.page';

describe('DeviceUpdatePage', () => {
  let component: DeviceUpdatePage;
  let fixture: ComponentFixture<DeviceUpdatePage>;

  beforeEach(() => {
    fixture = TestBed.createComponent(DeviceUpdatePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
