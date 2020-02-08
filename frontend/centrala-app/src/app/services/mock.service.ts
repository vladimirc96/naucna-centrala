import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class MockService {

  constructor(private httpClient: HttpClient) { }


  startPaymentProcess(){
    return this.httpClient.get('/api/mocks/payment');
  }

  payment(taskId, dto){
    return this.httpClient.post('/api/mocks/'.concat(taskId), dto, {responseType: 'text'});
  }

}
