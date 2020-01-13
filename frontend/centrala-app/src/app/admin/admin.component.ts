import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {

  constructor(private authService: AuthService,private userService: UserService) { }

  isLoggedIn: boolean = false;

  ngOnInit() {

    let user = this.authService.getLoggedUser();
    if(user != null){
      this.isLoggedIn = true; 
    } 

  }

  logOut(){
    this.authService.logout();
    this.isLoggedIn = false;
  }

}
