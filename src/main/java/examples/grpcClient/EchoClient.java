package example.grpcclient;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.concurrent.TimeUnit;
import service.*;
import test.TestProtobuf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Client that requests `parrot` method from the `EchoServer`.
 */
public class EchoClient {
  private final EchoGrpc.EchoBlockingStub blockingStub;
  private final JokeGrpc.JokeBlockingStub blockingStub2;
  private final RegistryGrpc.RegistryBlockingStub blockingStub3;
  private final CalcGrpc.CalcBlockingStub blockingStub4;
  private final StoryGrpc.StoryBlockingStub blockingStub5;
  private final KittyGrpc.KittyBlockingStub blockingStub6;

  /** Construct client for accessing server using the existing channel. */
  public EchoClient(Channel channel, Channel regChannel) {
    // 'channel' here is a Channel, not a ManagedChannel, so it is not this code's
    // responsibility to
    // shut it down.

    // Passing Channels to code makes code easier to test and makes it easier to
    // reuse Channels.
    blockingStub = EchoGrpc.newBlockingStub(channel);
    blockingStub2 = JokeGrpc.newBlockingStub(channel);
    blockingStub3 = RegistryGrpc.newBlockingStub(regChannel);
    blockingStub4 = CalcGrpc.newBlockingStub(channel);
    blockingStub5 = StoryGrpc.newBlockingStub(channel);
    blockingStub6 = KittyGrpc.newBlockingStub(channel);
  }

  public void askServerToParrot(String message) {
    ClientRequest request = ClientRequest.newBuilder().setMessage(message).build();
    ServerResponse response;
    try {
      response = blockingStub.parrot(request);
    } catch (Exception e) {
      System.err.println("RPC failed: " + e.getMessage());
      return;
    }
    System.out.println("Received from server: " + response.getMessage());
  }

  public void askForJokes(int num) {
    JokeReq request = JokeReq.newBuilder().setNumber(num).build();
    JokeRes response;

    try {
      response = blockingStub2.getJoke(request);
    } catch (Exception e) {
      System.err.println("RPC failed: " + e);
      return;
    }
    System.out.println("Your jokes: ");
    for (String joke : response.getJokeList()) {
      System.out.println("--- " + joke);
    }
  }

  public void setJoke(String joke) {
    JokeSetReq request = JokeSetReq.newBuilder().setJoke(joke).build();
    JokeSetRes response;

    try {
      response = blockingStub2.setJoke(request);
      System.out.println(response.getOk());
    } catch (Exception e) {
      System.err.println("RPC failed: " + e);
      return;
    }
  }

  public void add(double[] inputs) {
    int size = inputs.length;
    CalcRequest.Builder request = CalcRequest.newBuilder();

    for (int i = 0; i < size; i++) {
      request.addNum(inputs[i]);
    }
    CalcResponse response;
    CalcRequest req = request.build();

    try {
      response = blockingStub4.add(req);
      if (response.getIsSuccess()) {
        System.out.println("Sum: " + response.getSolution());
        // System.out.println(response.getIsSuccess());
      } else {
        System.out.println(response.getError());
      }

    } catch (Exception e) {
      System.err.println("RPC failed: " + e);
      return;
    }
  }

  public void subtract(double[] inputs) {
    int size = inputs.length;
    CalcRequest.Builder request = CalcRequest.newBuilder();

    for (int i = 0; i < size; i++) {
      request.addNum(inputs[i]);
    }
    CalcResponse response;
    CalcRequest req = request.build();

    try {
      response = blockingStub4.subtract(req);
      if (response.getIsSuccess()) {
        System.out.println("Difference: " + response.getSolution());
        // System.out.println(response.getIsSuccess());
      } else {
        System.out.println(response.getError());
      }

    } catch (Exception e) {
      System.err.println("RPC failed: " + e);
      return;
    }
  }

  public void multiply(double[] inputs) {
    int size = inputs.length;
    CalcRequest.Builder request = CalcRequest.newBuilder();

    for (int i = 0; i < size; i++) {
      request.addNum(inputs[i]);
    }
    CalcResponse response;
    CalcRequest req = request.build();

    try {
      response = blockingStub4.multiply(req);
      if (response.getIsSuccess()) {
        System.out.println("Product: " + response.getSolution());
        // System.out.println(response.getIsSuccess());
      } else {
        System.out.println(response.getError());
      }

    } catch (Exception e) {
      System.err.println("RPC failed: " + e);
      return;
    }
  }

  public void divide(double[] inputs) {
    int size = inputs.length;
    CalcRequest.Builder request = CalcRequest.newBuilder();

    for (int i = 0; i < size; i++) {
      request.addNum(inputs[i]);
    }
    CalcResponse response;
    CalcRequest req = request.build();

    try {
      response = blockingStub4.divide(req);
      if (response.getIsSuccess()) {
        System.out.println("Quotient: " + response.getSolution());
        // System.out.println(response.getIsSuccess());
      } else {
        System.out.println(response.getError());
      }

    } catch (Exception e) {
      System.err.println("RPC failed: " + e);
      return;
    }
  }

  public void read() {
    Empty req = Empty.newBuilder().build();
    ReadResponse response;
    try {
      response = blockingStub5.read(req);
      if (response.getIsSuccess()) {
        System.out.println("Last sentence entered: ");
        System.out.println(response.getSentence());
      } else {
        System.out.println(response.getError());
      }
    } catch (Exception e) {
      System.err.println("RPC failed: " + e);
      return;
    }
  }

  public void write(String sent) {
    WriteRequest.Builder request = WriteRequest.newBuilder();
    WriteResponse response;
    request.setNewSentence(sent);
    WriteRequest req = request.build();
    try {
      response = blockingStub5.write(req);
      if (response.getIsSuccess()) {
        System.out.println("The story so far:");
        System.out.println(response.getStory());
      } else {
        System.out.println(response.getError());
      }
    } catch (Exception e) {
      System.err.println("RPC failed: " + e);
      return;
    }
  }

  public String name() {
    String n;
    None req = None.newBuilder().build();
    Name response;
    try {
      response = blockingStub6.getName(req);
      n = response.getName();
      System.out.println("You check the name tag on the cat's collar: " + n);
    } catch (Exception e) {
      System.err.println("RPC failed: " + e);
      return null;
    }
    return n;
  }

  public void pet(String catname) {
    None req = None.newBuilder().build();
    PetResp response;
    try {
      response = blockingStub6.pet(req);
      System.out.println(catname + " " + response.getResponse());
    } catch (Exception e) {
      System.err.println("RPC failed: " + e);
      return;
    }
  }

  public int pickTreat(BufferedReader reader) {
    int choice = 0;
    boolean valid = false;

    do {
      valid = true;
      try {
        System.out.println("Please select the type of treat you'd like offer:");
        System.out.println("1. Chewy treat");
        System.out.println("2. Crunchy treat");
        String num = reader.readLine();
        choice = Integer.valueOf(num);
        switch (choice) {
          case (1):
            return choice;
          case (2):
            return choice;
          default:
            valid = false;
            System.out.println("Incorrect input, please select either 1 or 2.");
            continue;
        }
      } catch (Exception e) {
        valid = false;
        System.out.println("Invalid entry, please try again");
        e.printStackTrace();
      }
    } while (!valid);

    return choice;
  }

  public void giveTreat(int treat, String catname) {

    System.out.println("You offer the treat to " + catname);
    TreatReq.Builder request = TreatReq.newBuilder();
    request.setQuality(treat);
    TreatReq req = request.build();
    TreatResp response;

    try {
      response = blockingStub6.giveTreat(req);
      System.out.println(catname + " " + response.getResponse());
    } catch (Exception e) {
      System.err.println("RPC failed: " + e);
      return;
    }

  }

  public void peek(String catname) {
    System.out.println("You glance over at " + catname);
    None req = None.newBuilder().build();

    PeekResp response;
    try {
      response = blockingStub6.peek(req);
      System.out.println(catname + " " + response.getResponse());
    } catch (Exception e) {
      System.err.println("RPC failed: " + e);
      return;
    }
  }

  public void call(String catname) {
    System.out.println("You call over to " + catname);
    None req = None.newBuilder().build();

    CallResp response;
    try {
      response = blockingStub6.call(req);
      System.out.println(catname + " " + response.getResponse());
    } catch (Exception e) {
      System.err.println("RPC failed: " + e);
      return;
    }
  }

  public void getServices() {
    GetServicesReq request = GetServicesReq.newBuilder().build();
    ServicesListRes response;
    try {
      response = blockingStub3.getServices(request);
      System.out.println(response.toString());
    } catch (Exception e) {
      System.err.println("RPC failed: " + e);
      return;
    }
  }

  public void findServer(String name) {
    FindServerReq request = FindServerReq.newBuilder().setServiceName(name).build();
    SingleServerRes response;
    try {
      response = blockingStub3.findServer(request);
      System.out.println(response.toString());
    } catch (Exception e) {
      System.err.println("RPC failed: " + e);
      return;
    }
  }

  public void findServers(String name) {
    FindServersReq request = FindServersReq.newBuilder().setServiceName(name).build();
    ServerListRes response;
    try {
      response = blockingStub3.findServers(request);
      System.out.println(response.toString());
    } catch (Exception e) {
      System.err.println("RPC failed: " + e);
      return;
    }
  }

  public int getCalcInput(BufferedReader reader, boolean valid, int max) {
    int choice = 0;
    do {
      try {
        valid = true;
        System.out.println("Please enter the number of integers you'd like to calculate(Up to " + max + "):");
        String num = reader.readLine();
        choice = Integer.valueOf(num);
        if (choice < 0 || choice > 25) {
          System.out.println("Number ouf of range, try again");
          valid = false;
          continue;
        }
      } catch (NumberFormatException e) {
        // Handles if user's input is not a numberical value
        System.out.println("Incorrect input, please try again");
        valid = false;
      } catch (IOException e) {
        System.out.println("Invalid input, please try again");
        valid = false;
      }
    } while (!valid);

    return choice;
  }

  public String getWriteInput(BufferedReader reader, boolean valid) {
    String input = "Default one-liner.";
    do {
      try {
        valid = true;
        System.out.println("Please enter the next line for the story: ");
        input = reader.readLine();
      } catch (Exception e) {
        valid = false;
        System.out.println("Something went wrong, try again");
        e.printStackTrace();
      }
    } while (!valid);

    return input;
  }

  public double[] getCalcArr(BufferedReader reader, boolean valid, int size) {

    double[] inputs = new double[size];
    for (int i = 0; i < size; i++) {
      do {
        try {
          valid = true;
          System.out.println("Enter input #" + (i + 1) + ": ");
          String num = reader.readLine();
          double input = Double.valueOf(num);
          inputs[i] = input;
        } catch (NumberFormatException e) {
          // Handles if user's input is not a numberical value
          System.out.println("Incorrect input, please try again");
          valid = false;
        } catch (IOException e) {
          System.out.println("Invalid input, please try again");
          valid = false;
        }
      } while (!valid);
    }
    return inputs;
  }

  public static void main(String[] args) throws Exception {
    if (args.length != 5) {
      System.out
          .println("Expected arguments: <host(String)> <port(int)> <regHost(string)> <regPort(int)> <message(String)>");
      System.exit(1);
    }
    int port = 9099;
    int regPort = 9003;
    String host = args[0];
    String regHost = args[2];
    String message = args[4];
    try {
      port = Integer.parseInt(args[1]);
      regPort = Integer.parseInt(args[3]);
    } catch (NumberFormatException nfe) {
      System.out.println("[Port] must be an integer");
      System.exit(2);
    }

    // Create a communication channel to the server, known as a Channel. Channels
    // are thread-safe
    // and reusable. It is common to create channels at the beginning of your
    // application and reuse
    // them until the application shuts down.
    String target = host + ":" + port;
    ManagedChannel channel = ManagedChannelBuilder.forTarget(target)
        // Channels are secure by default (via SSL/TLS). For the example we disable TLS
        // to avoid
        // needing certificates.
        .usePlaintext().build();

    String regTarget = regHost + ":" + regPort;
    ManagedChannel regChannel = ManagedChannelBuilder.forTarget(regTarget).usePlaintext().build();

    // ##############################################################################
    // ## Assume we know the port here from the service node it is basically set
    // through Gradle
    // here.
    // In your version you should first contact the registry to check which services
    // are available and what the port
    // etc is.

    /**
     * Your client should start off with
     * 1. contacting the Registry to check for the available services
     * 2. List the services in the terminal and the client can
     * choose one (preferably through numbering)
     * 3. Based on what the client chooses
     * the terminal should ask for input, eg. a new sentence, a sorting array or
     * whatever the request needs
     * 4. The request should be sent to one of the
     * available services (client should call the registry again and ask for a
     * Server providing the chosen service) should send the request to this service
     * and
     * return the response in a good way to the client
     * 
     * You should make sure your client does not crash in case the service node
     * crashes or went offline.
     */

    // Just doing some hard coded calls to the service node without using the
    // registry

    try {
      // create client
      EchoClient client = new EchoClient(channel, regChannel);

      // call the parrot service on the server
      client.askServerToParrot(message);

      // ask the user for input how many jokes the user wants
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

      String num = "";
      int choice = 0;
      boolean valid = false;
      final int LIMIT = 25;
      double[] inputs;
      int val = 0;
      boolean active = true;
      String catname = "The unknown cat";

      do {
        try {
          valid = true;
          System.out.println("Enter number of the service you'd like to use: ");
          System.out.println("1. Joke");
          System.out.println("2. Calculator");
          // System.out.println("3. List Registry Services");
          System.out.println("3. Story");
          System.out.println("4. Kitty");
          System.out.println("5. Quit");
          num = reader.readLine();
          choice = Integer.valueOf(num);

          switch (choice) {

            case (1): // Reading data using readLine while "valid" for JOKE OPTION
              do {
                num = "";
                try {
                  System.out.println("How many jokes would you like?");
                  num = reader.readLine();
                  valid = true;

                  // calling the joked service from the server with num from user input
                  client.askForJokes(Integer.valueOf(num));

                  // adding a joke to the server
                  // client.setJoke("I made a pencil with two erasers. It was pointless.");

                  // showing 6 jokes
                  // client.askForJokes(Integer.valueOf(6));
                } catch (NumberFormatException e) {
                  // Handles if user's input is not a numberical value
                  System.out.println("Incorrect input, please try again");
                  valid = false;
                }
              } while (!valid);
              break;

            case (2): // CALC Option Prompt
              do {
                num = "";
                try {
                  System.out.println("Enter the number of calculation you'd like to perform:");
                  System.out.println("1. Add - Adds all numbers");
                  System.out.println("2. Subtract - Subtracts first integer by sum of all other numbers");
                  System.out.println("3. Multiply - Multiplies all numbers");
                  System.out.println("4. Divide - Divides the first number by sum of all other numbers");
                  num = reader.readLine();
                  choice = Integer.valueOf(num);
                  // double val = 0;

                  // EXECUTION ACTIONS FOR ADD/SUBTRACT/DIVIDE/MULTIPLY
                  switch (choice) {
                    // ADDITION
                    case (1):
                      System.out.println("Addition");
                      val = client.getCalcInput(reader, valid, LIMIT);
                      // adds inputs to an array of doubles
                      inputs = client.getCalcArr(reader, valid, val);
                      // creates and sends calculation request to server
                      client.add(inputs);

                      break;

                    // SUBTRACTION
                    case (2):
                      System.out.println("Subtraction");
                      val = client.getCalcInput(reader, valid, LIMIT);
                      // adds inputs to an array of doubles
                      inputs = client.getCalcArr(reader, valid, val);
                      // creates and sends calculation request to server
                      client.subtract(inputs);
                      break;

                    case (3):
                      System.out.println("Multiplies");
                      // gets VAL(#) integers to calculate from user
                      val = client.getCalcInput(reader, valid, LIMIT);
                      // adds VAL inputs to array of doubles
                      inputs = client.getCalcArr(reader, valid, val);
                      // creates and sends calculation request to server
                      client.multiply(inputs);
                      break;

                    case (4):
                      System.out.println("Divides");
                      // gets VAL(#) integers to calculate from user
                      val = client.getCalcInput(reader, valid, LIMIT);
                      // adds VAL inputs to array of doubles
                      inputs = client.getCalcArr(reader, valid, val);
                      // creates and sends calculation request to server
                      client.divide(inputs);
                      break;

                    default:
                      System.out.println("Invalid entry");
                      valid = false;
                      break;
                  }
                } catch (NumberFormatException e) {
                  // Handles if user's input is not a numberical value
                  System.out.println("Incorrect input, please try again");
                }
              } while (!valid);
              break;

            case (3):
              do {
                valid = true;
                System.out.println("Please select option number: ");
                System.out.println("1. Read - Read last sentence in story");
                System.out.println("2. Write - Append a line to the story");
                num = reader.readLine();
                try {
                  choice = Integer.valueOf(num);
                } catch (NumberFormatException e) {
                  valid = false;
                  System.out.println("Invalid input, please try again");
                  continue;
                }
                switch (choice) {
                  case (1):
                    client.read();
                    break;
                  case (2):
                    String input = client.getWriteInput(reader, valid);
                    client.write(input);
                    break;
                  default:
                    System.err.println("Invalid input.  ");
                    valid = false;
                    break;
                }
              } while (!valid);
              continue;

            case (4):
              do {
                valid = true;
                System.out.println(
                    "You've entered the cat booth.  You are now alone in the room with a cat.  What would you like to do?");
                System.out.println("1. Get Name - Returns the name of the cat");
                System.out.println("2. Pet - Attempt to pet the cat");
                System.out.println("3. Offer Treat - Offers a treat to the cat");
                System.out.println("4. Peek - Check out what the cat is doing");
                System.out.println("5. Call - Try to beckon the cat");
                num = reader.readLine();
                try {
                  choice = Integer.valueOf(num);
                } catch (NumberFormatException e) {
                  valid = false;
                  System.out.println("Invalid input, please try again");
                  continue;
                }
                switch (choice) {
                  case (1):
                    catname = client.name();
                    continue;
                  case (2):
                    client.pet(catname);
                    break;
                  case (3):
                    int treat = client.pickTreat(reader);
                    client.giveTreat(treat, catname);
                    break;
                  case (4):
                    client.peek(catname);
                    break;
                  case (5):
                    client.call(catname);
                    break;
                  default:
                    valid = false;
                    System.err.println("Invalid input.  ");
                    break;
                }
              } while (!valid);
              break;

            case (5):
              System.out.println("Client exiting -- Goodbye!");
              System.exit(0);
            default:
              System.out.println("Invalid input, try again.");
              valid = false;
              continue;
          }
        } catch (NumberFormatException e) {
          // Handles if user's input is not a numberical value
          System.out.println("Incorrect input, please try again");
          valid = false;
        }
      } while (!valid || active);

      // ############### Contacting the registry just so you see how it can be done

      // Comment these last Service calls while in Activity 1 Task 1, they are not
      // needed and wil throw issues without the Registry running
      // get thread's services

      /*
       * client.getServices();
       * 
       * // get parrot
       * client.findServer("services.Echo/parrot");
       * 
       * // get all setJoke
       * client.findServers("services.Joke/setJoke");
       * 
       * // get getJoke
       * client.findServer("services.Joke/getJoke");
       * 
       * // does not exist
       * // client.findServer("random");
       */

    } finally {
      // ManagedChannels use resources like threads and TCP connections. To prevent
      // leaking these
      // resources the channel should be shut down when it will no longer be used. If
      // it may be used
      // again leave it running.
      channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
      regChannel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
    }

  }
}
