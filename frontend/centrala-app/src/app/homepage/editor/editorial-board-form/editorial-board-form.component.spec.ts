import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditorialBoardFormComponent } from './editorial-board-form.component';

describe('EditorialBoardFormComponent', () => {
  let component: EditorialBoardFormComponent;
  let fixture: ComponentFixture<EditorialBoardFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditorialBoardFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditorialBoardFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
