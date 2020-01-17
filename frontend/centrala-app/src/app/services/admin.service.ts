import { Injectable } from '@angular/core';

import { HttpClient, HttpHeaders } from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(private httpClient: HttpClient) { }

  getForm(taskId){
    return this.httpClient.get('/api/admin/reviewer/form/' + taskId);
  }

  getTasksReviewer(){
    return this.httpClient.get('/api/admin/tasks/reviewer');
  }


  setReviewer(dto, taskId){
    return this.httpClient.put('/api/admin/set-reviewer/' + taskId, dto, {responseType: 'text'});
  }

  getTasksCheckMagazineData(){
    return this.httpClient.get('/api/admin/tasks/check-magazine-data');
  }

  getCheckMagazineDataForm(taskId){
    return this.httpClient.get('/api/admin/form/check-magazine-data/' + taskId);
  }

  checkMagazineDataSubmit(data, taskId){
    return this.httpClient.post('/api/admin/check-magazine-data/' + taskId, data, {responseType: 'text'});
  }
}
