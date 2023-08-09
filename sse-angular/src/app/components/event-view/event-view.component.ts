import { Component, Input, NgZone, OnDestroy, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { SseService } from 'src/app/services/sse.service';

@Component({
  selector: 'app-event-view',
  templateUrl: './event-view.component.html',
  styleUrls: ['./event-view.component.scss']
})
export class EventViewComponent implements OnInit, OnDestroy {

  message = '';
  messages!: any[];
  sub!: Subscription;

  @Input() id!:string;

  constructor(private readonly sseService: SseService
    // , private http: HttpClient
    // , private readonly route: ActivatedRoute
    ) {

      // this.userId = this.route.snapshot.paramMap.get('userId') ?? '';
  }

  getMessages(): Observable<any> {

    if(!this.id) this.addMessage('Id Not Provided')

    console.log(this.id);

    return this.sseService.connectToSseStream(`http://localhost:4439/events/${this.id}`);
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
