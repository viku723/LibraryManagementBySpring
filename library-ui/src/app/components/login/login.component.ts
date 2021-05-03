import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { SnackbarService } from 'src/app/services/snackbar.service';
import { UserService } from 'src/app/services/user.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent {
    formGroup: FormGroup;
    constructor(private formBuilder: FormBuilder, public userService: UserService,
        private router: ActivatedRoute, private route: Router, private snackbarService: SnackbarService) {
        let emailregex: RegExp = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
        this.formGroup = this.formBuilder.group({
            'email': [null, [Validators.required, Validators.pattern(emailregex)]],
            'password': [null, [Validators.required]],
        });
    }

    ngOnInit() {
        if (this.userService.isLoggedIn()) this.route.navigate(["/home"])
    }

    getErrorEmail() {
        // @ts-ignore: Object is possibly 'null'.
        return this.formGroup.get('email').hasError('required') ? 'Field is required' : this.formGroup.get('email').hasError('pattern') ? 'Not a valid emailaddress' : this.formGroup.get('email').hasError('alreadyInUse') ? 'This emailaddress is already in use' : '';
    }

    getErrorPassword() {
        // @ts-ignore: Object is possibly 'null'.
        return this.formGroup.get('password').hasError('required') ? 'Field is required (at least eight characters, one uppercase letter and one number)' : this.formGroup.get('password').hasError('requirements') ? 'Password needs to be at least eight characters, one uppercase letter and one number' : '';
    }
    onSubmit(credentials: FormGroup) {
        this.userService.login(credentials)
        .subscribe((userDetails) => {
            this.userService.setUserDetails(userDetails);
            this.route.navigate(["/home"]);
        });
    }

    onRegister() {
        this.route.navigate(["register"]);
    }
}
