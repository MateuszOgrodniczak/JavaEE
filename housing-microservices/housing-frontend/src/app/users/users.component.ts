import { Component, OnInit } from '@angular/core';
import {User} from '../../user';
import {UserService} from '../../user.service';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {
  users: User[] = [];
  service;

  constructor(service: UserService) {
    this.service = service;
  }

  ngOnInit() {
    this.getUsers();
  }

  getUsers(): void {
    this.service.getUsers()
      .subscribe(users => this.users = users);
  }
}
