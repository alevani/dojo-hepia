import {Component, OnInit} from '@angular/core';
import {TokenService} from '../../services/auth/signin/token.service';

@Component({
  selector: 'app-token',
  templateUrl: './token.component.html',
  styleUrls: ['./token.component.css']
})
export class TokenComponent implements OnInit {

  constructor(private tokenService: TokenService) {
  }

  token = '';

  generate() {
    this.tokenService.getToken().subscribe((data: string) => {
      this.token = data;
    });
  }

  ngOnInit() {
  }

}
