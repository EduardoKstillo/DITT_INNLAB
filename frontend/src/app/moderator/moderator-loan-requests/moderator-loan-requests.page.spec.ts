import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ModeratorLoanRequestsPage } from './moderator-loan-requests.page';

describe('ModeratorLoanRequestsPage', () => {
  let component: ModeratorLoanRequestsPage;
  let fixture: ComponentFixture<ModeratorLoanRequestsPage>;

  beforeEach(() => {
    fixture = TestBed.createComponent(ModeratorLoanRequestsPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
