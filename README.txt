.
├── build.gradle    [Builds the applications]
├── README.txt      [This file]
└── src
    ├── main
    │   └── java
    │       └── csx55
    │           └── overlay
    │               ├── dijkstra
    │               │   ├── Graph.java                          [Graph implementation for use in ShortestPath.java]
    │               │   ├── RoutingCache.java                   []
    │               │   └── ShortestPath.java                   [Finds the shortest path between messaging nodes]
    │               ├── node
    │               │   ├── MessagingNode.java                  [MessagingNode application, joins the overlay and sends messages to other messaging nodes]
    │               │   ├── Node.java                           [Base class for nodes, provides core functionality]
    │               │   └── Registry.java                       [Registry application, sets up the overlay and initiates tasks]
    │               ├── transport
    │               │   ├── TCPReceiverThread.java              [Reads input from sockets without blocking the main thread]
    │               │   ├── TCPSender.java                      [Sends events to other nodes]
    │               │   └── TCPServerThread.java                [Listens for new TCP connections without blocking the main thread]
    │               ├── util
    │               │   ├── InputReceiverThread.java            [Reads input from System.in, queues input strings]
    │               │   ├── OverlayCreator.java                 [Sets up the overlay as specified in the assignment]
    │               │   └── StatisticsCollectorAndDisplay.java
    │               └── wireformats                             [All files are events specified by the assignment unless otherwise noted]
    │                   ├── Deregister.java
    │                   ├── DeregisterResponse.java
    │                   ├── EventFactory.java                   [Reads events from byte arrays and returns events]
    │                   ├── Event.java                          [Base class for event types, uses reflection to marshall/unmarshall events]
    │                   ├── LinkInfo.java
    │                   ├── LinkWeights.java
    │                   ├── Message.java
    │                   ├── MessagingNodeInfo.java
    │                   ├── MessagingNodesList.java
    │                   ├── Protocol.java
    │                   ├── Register.java
    │                   ├── RegisterResponse.java
    │                   ├── TaskComplete.java
    │                   ├── TaskInitiate.java
    │                   ├── TaskSummaryRequest.java
    │                   └── TaskSummaryResponse.java
    └── test
        └── java
            └── csx55
                └── overlay
                    ├── transport
                    │   ├── TCPReceiverThreadTest.java
                    │   ├── TCPSenderTest.java
                    │   └── TestTCPServerThread.java
                    └── wireformats
                        ├── LinkInfoTest.java
                        ├── LinkWeightsTest.java
                        ├── TaskCompleteTest.java
                        ├── TaskInitiateTest.java
                        ├── TaskSummaryRequestTest.java
                        ├── TaskSummaryResponseTest.java
                        ├── TestDeregister.java
                        ├── TestEventFactory.java
                        ├── TestEvent.java
                        ├── TestRegister.java
                        └── TestRegisterResponse.java
