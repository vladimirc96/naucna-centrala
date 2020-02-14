import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  constructor(private httpClient: HttpClient) { }

  initMagazineOrder(magazineDTO){
    return this.httpClient.post('/api/orders/magazine/init', magazineDTO);
  }

  initMagazineSubscription(magazineDTO) {
    return this.httpClient.post('/api/orders/magazine/initSub', magazineDTO);
  }

  initPaperOrder(paper) {
    return this.httpClient.post('/api/orders/scPaper/init', paper);
  }

  getAllOrders() {
    return this.httpClient.get('/api/orders/');
  }

  getAllOrdersByUsername(){
    return this.httpClient.get('/api/orders/all-by-user');
  }

}
