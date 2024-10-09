import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LoanRequestDetailsPage } from './loan-request-details.page';

describe('LoanRequestDetailsPage', () => {
  let component: LoanRequestDetailsPage;
  let fixture: ComponentFixture<LoanRequestDetailsPage>;

  beforeEach(() => {
    fixture = TestBed.createComponent(LoanRequestDetailsPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
