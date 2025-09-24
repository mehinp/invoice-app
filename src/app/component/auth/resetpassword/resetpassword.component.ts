import { ChangeDetectionStrategy, Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Observable, of, map, startWith, catchError } from 'rxjs';
import { DataState } from 'src/app/enum/datastate.enum';
import { RegisterState } from 'src/app/interface/appstates';
import { NotificationService } from 'src/app/service/notification.service';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-resetpassword',
  templateUrl: './resetpassword.component.html',
  styleUrls: ['./resetpassword.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ResetpasswordComponent {
  resetPasswordState$: Observable<RegisterState> = of({dataState: DataState.LOADED});
  readonly DataState = DataState;

  constructor(private userService: UserService, private notificationService: NotificationService) {}

  resetPassword(resetPasswordForm: NgForm): void {
    const email = resetPasswordForm.value.email
    this.resetPasswordState$ = this.userService.requestPasswordReset$(email)
      .pipe(
        map(response => {
          this.notificationService.onDefault(response.message)
          resetPasswordForm.reset();
          return { dataState: DataState.LOADED, registerSuccess: true, message: response.message}
        }),
        startWith( {dataState: DataState.LOADING, registerSuccess: false}),
        catchError((error: string) => {
          this.notificationService.onError(error)
          return of({dataState: DataState.ERROR, registerSuccess: false, error});
        })
      );
  }

  createAccountForm(): void {
    this.resetPasswordState$ = of({dataState: DataState.LOADED, registerSuccess: false});
  }

}


