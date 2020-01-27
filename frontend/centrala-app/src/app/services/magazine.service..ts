import { Injectable } from '@angular/core';

import { HttpClient, HttpHeaders } from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class MagazineService {

  constructor(private httpClient: HttpClient) { }


  get(id){
    return this.httpClient.get('/api/magazines/' + id);
  }
  
  getAll(){
    return this.httpClient.get('/api/magazines');
  }

  getForm(){
    return this.httpClient.get('/api/magazines/form');
  }

  getEditorialBoardForm(processInstanceId){
    return this.httpClient.get('/api/magazines/form/editorial-board/' + processInstanceId);
  }

  newMagazine(taskId, magazine){
    return this.httpClient.post('/api/magazines/' + taskId, magazine, {responseType: 'text'});
  }

  saveEditorialBoard(taskId, board){
    return this.httpClient.post('/api/magazines/editorial-board/' + taskId, board, {responseType: 'text'});
  }

  getMagazineCorrectionTasks(){
    return this.httpClient.get('/api/magazines/tasks/magazine-correction');
  }

  getMagazineCorrectionForm(taskId){
    return this.httpClient.get('/api/magazines/form/magazine-correction/' + taskId);
  }

  magazineCorrection(dto, taskId){
    return this.httpClient.put('/api/magazines/magazine-correction/'+ taskId, dto, {responseType: 'text'});
  }

  getAllByChiefEditor(){
    return this.httpClient.get('/api/magazines/get-by-editor');
  }

}
