import { Component, Input, NgZone, OnDestroy, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-event-view',
  templateUrl: './event-view.component.html',
  styleUrls: ['./event-view.component.scss']
})
export class EventViewComponent implements OnInit, OnDestroy {

  message = '';
  messages!: any[];
  sub!: Subscription;

  // @Input() userId!:string;
  userId!:string;

  constructor(private zone: NgZone
    , private http: HttpClient
    , private readonly route: ActivatedRoute
    ) {

      this.userId = this.route.snapshot.paramMap.get('userId') ?? '';
  }

  getMessages(): Observable<any> {

    if(!this.userId) this.addMessage('User Id Not Provided')

    return Observable.create(
      (observer:any) => {

        let source = new EventSource(`http://localhost:4439/events/${this.userId}`);
        source.onmessage = event => {
          this.zone.run(() => {
            observer.next(event.lastEventId)
            observer.next(event.data)
          })
        }

        source.onerror = event => {
          this.zone.run(() => {
            observer.error(event)
          })
        }
      }
    )
  }

  ngOnInit(): void {
    this.messages = [];
    this.sub = this.getMessages().subscribe({
      next: data => {
        console.log(data);
        this.addMessage(data);
      },
      error: err => console.error(err)
    });
  }

  addMessage(msg: any) {
    this.messages = [...this.messages, msg];
    //console.log("messages::" + this.messages);
  }

  ngOnDestroy(): void {
    this.sub && this.sub.unsubscribe();
  }


}
