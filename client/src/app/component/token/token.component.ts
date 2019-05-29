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
  selectedOption = '10';

  generate(level: string) {
    this.tokenService.getToken(level, this.selectedOption).subscribe((data: string) => {
      this.token = data;
    });
  }

  ngOnInit() {
  }

}
