import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_client_sse/flutter_client_sse.dart';

void main() => runApp(const MyApp());

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    const title = 'SSE Client';
    return const MaterialApp(
      title: title,
      home: MyHomePage(
        title: title,
      ),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({
    super.key,
    required this.title,
  });

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  final _socketResponse = StreamController<String>();

  @override
  void initState() {
    SSEClient.subscribeToSSE(url: 'http://192.168.2.3:8071/subscribe', header: {
      "Accept": "text/event-stream",
      "Cache-Control": "no-cache",
    }).listen((event) {
      print('Event: ' + event.event!);
      print('Data: ' + event.data!);
      _socketResponse.add(event.data!);
    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Padding(
        padding: const EdgeInsets.all(20.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            StreamBuilder(
              stream: _socketResponse.stream,
              builder: (context, snapshot) {
                return Text(snapshot.hasData ? '${snapshot.data}' : '');
              },
            )
          ],
        ),
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }
}
