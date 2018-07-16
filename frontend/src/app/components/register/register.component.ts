import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';

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
  fileToUpload: File = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) { }

  ngOnInit() {
  }

  registerUser() {

    const formData: FormData = new FormData();

    const user = {
      'email': this.email,
      'password_1': this.password_1,
      'password_2': this.password_2,
      'firstName': this.firstName,
      'lastName': this.lastName,
      'phoneNumber': this.phoneNumber
    };

    const image = {
        'imageFile': this.fileToUpload
    };

    formData.append('user', JSON.stringify(user));
    formData.append('profileImage', this.fileToUpload);

    console.log(formData);

    const req = this.http.post('http://localhost:8080/api/register',
      formData
     , {responseType: 'text', withCredentials: true}).subscribe((data: any) => {
      this.router.navigate(['/login']); // TODO make a pop up that says "Register completed"
      },
      (err: HttpErrorResponse) => {
        console.log(err); // TODO this will be trigger if the server is down or the email already exists.
    });
  }

  handleFileInput(files: FileList) {
    this.fileToUpload = files.item(0);
  }


}
