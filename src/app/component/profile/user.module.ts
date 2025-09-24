import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';
import { NavbarModule } from '../navbar/navbar.module';
import { UserComponent } from './user/user.component';
import { UserRoutingModule } from './user-routing.module';

@NgModule({
  declarations: [ UserComponent ],
  imports: [ SharedModule, UserRoutingModule, NavbarModule ]
})

export class UserModule {}
