import { Injectable } from '@angular/core';

import { HttpClient, HttpHeaders } from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(private httpClient: HttpClient) { }

  claimTask(taskId){
    return this.httpClient.post('/api/admin/tasks/claim/' + taskId, null);
  }

  getForm(taskId){
    return this.httpClient.get('/api/admin/reviewer/form/' + taskId);
  }

  setReviewer(dto, taskId){
    return this.httpClient.put('/api/admin/set-reviewer/' + taskId, dto, {responseType: 'text'});
  }

}
