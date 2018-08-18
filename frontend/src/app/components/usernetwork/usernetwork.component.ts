import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { faEnvelope } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-usernetwork',
  templateUrl: './usernetwork.component.html',
  styleUrls: ['./usernetwork.component.css']
})
export class UsernetworkComponent implements OnInit {

  faEnvelope = faEnvelope;

  userId = localStorage.getItem('userID');
  userToken = localStorage.getItem('userToken');

  profileUserID;

  totalUsers = 0;
  users = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient) { }

  ngOnInit() {
    this.route.params.subscribe((params) => {
      this.profileUserID = +params['user_id'];
      this.totalUsers = 0;
      this.users = [];
      this.getConnectedUsers();
    });

  }

  getConnectedUsers() {
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const profileNetwork = { profileUserID: this.profileUserID.toString() };
    const req = this.http.post('http://localhost:8080/api/getconnectedusers', {
      userIdentifiers,
      profileNetwork
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      const obj = JSON.parse(data);
      this.totalUsers = obj.numberOfResults;
      for (let i = 0; i < this.totalUsers; i++) {
        this.users.push(obj.list[i]);
        this.users[i].image = 'data:image/jpeg;base64,' + this.users[i].image;
      }
      console.log(this.totalUsers);
    },
      (err: HttpErrorResponse) => {
        console.log(err);
      });
  }

}
