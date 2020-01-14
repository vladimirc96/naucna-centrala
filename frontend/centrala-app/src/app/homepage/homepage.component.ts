import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent implements OnInit {

  constructor(private authService: AuthService,private userService: UserService) { }

  isAdmin: boolean = false;
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
