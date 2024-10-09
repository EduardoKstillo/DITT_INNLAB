import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AllProjectsPage } from './all-projects.page';

describe('AllProjectsPage', () => {
  let component: AllProjectsPage;
  let fixture: ComponentFixture<AllProjectsPage>;

  beforeEach(() => {
    fixture = TestBed.createComponent(AllProjectsPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
