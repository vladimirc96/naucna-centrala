import { Injectable } from '@angular/core';

import { HttpClient, HttpHeaders } from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class MagazineService {

  constructor(private httpClient: HttpClient) { }

  getForm(){
    return this.httpClient.get('/api/magazines/form');
  }

  getEditorialBoardForm(processInstanceId){
    return this.httpClient.get('/api/magazines/editorial-board-form/' + processInstanceId);
  }

  newMagazine(taskId, magazine){
    return this.httpClient.post('/api/magazines/' + taskId, magazine, {responseType: 'text'});
  }

  saveEditorialBoard(taskId, board){
    return this.httpClient.post('/api/magazines/editorial-board/' + taskId, board, {responseType: 'text'});
  }

}
