# GRPC Services and Registry

This repo contains commands to run a registry server, two different service nodes, and two different clients.  
The clients will be able to access the the services of their respective nodes.  
One of them will communicate with the registry server in order to pull responses from the service node.
The other will communicate directly with the service node to access their services.

## Commands to start application


### `gradle runRegistryServer`
This will run the Registry node on localhost (arguments are possible see gradle). This node will run and allows nodes to register themselves. 


### `gradle registerServiceNode`

This command starts the node responsible for the services and registers its services with with the registry node. 

*This application will only run successfully if `runRegistryServer` is running and is needed for `runClient2` to run successfully*

Expected arguments: <regAddr(string)> <regPort(int)> <nodeAddr(string)> <nodePort(int)>

Argument format (Default is given):

 ``` 
-PregistryHost="localhost"
-PgrpcPort=9002
-PserviceHost="localhost"
-PservicePort=8000
-PnodeName="test"
```

### `gradle runClient2`

This command calls a client that will contact the registry server for a list of available services where the user will be able to make a numerical selection of the service they'd like to access.  After selecting the service, the client will contact the registry server once more for the server info of the registered service, and then return it to the client.  The client will then create a channel to the server to access the service.  Once completed, the channel will close out and the user will be returned the main menu (list of services).

*This application will only run successfully if `runRegistryServer` and `registerServerNode` is running.*

Expected arguments: <regHost(string)> <regPort(int)> <message(String)>


Argument format (Default is given):
 ```
-PregistryHost="localhost"
-PgrpcPort=9002
-Pmessage="Hello you"
```

### `gradle runNode`
This command starts the node responsible for the services.

Expected arguments: <regAddr(string)> <regPort(int)> nodeAddr(string)> <nodePort(int)>

Argument format (Default is given):

 ``` 
-PregistryHost="localhost"
-PgrpcPort=9002
-PserviceHost="localhost"
-PservicePort=8000
-PnodeName="test"
```

### gradle runClientJava
This will run a client which will call the services from the node, it talks to the node directly not through the registry. At the end the client does some calls to the Registry to pull the services, this will be needed later.

Expected arguments: <host(String)> <port(int)> <regHost(string)> <regPort(int)> <message(String)>

Argument format (Default is given):

 ```
-PserviceHost="localhost"
-PservicePort=8000
-PregistryHost="localhost"
-PgrpcPort=9002
-Pmessage="Hello you"
```

### `gradle runDiscovery`
Will create a couple of threads with each running a node with services in JSON and Protobuf. This is just an example and not needed for assignment 6. 

### `gradle testProtobufRegistration`
Registers the protobuf nodes from runDiscovery and do some calls. 

### `gradle testJSONRegistration`
Registers the json nodes from runDiscovery and do some calls. 

## Services

### `runClientJava`

(Integer inputs are required for most services, excluding Story)

1. Joke - This service returns a number of jokes from the server
2. Calculator - This service simulates a calculator with 4 basic operations
3. Story - This service saves a story that clients have created sentence by sentence and keeps track of the story. 
4. Kitty - This service starts interactions with a virtual cat.  The user has different ways of interaction with the cat and receiving a response from the cat.
5. Quit - This quits the client

### `runClient2`

1. Parrot - Sends back a message from the server
2. Set Joke - Allows you to add a joke <string> to the server
3. Get Joke - Retrieves a number of your choice <int> of jokes from the server
4. Subtract - Subtracts a number <int> of terms <int> of your choice
5. Divide - Divides a number <int> of terms <int> of your choice 
6. Add - Adds a number <int> of terms <int> of your choice 
7. Multiply - Multiplies a number <int> of terms <int> of your choice 
8. Read - Returns the last line of the "story" that was entered
9. Write - Allows you to add a new sentence <string> to the story then returns the story so far
10. Pet - Allows you to 'pet' a cat
11. Call - Allows you to attempt to beckon the cat
12. Get Name - Get name of the cat
13. Give Treat - Offers a treat<int> to the cat 
14. Peek - Checks out what the cat is doing 
0. Exit - Quit the client


## Requirements Fulfilled
### Activity 1
* Able to run with `gradle runNode` and `gradle runClientJava` with default arguments 
* No connection to register, just between client and service node
* Implements additional services: 
 * Calc - calc.proto
 	* add
 	* subtract
 	* multiply
 	* divide
 * Story - story.proto
 	* read
 	* write
* User is able to navigate using CLI using mainly int inputs (Occasionally string inputs)

### Activity 2
* Able to run with `gradle registerServiceNode` and `gradle runClient2` with default arguments 
* Connection with registry is re-established for both service node and client
* Client contacts the registry for available services and lists the available services
* User is able to choose an available service by entering an <(int)>
* The request then calls the registry again and asks for a server of the chosen service
* The client then sends a request to the server of that service and returns the response

## [Screencast](https://youtu.be/7EO8t2FVEWQ)


 




