import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { take } from 'rxjs/operators';
import { DataService } from 'src/app/services/data.service';
import { SnackbarService } from 'src/app/services/snackbar.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  templateUrl: './register-user.component.html',
  styleUrls: ['./register-user.component.scss']
})
export class RegisterUserComponent implements OnInit {
  formGroup: FormGroup;
  constructor(private formBuilder: FormBuilder, public userService: UserService, public dataService: DataService,
    private router: ActivatedRoute, private route: Router, private snackbarService: SnackbarService,) { 
    this.formGroup = this.formBuilder.group({
      'name': [null, [Validators.required]],
      'email': [null, [Validators.required]],
      'password': [null, [Validators.required]]
  });
  }

  ngOnInit(): void {
  }

  onSubmit(user: FormGroup) {
    this.dataService.post("/user/add", user)
        .pipe(take(1))
        .subscribe((response: any) => {
            this.snackbarService.openSnackBar(response.success)
            this.route.navigate(["/login"]);
        });
}

}
