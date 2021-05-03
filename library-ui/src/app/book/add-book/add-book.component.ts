import { DataService } from './../../services/data.service';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { SnackbarService } from 'src/app/services/snackbar.service';
import { UserService } from 'src/app/services/user.service';
import { take, switchMap, filter } from 'rxjs/operators';

@Component({
    templateUrl: './add-book.component.html',
    styleUrls: ['./add-book.component.scss']
})
export class AddBookComponent implements OnInit {

    formGroup: FormGroup;
    editBookId: number = -1;

    constructor(private formBuilder: FormBuilder, public userService: UserService, public dataService: DataService,
        private router: ActivatedRoute, private route: Router, private snackbarService: SnackbarService, private activatedRoute: ActivatedRoute
        ) {
        this.formGroup = this.formBuilder.group({
            'name': [null, [Validators.required]],
            'author': [null, [Validators.required]],
            'price': [null, [Validators.required]]
        });
    }

    ngOnInit(): void {
        this.activatedRoute.params.pipe(
            filter(param => !!param["id"]),
            switchMap(param => {
                this.editBookId = param["id"];
                return this.dataService.get("/book/" + this.editBookId)
            })
        ).subscribe((book: any) => {
            console.log("book", book);
            const { name, price, author } = book;
            this.formGroup.setValue({
                name,
                author,
                price
            });
        });
    }

    onSubmit(book: FormGroup) {
        this.dataService.post(this.editBookId === -1 ? "/book/add": "/book/update/" + this.editBookId, book)
            .pipe(take(1))
            .subscribe((response: any) => {
                this.snackbarService.openSnackBar(response.success)
                this.route.navigate(["/home"]);
            });
    }
}
