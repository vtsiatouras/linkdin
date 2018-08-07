import { Component, OnInit, Input } from '@angular/core';
import { faStar } from '@fortawesome/free-solid-svg-icons';
import { faCommentAlt } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnInit {

  faStar = faStar;
  faCommentAlt = faCommentAlt;

  @Input() userName: string;
  @Input() userSurname: string;


  constructor() { }

  ngOnInit() {
  }

}
