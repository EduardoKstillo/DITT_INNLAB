import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RequestLoanPage } from './request-loan.page';

describe('RequestLoanPage', () => {
  let component: RequestLoanPage;
  let fixture: ComponentFixture<RequestLoanPage>;

  beforeEach(() => {
    fixture = TestBed.createComponent(RequestLoanPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
