import 'package:flutter_client_sse/flutter_client_sse.dart';

main() {
  SSEClient.subscribeToSSE(url: 'http://localhost:8071/subscribe', header: {
    "Accept": "text/event-stream",
    "Cache-Control": "no-cache",
  }).listen((event) {
    print('Id: ' + event.id!);
    print('Event: ' + event.event!);
    print('Data: ' + event.data!);
  });
}
