package example.grpcclient;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import service.*;
import test.TestProtobuf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.simple.parser.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Client that requests `parrot` method from the `EchoServer`.
 */
public class Client {
    private EchoGrpc.EchoBlockingStub blockingStub;
    private JokeGrpc.JokeBlockingStub blockingStub2;
    private RegistryGrpc.RegistryBlockingStub blockingStub3;
    private CalcGrpc.CalcBlockingStub blockingStub4;
    private StoryGrpc.StoryBlockingStub blockingStub5;
    private KittyGrpc.KittyBlockingStub blockingStub6;

    public Client(Channel regChannel) {
        blockingStub = EchoGrpc.newBlockingStub(regChannel);
        blockingStub2 = JokeGrpc.newBlockingStub(regChannel);
        blockingStub3 = RegistryGrpc.newBlockingStub(regChannel);
        blockingStub4 = CalcGrpc.newBlockingStub(regChannel);
        blockingStub5 = StoryGrpc.newBlockingStub(regChannel);
        blockingStub6 = KittyGrpc.newBlockingStub(regChannel);
    }

    /** Construct client for accessing server using the existing channel. */
    public Client(Channel channel, Channel regChannel) {
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

    public void setChannel(Channel channel) {
        blockingStub = EchoGrpc.newBlockingStub(channel);
        blockingStub2 = JokeGrpc.newBlockingStub(channel);
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
            System.out.println("Sorry the registry server is not currently available.");
            System.err.println("Error: " + e + "\n");
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
            System.out.println("Your joke has been added: " + response.getOk());
        } catch (Exception e) {
            System.out.println("Sorry the registry server is not currently available.");
            System.err.println("Error: " + e + "\n");
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
            System.out.println("Sorry the registry server is not currently available.");
            System.err.println("Error: " + e + "\n");
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
            System.out.println("Sorry the registry server is not currently available.");
            System.err.println("Error: " + e + "\n");
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
            System.out.println("Sorry the registry server is not currently available.");
            System.err.println("Error: " + e + "\n");
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
            System.out.println("Sorry the registry server is not currently available.");
            System.err.println("Error: " + e + "\n");
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
            System.out.println("Sorry the registry server is not currently available.");
            System.err.println("Error: " + e + "\n");
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
            System.out.println("Sorry the registry server is not currently available.");
            System.err.println("Error: " + e + "\n");
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
            System.out.println("Sorry the registry server is not currently available.");
            System.err.println("Error: " + e + "\n");
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
            System.out.println("Sorry the registry server is not currently available.");
            System.err.println("Error: " + e + "\n");
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
            System.out.println("Sorry the registry server is not currently available.");
            System.err.println("Error: " + e + "\n");
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
            System.out.println("Sorry the registry server is not currently available.");
            System.err.println("Error: " + e + "\n");
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
            System.out.println("Sorry the registry server is not currently available.");
            System.err.println("Error: " + e + "\n");
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
            System.out.println("Sorry the registry server is not currently available.");
            System.err.println("Error: " + e + "\n");
            return;
        }
    }

    public String findServer(String name) {

        FindServerReq request = FindServerReq.newBuilder().setServiceName(name).build();
        SingleServerRes response;
        try {
            response = blockingStub3.findServer(request);
            // System.out.println(response.toString());
            return response.toString();
        } catch (Exception e) {
            System.out.println("Sorry the registry server is not currently available.");
            System.err.println("Error: " + e + "\n");
            return null;
        }
    }

    public void findServers(String name) {
        FindServersReq request = FindServersReq.newBuilder().setServiceName(name).build();
        ServerListRes response;
        try {
            response = blockingStub3.findServers(request);
            System.out.println(response.toString());
        } catch (Exception e) {
            System.out.println("Sorry the registry server is not currently available.");
            System.err.println("Error: " + e + "\n");
            return;
        }
    }

    public String getUserJoke(BufferedReader reader) {
        String joke = "I made an eraser out of two pencils.  It was pointless.";
        boolean valid = false;
        do {
            valid = true;
            try {
                System.out.println("Please enter your joke:");
                joke = reader.readLine();
            } catch (IOException e) {
                System.out.println("Invalid input, please try again");
                valid = false;
            }
        } while (!valid);

        return joke;
    }

    public int howManyJokes(BufferedReader reader) {
        int choice = 0;
        boolean valid = false;
        do {
            valid = true;
            try {
                System.out.println("Please enter how many jokes you'd like:");
                String num = reader.readLine();
                choice = Integer.valueOf(num);
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

    public int getUserService(BufferedReader reader) {
        boolean valid = false;

        int choice = 0;
        do {
            valid = true;
            System.out.println("Please enter the number of the available service you'd like to use:");
            System.out.println(
                    "1. Parrot\n2. Set Joke\n3. Get Joke\n4. Subtract\n5. Divide\n6. Add\n7. Multiply\n8. Read\n9. Write\n10. Pet\n11. Call\n12. Get Name\n13. Give Treat\n14. Peek\n0. Exit");
            try {
                String num = reader.readLine();
                choice = Integer.valueOf(num);
            } catch (Exception e) {
                valid = false;
                System.out.println("Invalid input, please try again.");
            }

        } while (!valid);

        return choice;

    }

    public String getTarget(String data) {
        // System.out.println(data);
        Pattern pat = Pattern.compile("\\{.*?\\}");
        Matcher matcher = pat.matcher(data);
        if (matcher.find()) {
            data = matcher.group();
        }
        String splitData[] = data.split("\\{| \\}| \\n| \\s*");
        // System.out.println("size: " + splitData.length);
        /*
         * System.out.println("index of \"uri\":" + data.indexOf("uri"));
         * System.out.println("index of \"localhost\":" +
         * data.indexOf("\"localhost\""));
         */

        /*
         * for (int i = 0; i < splitData.length - 1; i++){
         * System.out.println(splitData[4]);
         * }
         */
        // uri - 4/4
        // port - 9/10
        // System.out.println(splitData[3]);

        String host = splitData[5].trim();
        String[] hostArr = host.split("\"");
        // System.out.println("size: " + hostArr.length);
        // System.out.println(hostArr[1]);
        host = hostArr[1];
        int port = Integer.parseInt(splitData[9].trim());
        // System.out.println("URI: " + host + "\nPort: " + port);
        String target = host + ":" + port;
        target.trim();
        // System.out.println("Target: " + target);
        return target;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.out
                    .println(
                            "Expected arguments: <regHost(string)> <regPort(int)> <message(String)>");
            System.exit(1);
        }
        int port = 0000;
        int regPort = 9003;
        String host = "";
        String regHost = args[0];
        String message = args[2];
        try {
            // port = Integer.parseInt(args[1]);
            regPort = Integer.parseInt(args[1]);
        } catch (NumberFormatException nfe) {
            System.out.println("[Port] must be an integer");
            System.exit(2);
        }

        // Create a communication channel to the server, known as a Channel. Channels
        // are thread-safe
        // and reusable. It is common to create channels at the beginning of your
        // application and reuse
        // them until the application shuts down.

        String regTarget = regHost + ":" + regPort;
        ManagedChannel regChannel = ManagedChannelBuilder.forTarget(regTarget).usePlaintext().build();

        // Just doing some hard coded calls to the service node without using the
        // registry
        ManagedChannel channel;

        boolean valid = false;
        String num = "";
        int choice = 0;
        final int LIMIT = 25;
        double[] inputs;
        int val = 0;
        boolean active = true;
        String catname = "The unknown cat";
        JSONParser parser = new JSONParser();
        JSONObject servObj = new JSONObject();
        String data, target;

        do {
            try {
                // create client
                Client client = new Client(regChannel);

                // Create reader to read user input
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

                do {
                    System.out.println("");
                    System.out.println("List of services: ");
                    client.getServices();
                    choice = client.getUserService(reader);
                    switch (choice) {

                        // Exit case
                        case (0):
                            System.out.println("Exiting client - Goodbye!");
                            System.exit(0);

                            // Parrot
                        case (1):
                            data = client.findServer("services.Echo/parrot");
                            target = client.getTarget(data);
                            channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
                            client.setChannel(channel);
                            client.askServerToParrot(message);
                            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
                            break;

                        // Set Joke
                        case (2):
                            data = client.findServer("services.Joke/setJoke");
                            target = client.getTarget(data);
                            channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
                            client.setChannel(channel);
                            String joke = client.getUserJoke(reader);
                            client.setJoke(joke);
                            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
                            break;

                        // Get Joke
                        case (3):
                            data = client.findServer("services.Joke/getJoke");
                            target = client.getTarget(data);
                            channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
                            client.setChannel(channel);
                            choice = client.howManyJokes(reader);
                            client.askForJokes(choice);
                            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
                            break;

                        // Subtract
                        case (4):
                            data = client.findServer("services.Calc/subtract");
                            target = client.getTarget(data);
                            channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
                            client.setChannel(channel);
                            choice = client.getCalcInput(reader, valid, LIMIT);
                            client.subtract(client.getCalcArr(reader, valid, choice));
                            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
                            break;

                        // Divide
                        case (5):
                            data = client.findServer("services.Calc/divide");
                            target = client.getTarget(data);
                            channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
                            client.setChannel(channel);
                            choice = client.getCalcInput(reader, valid, LIMIT);
                            client.divide(client.getCalcArr(reader, valid, choice));
                            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
                            break;

                        // Add
                        case (6):
                            data = client.findServer("services.Calc/add");
                            target = client.getTarget(data);
                            channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
                            client.setChannel(channel);
                            choice = client.getCalcInput(reader, valid, LIMIT);
                            client.add(client.getCalcArr(reader, valid, choice));
                            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
                            break;

                        // Multiply
                        case (7):
                            data = client.findServer("services.Calc/add");
                            target = client.getTarget(data);
                            channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
                            client.setChannel(channel);
                            choice = client.getCalcInput(reader, valid, LIMIT);
                            client.multiply(client.getCalcArr(reader, valid, choice));
                            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
                            break;

                        // Read
                        case (8):
                            data = client.findServer("services.Story/read");
                            target = client.getTarget(data);
                            channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
                            client.setChannel(channel);
                            client.read();
                            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
                            break;

                        // Write
                        case (9):
                            data = client.findServer("services.Story/write");
                            target = client.getTarget(data);
                            channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
                            client.setChannel(channel);
                            client.write(client.getWriteInput(reader, valid));
                            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
                            break;

                        // Pet
                        case (10):
                            data = client.findServer("services.Kitty/pet");
                            target = client.getTarget(data);
                            channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
                            client.setChannel(channel);
                            client.pet(catname);
                            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
                            break;

                        // Call
                        case (11):
                            data = client.findServer("services.Kitty/call");
                            target = client.getTarget(data);
                            channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
                            client.setChannel(channel);
                            client.call(catname);
                            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
                            break;

                        // Get name
                        case (12):
                            data = client.findServer("services.Kitty/getName");
                            target = client.getTarget(data);
                            channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
                            client.setChannel(channel);
                            catname = client.name();
                            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
                            break;

                        // Give Treat
                        case (13):
                            data = client.findServer("services.Kitty/giveTreat");
                            target = client.getTarget(data);
                            channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
                            client.setChannel(channel);
                            int treat = client.pickTreat(reader);
                            client.giveTreat(treat, catname);
                            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
                            break;

                        // Peek
                        case (14):
                            data = client.findServer("services.Kitty/peek");
                            target = client.getTarget(data);
                            channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
                            client.setChannel(channel);
                            client.peek(catname);
                            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
                            break;

                        default:
                            System.out.println("Invalid input, please try again");
                            valid = false;
                            break;
                    }

                } while (!valid);

            } catch (Exception e) {
                System.out.println("There was an error connecting to the server.  Please try again later .");
                //e.printStackTrace();
                valid = false;
            } finally {
                // ManagedChannels use resources like threads and TCP connections. To prevent
                // leaking these
                // resources the channel should be shut down when it will no longer be used. If
                // it may be used
                // again leave it running.
                regChannel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
            }

        } while (!valid);
    }
}
