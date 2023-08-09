import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { SseService } from 'src/app/services/sse.service';
import { v4 as uuidv4 } from 'uuid';

@Component({
  selector: 'app-event-view',
  templateUrl: './event-view.component.html',
  styleUrls: ['./event-view.component.scss']
})
export class EventViewComponent implements OnInit, OnDestroy {

  messages!: any[];
  sub!: Subscription;

  @Input() id!: string;

  constructor(private readonly sseService: SseService) {
  }

  getMessages(): Observable<any> {

    if (!this.id) this.addMessage('Id Not Provided')

    console.log(this.id);

    const eventId = uuidv4();
    console.log(eventId)
    return this.sseService.connectToSseStream(`http://localhost:4439/events/${this.id}/${eventId}}`);
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
  }

  ngOnDestroy(): void {
    this.sub && this.sub.unsubscribe();
  }

}
