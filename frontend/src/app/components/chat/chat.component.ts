import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {

  chatID;

  userId = localStorage.getItem('userID');
  userToken = localStorage.getItem('userToken');

  showConversation = false;

  activeChats = [];
  userIDsChats = [];
  chatIDs = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient) {
  }

  ngOnInit() {
    this.route.params.subscribe((params) => {
      this.chatID = +params['chat_id'];
      this.getActiveChats();
      console.log(this.chatID);
      if (this.chatID) {
        this.loadChatRoom();
      }
    });
  }

  getActiveChats() {
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/getactivechats', {
      userIdentifiers
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      const obj = JSON.parse(data);
      this.activeChats = obj;
      for (let i = 0; i < obj.length; i++) {
        if (obj[i].user1 == this.userId) {
          this.userIDsChats.push(obj[i].user2);
          this.chatIDs.push(obj[i].id);
        } else {
          this.userIDsChats.push(obj[i].user1);
          this.chatIDs.push(obj[i].id);
        }
      }
      console.log(this.activeChats);
      console.log(this.userIDsChats);
    },
      (err: HttpErrorResponse) => {
        console.log(err);
        this.router.navigate(['/error', false], { skipLocationChange: true });
      });
  }

  loadChatRoom() {
    this.route.params.subscribe((params) => {
      this.chatID = +params['chat_id'];
      const userIdentifiers = { userToken: this.userToken, id: this.userId };
      const chatMessageContent = { chatID: this.chatID };
      console.log(chatMessageContent);
      const API_URL = environment.API_URL;
      const req = this.http.post(API_URL + '/api/getchatmessages', {
        userIdentifiers,
        chatMessageContent
      }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
        const obj = JSON.parse(data);

        this.showConversation = true;
      },
        (err: HttpErrorResponse) => {
          console.log(err);
          this.router.navigate(['/error', false], { skipLocationChange: true });
        });
    });
  }

}
