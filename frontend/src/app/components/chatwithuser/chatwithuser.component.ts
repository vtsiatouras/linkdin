import {Component, OnInit, Input} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {environment} from '../../../environments/environment';

@Component({
  selector: 'app-chatwithuser',
  templateUrl: './chatwithuser.component.html',
  styleUrls: ['./chatwithuser.component.css']
})
export class ChatwithuserComponent implements OnInit {

  userId = localStorage.getItem('userID');
  userToken = localStorage.getItem('userToken');

  @Input() userToChat;

  chatID;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient) {
  }

  ngOnInit() {
    this.getChatsIDs();
  }

  getChatsIDs() {
    const userIdentifiers = {userToken: this.userToken, id: this.userId};
    const chat = {userID1: this.userId, userID2: this.userToChat};
    const params = {...userIdentifiers, ...chat}
    const API_URL = environment.API_URL;

    const req = this.http.get(API_URL + '/api/getchatbyusersids', {
      params: params, responseType: 'text', withCredentials: true
    }).subscribe((data: any) => {
        const obj = JSON.parse(data);
        this.chatID = obj.id;
      },
      (err: HttpErrorResponse) => {
        console.log(err);
        this.router.navigate(['/error', false], {skipLocationChange: true});
      });
  }

}
