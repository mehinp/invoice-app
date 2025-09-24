import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { Observable, BehaviorSubject, map, startWith, catchError, of, switchMap } from 'rxjs';
import { DataState } from 'src/app/enum/datastate.enum';
import { AccountType, VerifyState } from 'src/app/interface/appstates';
import { User } from 'src/app/interface/user';
import { CustomerService } from 'src/app/service/customer.service';
import { NotificationService } from 'src/app/service/notification.service';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-verify',
  templateUrl: './verify.component.html',
  styleUrls: ['./verify.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class VerifyComponent implements OnInit {
  verifyState$: Observable<VerifyState>
  private userSubject = new BehaviorSubject<User>(null);
  user$ = this.userSubject.asObservable();
  private isLoadingSubject = new BehaviorSubject<boolean>(false);
  isLoading$ = this.isLoadingSubject.asObservable();
  public readonly DataState = DataState;

  constructor(private activatedRoute: ActivatedRoute, private userService: UserService, private notificationService: NotificationService) { }

  ngOnInit(): void {
    this.verifyState$ = this.activatedRoute.paramMap.pipe(
      switchMap((params: ParamMap) => {
        console.log(this.activatedRoute)
        const type: AccountType = this.getAccountType(window.location.href);
        return this.userService.verify$(params.get('key'), type)
          .pipe(
            map(response => {
              this.notificationService.onDefault(response.message);
              console.log(response)
              type === 'password' ? this.userSubject.next(response.data.user) : null;
              return {
                type, title: 'Verified!', dataState: DataState.LOADED, message: response.message, verifySucess: true
              };
            }),
            startWith({ type, title: 'Verifying...', dataState: DataState.LOADING, message: 'Please wait while we verify the information.', verifySucess: false }),
            catchError((error: string) => {
              this.notificationService.onError(error);
              return of({ title: 'An error occurred.', dataState: DataState.ERROR, message: error, verifySucess: false });
            })
          )
      })
    );
  }

  renewPassword(resetPasswordForm: NgForm): void {
    this.isLoadingSubject.next(true)
    this.verifyState$ = this.userService.renewPassword$({
      userId: this.userSubject.value.id,
      newPassword: resetPasswordForm.value.newPassword,
      confirmPassword: resetPasswordForm.value.confirmPassword
    })
      .pipe(
        map(response => {
          this.notificationService.onDefault(response.message);
          console.log(response)
          this.isLoadingSubject.next(false);
          return {
            type: 'account' as AccountType, dataState: DataState.LOADED, message: response.message
          };
        }),
        startWith({ type: 'password' as AccountType, dataState: DataState.LOADED}),
        catchError((error: string) => {
          this.notificationService.onError(error);
          this.isLoadingSubject.next(false);
          return of({ type: 'password' as AccountType, dataState: DataState.LOADED, error: error});
        })
      )
  }

  private getAccountType(url: string): AccountType {
    return url.includes('password') ? 'password' : 'account';
  }


}