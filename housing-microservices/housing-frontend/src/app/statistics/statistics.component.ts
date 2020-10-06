import { Component, OnInit } from '@angular/core';
import {Statistics} from '../../statistics';
import {UserService} from '../../user.service';

@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.css']
})
export class StatisticsComponent implements OnInit {
  statistics: Statistics;
  service: UserService;

  constructor(service: UserService) {
    this.service = service;
  }

  ngOnInit() {
    this.getStatistics();
  }

  getStatistics() {
    this.service.getStatistics()
      .subscribe(statistics => this.statistics = statistics);
  }
}
