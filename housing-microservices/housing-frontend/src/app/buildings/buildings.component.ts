import { Component, OnInit } from '@angular/core';
import {Building} from '../../building';
import {UserService} from '../../user.service';

@Component({
  selector: 'app-buildings',
  templateUrl: './buildings.component.html',
  styleUrls: ['./buildings.component.css']
})
export class BuildingsComponent implements OnInit {
  buildings: Building[] = [];
  service;

  constructor(service: UserService) {
    this.service = service;
  }

  ngOnInit() {
    this.getBuildings();
  }

  getBuildings(): void {
    this.service.getBuildings()
      .subscribe(buildings => this.buildings = buildings);
  }
}
