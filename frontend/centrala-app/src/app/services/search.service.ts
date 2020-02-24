import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  constructor(private httpClient: HttpClient) { }


  simpleQuery(dto){
    return this.httpClient.post('/api/search/match', dto);
  }

  boolQuery(dto){
    return this.httpClient.post('/api/search/boolean', dto);
  }

  geoDistanceQuery(taskId){
    return this.httpClient.get('/api/search/distance/'.concat(taskId));
  }

  moreLikeThisQuery(taskId){
    return this.httpClient.get('/api/search/more-like-this/'.concat(taskId));
  }

}
