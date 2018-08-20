import { Component, OnInit, Input } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { environment } from '../../../environments/environment';

import { faStar } from '@fortawesome/free-solid-svg-icons';
import { faCommentAlt } from '@fortawesome/free-solid-svg-icons';
import { faBriefcase } from '@fortawesome/free-solid-svg-icons';
import { faGlobeAfrica } from '@fortawesome/free-solid-svg-icons';
import { faUsers } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css'],
})
export class PostComponent implements OnInit {

  faBriefcase = faBriefcase;
  faGlobeAfrica = faGlobeAfrica;
  faUsers = faUsers;
  faStar = faStar;
  faCommentAlt = faCommentAlt;

  userId = localStorage.getItem('userID');
  userToken = localStorage.getItem('userToken');

  userName: string;
  userSurname: string;
  userImage: string;
  @Input() postId: string;
  @Input() userIdPost: string;
  @Input() date: Date;
  @Input() isAd: any;
  @Input() isPublic: any;
  @Input() content: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) { }

  ngOnInit() {
    // this.getUserIdentifiers();
    console.log(this.userIdPost);
    console.log(this.date);
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const userInfoRequest = { userIdPost: this.userIdPost };
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/getuserbasicinfo', {
      userIdentifiers,
      userInfoRequest
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      const obj = JSON.parse(data);
      this.userName = obj.name;
      this.userSurname = obj.surname;
      this.userImage = 'data:image/jpeg;base64,' + obj.image;
    },
      (err: HttpErrorResponse) => {
        console.log(err);
      });
  }

  getUserIdentifiers() {
    console.log(this.userIdPost);
    console.log(this.date);
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const userInfoRequest = { userIdPost: this.userIdPost };
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/getuserbasicinfo', {
      userIdentifiers,
      userInfoRequest
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      const obj = JSON.parse(data);
      this.userName = obj.name;
      this.userSurname = obj.surname;
      this.userImage = 'data:image/jpeg;base64,' + obj.image;
    },
      (err: HttpErrorResponse) => {
        console.log(err);
      });
  }

}
