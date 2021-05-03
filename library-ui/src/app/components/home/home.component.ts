import { BOOK_LIST_TYPE } from './../../book/book-list/book-list.component';
import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  
  bookListType: typeof BOOK_LIST_TYPE = BOOK_LIST_TYPE;
  constructor(public userService: UserService) { }

  ngOnInit(): void {
  }

}
