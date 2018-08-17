import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';

import { faBell } from '@fortawesome/free-solid-svg-icons';
import { faCheck } from '@fortawesome/free-solid-svg-icons';
import { faTimes } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css']
})
export class NotificationsComponent implements OnInit {

  userId = localStorage.getItem('userID');
  userToken = localStorage.getItem('userToken');

  faBell = faBell;
  faCheck = faCheck;
  faTimes = faTimes;

  pendingRequests = [];
  totalPendingRequests = 0;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) { }

  ngOnInit() {
  }

  getPendingConnectRequests() {
    this.pendingRequests = [];
    this.totalPendingRequests = 0;
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const req = this.http.post('http://localhost:8080/api/getconnectrequests', {
      userIdentifiers,
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      const obj = JSON.parse(data);
      console.log(obj.numberOfResults);
      this.totalPendingRequests = obj.numberOfResults;
      if (this.totalPendingRequests > 0) {
        for (let i = 0; i < this.totalPendingRequests; i++) {
          this.pendingRequests.push(obj.list[i]);
          this.pendingRequests[i].image = 'data:image/jpeg;base64,' + this.pendingRequests[i].image;
        }
      }
      console.log(this.pendingRequests);
    },
      (err: HttpErrorResponse) => {
        console.log(err);
      });
  }

  acceptConnectRequest(index) {
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const connectRequest = { profileUserID: this.pendingRequests[index].id, accepted: '1' };
    const req = this.http.post('http://localhost:8080/api/handleconnectrequest', {
      userIdentifiers,
      connectRequest
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
    },
      (err: HttpErrorResponse) => {
        console.log(err);
      });
  }

  declineConnectRequest(index) {
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const connectRequest = { profileUserID: this.pendingRequests[index].id, accepted: '0' };
    const req = this.http.post('http://localhost:8080/api/handleconnectrequest', {
      userIdentifiers,
      connectRequest
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
    },
      (err: HttpErrorResponse) => {
        console.log(err);
      });
  }

}
