import { Injectable, NgZone } from '@angular/core';
import { Observable, delay, retryWhen } from 'rxjs';
import { EventInfo } from '../models/event-info.model';

@Injectable({
  providedIn: 'root'
})
export class SseService {

  private eventSource!: EventSource;

  constructor(private readonly zone: NgZone) { }

  connectToSseStream(url: string): Observable<EventInfo> {
    return new Observable<any>(observer => {
      this.eventSource = new EventSource(url);

      this.eventSource.onmessage = event => {
        this.zone.run(() => {
        observer.next({
          lastEventId: event.lastEventId,
          data: JSON.parse(event.data)
        });
      })
      };

      this.eventSource.onerror = error => {
        this.zone.run(() => {
        observer.error(error);
        this.eventSource.close();
        })

      };

      return () => {
        this.eventSource.close();
      };
    }).pipe(
      retryWhen(errors => {
          return errors.pipe(
              delay(1000), // Retry every one second
              // You can customize the number of retries if needed
              // retry(5) // Retry up to 5 times
          );
      })
  );
  }
}
