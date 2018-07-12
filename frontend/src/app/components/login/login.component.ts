import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {User} from '../../models/user';
import {FormControl} from "@angular/forms";
import {IsAuthenticatedService} from "../../services/isauthenticated.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent implements OnInit {
  user: User = new User();
  loginForm: FormControl = new FormControl();

  authService: IsAuthenticatedService;


  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
    ) {

    this.authService = new IsAuthenticatedService(route, router, http);
  }

  ngOnInit() {
    this.authService.authenticationCheck();
  }

  loginUser() {
    // console.log(this.user);

    const email = this.user.email;
    const password = this.user.password;

    if (email == '' || password == '' ) { // Don't send null values
      this.router.navigate(['/']);
    } else {
      const req = this.http.post('http://localhost:8080/api/login', {
        email: email,
        password: password
      }, {responseType: 'text', withCredentials: true}).subscribe((data: any) => {
        localStorage.setItem('userToken', data);
        this.router.navigate(['/home']);
        },
        (err: HttpErrorResponse) => {
        console.log(err);
      });
    }
  }

}
