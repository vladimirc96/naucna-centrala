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
  isReviewer: boolean = false;
  isEditor: boolean = false;
  isLoggedIn: boolean = false;

  ngOnInit() {
    let user = this.authService.getLoggedUser();
    if(user != null){
      this.isLoggedIn = true; 
    } 

    this.userService.getUser().subscribe(
      (user: any) => {
        if(user.role === 'ADMIN'){
          this.isAdmin = true;
        }else if(user.role === 'REVIEWER'){
          this.isReviewer = true;
        }else if(user.role === 'EDITOR'){
          this.isEditor = true;
        }
      }
    )

  }

  logOut(){
    this.authService.logout();
    this.isLoggedIn = false;
    this.isAdmin = false;
    this.isEditor = false;
    this.isReviewer = false;
  }

}
