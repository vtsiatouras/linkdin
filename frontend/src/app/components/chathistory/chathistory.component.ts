import { Component, OnInit, Input, AfterViewChecked, ElementRef, ViewChild } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { environment } from '../../../environments/environment';


@Component({
  selector: 'app-chathistory',
  templateUrl: './chathistory.component.html',
  styleUrls: ['./chathistory.component.css']
})
export class ChathistoryComponent implements OnInit, AfterViewChecked {

  @ViewChild('chathistoryContent') private myScrollContainer: ElementRef;


  userId = localStorage.getItem('userID');
  userToken = localStorage.getItem('userToken');
  userName = localStorage.getItem('firstName');
  userSurname = localStorage.getItem('lastName');
  userImage;

  @Input() chatUserId;
  chatUserName;
  chatUserSurname;
  chatImage;


  @Input() chatHistory = [];


  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient) { }

  ngOnInit() {
    this.scrollToBottom();
  }

  ngAfterViewChecked() {
    this.scrollToBottom();
  }

  scrollToBottom(): void {
    try {
      this.myScrollContainer.nativeElement.scrollTop = this.myScrollContainer.nativeElement.scrollHeight;
    } catch (err) { }
  }

  // getUserIdentifiers() {
  //      const userIdentifiers = { userToken: this.userToken, id: this.userId };
  //   const userInfoRequest = { userIdInfo: this.userIDInfo };
  //   const API_URL = environment.API_URL;
  //   const req = this.http.post(API_URL + '/api/getuserbasicinfo', {
  //     userIdentifiers,
  //     userInfoRequest
  //   }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
  //     const obj = JSON.parse(data);
  //     this.userName = obj.name;
  //     this.userSurname = obj.surname;
  //     this.userImage = 'data:image/jpeg;base64,' + obj.image;
  //     this.render = true;
  //   },
  //     (err: HttpErrorResponse) => {
  //       console.log(err);
  //     });
  // }


}
