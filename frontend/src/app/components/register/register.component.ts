import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {AbstractControl} from "@angular/forms";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  email: string;
  password_1: string;
  password_2: string;
  firstName: string;
  lastName: string;
  phoneNumber: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) { }

  ngOnInit() {
  }

  registerUser() {
    const req = this.http.post('http://localhost:8080/api/register', {
      email: this.email,
      password_1: this.password_1,
      password_2: this.password_2,
      firstName: this.firstName,
      lastName: this.firstName,
      phoneNumber: this.phoneNumber
    }, {responseType: 'text', withCredentials: true}).subscribe((data: any) => {
      this.router.navigate(['/login']); // TODO make a pop up that says "Register completed"
      },
      (err: HttpErrorResponse) => {
        console.log(err); // TODO this will be trigger if the server is down or the email already exists.
    });
  }

}
