import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {ActivatedRoute, Router} from "@angular/router";

@Injectable()
export class IsAuthenticatedService {

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) {}

  authenticationCheck () {
    const usrToken = localStorage.getItem('userToken');
    // console.log('tok: ', usrToken);
    if(usrToken) {
      console.log('tok: ', usrToken);
      const req = this.http.post('http://localhost:8080/api/authcheck', {
        userTok: usrToken
      }, {responseType: 'text', withCredentials: true}).subscribe((data: any) => {
        this.router.navigate(['/home']);

      },
        (err: HttpErrorResponse) => {
          console.log(err);
      });

    } else {
      console.log('no tokens');
    }
  }

}
