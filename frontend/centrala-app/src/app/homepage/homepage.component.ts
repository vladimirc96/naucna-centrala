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
  isAuthor: boolean = false;
  isCustomer: boolean = false;
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
          console.log("ROLA: ADMIN");
        }else if(user.role === 'REVIEWER'){
          this.isReviewer = true;
          console.log("ROLA: RECENZENT");
        }else if(user.role === 'EDITOR'){
          this.isEditor = true;
          console.log("ROLA: UREDNIK");
        }else if(user.role == 'AUTHOR'){
          this.isAuthor = true;
          console.log("ROLA: AUTOR");
        }else if(user.role == 'CUSTOMER'){
          this.isCustomer = true;
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
    this.isAuthor = false;
    this.isCustomer = false;
  }

}
