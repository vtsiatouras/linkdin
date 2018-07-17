import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {AuthenticationService} from '../../services/authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent implements OnInit {

  email: string;
  password: string;
  authService: AuthenticationService;

  hasError = false;
  alerts: string;
  type: 'danger';

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
    if (localStorage.getItem('userToken')) {
      this.router.navigate(['/home']);
    }

    // this.authService.authenticatedRedirect();
  }

  loginUser() {
    // Re initialize error flag for the alert pop up
    this.hasError = false;
    // Don't send null values
    if (this.email === '' || this.password === '' ) { // TODO mporei na nai peritto auto
      this.router.navigate(['/']);
    } else {
      // Make a post request with users credentials
      // The server will respond with a user token on success
      // and the client will redirect to user's home page
      // If the credentials have errors then the server will send UNAUTHORIZED //TODO HANDLE THIS
      const req = this.http.post('http://localhost:8080/api/login', {
        email:  this.email,
        password: this.password
      }, {responseType: 'text', withCredentials: true}).subscribe((data: any) => {
        const obj = JSON.parse(data);
        localStorage.setItem('userToken', obj.userToken);
        localStorage.setItem('firstName', obj.firstName);
        localStorage.setItem('lastName', obj.lastName);
        localStorage.setItem('email', obj.email);
        this.router.navigate(['/home']);
        },
        (err: HttpErrorResponse) => {
          this.showAlert(err.error);
          console.log(err);
      });
    }
  }

  showAlert(errorMessage) {
    console.log(errorMessage);
    if(errorMessage === 'BAD_CREDENTIALS') {
      this.hasError = true;
      this.alerts = 'Wrong Email or Password';
    }
  }

}
