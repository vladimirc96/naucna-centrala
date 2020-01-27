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

}
