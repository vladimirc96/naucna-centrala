import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewerFormComponent } from './reviewer-form.component';

describe('ReviewerFormComponent', () => {
  let component: ReviewerFormComponent;
  let fixture: ComponentFixture<ReviewerFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReviewerFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewerFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
