import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {User} from '../../models/user';
import {AuthenticationService} from '../../services/authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent implements OnInit {
  user: User = new User();
  authService: AuthenticationService;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
    ) {

    this.authService = new AuthenticationService(route, router, http);
  }

  ngOnInit() {
    // If there is store a userToken check if there is a valid session
    // And redirect to users home page
    if(localStorage.getItem('userToken')){
      this.router.navigate(['/home']);
    }

    // this.authService.authenticatedRedirect();
  }

  loginUser() {
    // console.log(this.user);

    const email = this.user.email;
    const password = this.user.password;

    // Don't send null values
    if (email == '' || password == '' ) { //TODO mporei na nai peritto auto
      this.router.navigate(['/']);
    } else {
      // Make a post request with users credentials
      // The server will respond with a user token on success
      // and the client will redirect to user's home page
      // If the credentials have errors then the server will send UNAUTHORIZED //TODO HANDLE THIS
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
