import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/mapTo';
import 'rxjs/add/operator/catch';

@Injectable()
export class AuthenticationService {

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) {}

  isAuthenticated(): Observable<boolean> {
    const userToken = localStorage.getItem('userToken');
    // If there is a user token
    if (userToken) {
      // Check the server if the session belongs to online user
      // If not the server will send UNAUTHORIZED
      return this.http.post('http://localhost:8080/api/authcheck', {
          userToken: userToken
        }, {responseType: 'text', withCredentials: true}).mapTo(true)
        .catch(err => (Observable.of(false)));
    // Else cannot be exist a valid session
    } else {
      return Observable.of(false);
    }
  }

  // TODO to parakatw mallon den xreiazetai (kata 99%)
  authenticatedRedirect () {
    const usrToken = localStorage.getItem('userToken');
    if (usrToken) {
      console.log('tok: ', usrToken);
      const req = this.http.post('http://localhost:8080/api/authcheck', {
        userTok: usrToken
      }, {responseType: 'text', withCredentials: true}).subscribe(
        (response) => { this.router.navigate(['/home']); },
        (err) => { console.log(err); }
      );
    } else {
      console.log('no tokens');
    }
  }

}
