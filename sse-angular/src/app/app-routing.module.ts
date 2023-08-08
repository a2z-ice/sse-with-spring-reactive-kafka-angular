import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { EventViewComponent } from './components/event-view/event-view.component';

const routes: Routes = [
  {
    path: ''
    , component: EventViewComponent
  },
  {
    path: ':userId',
    component: EventViewComponent
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {
    bindToComponentInputs: true,
    onSameUrlNavigation: 'reload'
  })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
