import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import 'rxjs/add/operator/map';
import {User} from '../models/user';


@Injectable()
export class UserService {
  readonly rootUrl = 'http://localhost:8080/api';
  constructor(private http: HttpClient) {}

  // registerUser(user: User) {
  //   const body: User = {
  //     UserName: user.UserName,
  //     Password: user.Password,
  //     Email: user.Email,
  //     FirstName: user.FirstName,
  //     LastName: user.LastName
  //   }
  //   var reqHeader = new HttpHeaders({'No-Auth':'True'});
  //   return this.http.post(this.rootUrl + '/api/User/Register', body,{headers : reqHeader});
  // }

  userAuthentication(userName, password) {

    const data = 'username=' + userName + '&password=' + password + '&grant_type=password';
    const reqHeader = new HttpHeaders({ 'Content-Type': 'application/x-www-urlencoded', 'No-Auth': 'True' });
    console.log(data);
    return this.http.post(this.rootUrl + '/login', data, { headers: reqHeader });
  }

}
