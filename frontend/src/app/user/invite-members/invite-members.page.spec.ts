import { ComponentFixture, TestBed } from '@angular/core/testing';
import { InviteMembersPage } from './invite-members.page';

describe('InviteMembersPage', () => {
  let component: InviteMembersPage;
  let fixture: ComponentFixture<InviteMembersPage>;

  beforeEach(() => {
    fixture = TestBed.createComponent(InviteMembersPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
