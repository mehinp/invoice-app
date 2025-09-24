import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Observable, BehaviorSubject, map, startWith, catchError, of } from 'rxjs';
import { DataState } from 'src/app/enum/datastate.enum';
import { CustomHttpResponse, Page } from 'src/app/interface/appstates';
import { Customer } from 'src/app/interface/customer';
import { State } from 'src/app/interface/state';
import { User } from 'src/app/interface/user';
import { CustomerService } from 'src/app/service/customer.service';
import { NotificationService } from 'src/app/service/notification.service';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-newinvoicecomponent',
  templateUrl: './newinvoicecomponent.component.html',
  styleUrls: ['./newinvoicecomponent.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class NewinvoicecomponentComponent  implements OnInit {
  newInvoiceState$: Observable<State<CustomHttpResponse<Customer[] & User>>>
  private dataSubject = new BehaviorSubject<CustomHttpResponse<Customer[] & User>>(null);
  private isLoadingSubject = new BehaviorSubject<boolean>(false);
  isLoading$ = this.isLoadingSubject.asObservable();
  public readonly DataState = DataState;

  constructor(private userSerivce: UserService, private customerService: CustomerService, private notificationService: NotificationService) { }

  ngOnInit(): void {
    this.newInvoiceState$ = this.customerService.newInvoice$()
      .pipe(
        map(response => {
          console.log(response)
          this.dataSubject.next(response);
          return {
            dataState: DataState.LOADED, appData: response
          };
        }),
        startWith({ dataState: DataState.LOADING }),
        catchError((error: string) => {
          this.notificationService.onError(error);
          return of({
            dataState: DataState.ERROR,
            error,
          });
        })
      )
  }

  newInvoice(newInvoiceForm: NgForm): void {
    this.isLoadingSubject.next(true);
    this.newInvoiceState$ = this.customerService.createInvoice$(newInvoiceForm.value.customerId, newInvoiceForm.value)
      .pipe(
        map(response => {
          this.notificationService.onDefault(response.message);
          console.log(response)
          newInvoiceForm.reset( {status: 'PENDING'})
          this.isLoadingSubject.next(false);
          this.dataSubject.next(response)
          return {
            dataState: DataState.LOADED, appData: this.dataSubject.value
          };
        }),
        startWith({ dataState: DataState.LOADED, appData: this.dataSubject.value }),
        catchError((error: string) => {
          this.notificationService.onDefault(error);
          this.isLoadingSubject.next(false);
          return of({
            dataState: DataState.LOADED,
            error,
          });
        })
      )
  }
}
