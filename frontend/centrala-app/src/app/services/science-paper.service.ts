import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class SciencePaperService {

  constructor(private httpClient: HttpClient) { }

  startProcess(){
    return this.httpClient.get('/api/science-paper/form');
  }

  selectMagazine(taskId, dto){
    return this.httpClient.post('/api/science-paper/select-magazine/'.concat(taskId), dto);
  }

  save(taskId, dto){
    return this.httpClient.post('/api/science-paper/'.concat(taskId), dto, {responseType: 'text'});
  }

  savePdf(sciencePaperId, fileToUpload){
    const formData: FormData = new FormData();  
    formData.append("file", fileToUpload);
    return this.httpClient.put('/api/science-paper/'.concat(sciencePaperId), formData, {responseType: 'text'});
  }

  paperReview(taskId, dto){
    return this.httpClient.put('/api/science-paper/paper-review/'.concat(taskId), dto, {responseType: 'text'});
  }

  download(processId){
    const httpOptions = {
      'responseType'  : 'arraybuffer' as 'json'
    };
    return this.httpClient.get('/api/science-paper/download/'.concat(processId), httpOptions);
  }

  paperFormat(taskId, dto){
    return this.httpClient.put('/api/science-paper/paper-format/'.concat(taskId), dto, {responseType: 'text'});
  }

  paperCorrection(taskId, dto){
    return this.httpClient.put('/api/science-paper/paper-correction/'.concat(taskId), dto, {responseType: 'text'});
  }

  chiefEditorReview(taskId, dto){
    return this.httpClient.put('/api/science-paper/chief-editor-review/'.concat(taskId), dto, {responseType: 'text'});
  }

  chiefEditorChoice(taskId, dto){
    return this.httpClient.put('/api/science-paper/chief-editor-choice/'.concat(taskId), dto, {responseType: 'text'});
  }

  chooseReviewers(taskId, dto){
    return this.httpClient.put('/api/science-paper/choose-reviewers/'.concat(taskId), dto, {responseType: 'text'});
  }

}
