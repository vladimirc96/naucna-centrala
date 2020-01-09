import { Injectable } from '@angular/core';

import { HttpClient, HttpHeaders } from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient: HttpClient) { }

  registerUser(user, taskId) {
    return this.httpClient.post("http://localhost:8440/users/register/" + taskId, user);
  }
}
