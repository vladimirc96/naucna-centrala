import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RepositoryService {

  categories = [];
  languages = [];
  books = [];

  constructor(private httpClient: HttpClient) { 
  }

  startProcess(){
    return this.httpClient.get('/api/users/form');
  }

  getTasksReviewer(){
    return this.httpClient.get('/api/admin/get/tasks/reviewer');
  }

  claimTask(taskId){
    return this.httpClient.post('/api/repository/tasks/claim/' + taskId, null);
  }

  getForm(taskId){
    return this.httpClient.get('/api/repository/get/form/' + taskId);
  }

  getCheckMagazineDataForm(taskId){
    return this.httpClient.get('/api/repository/form/check-magazine-data/' + taskId);
  }
  
}