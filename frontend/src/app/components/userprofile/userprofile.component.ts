import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-userprofile',
  templateUrl: './userprofile.component.html',
  styleUrls: ['./userprofile.component.css']
})
export class UserprofileComponent implements OnInit {

  href = '';

  // Retrieve user's data from local storage
  email = localStorage.getItem('email');
  userToken = localStorage.getItem('userToken');
  firstName = localStorage.getItem('firstName');
  lastName = localStorage.getItem('lastName');
  userID = localStorage.getItem('userID');

  // Variables for requested user's profile
  profileFirstName: string;
  profileSurname: string;
  profilePhoneNumber: string;
  profileImage: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) { }

  ngOnInit() {

    this.href = this.router.url;
    const url = this.href.split('/');
    const urlUserID = url[2];
    console.log(urlUserID);

    const userAttrs = { userToken: this.userToken, email: this.email };
    const requestProfile = { userID: this.userID, profileUserID: urlUserID };

    const req = this.http.post('http://localhost:8080/api/user', {
      userAttrs,
      requestProfile
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      const obj = JSON.parse(data);
      this.profileImage = 'data:image/jpeg;base64,' + obj.profileImage;
      this.profileFirstName = obj.user.firstName;
      console.log(obj);
    },
      (err: HttpErrorResponse) => {
        console.log(err);
      });
  }
}
