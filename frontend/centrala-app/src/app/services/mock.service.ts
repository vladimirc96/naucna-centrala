import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class MockService {

  constructor(private httpClient: HttpClient) { }


  startPaymentProcess(processId){
    return this.httpClient.get('/api/mocks/payment/'.concat(processId));
  }

  payment(taskId, dto){
    return this.httpClient.post('/api/mocks/'.concat(taskId), dto, {responseType: 'text'});
  }

}
