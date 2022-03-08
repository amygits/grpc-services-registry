package example.grpcclient;

import java.util.Random;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerMethodDefinition;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import service.*;
import java.util.Stack;
import java.util.LinkedList;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.util.List;

import buffers.RequestProtos.Request;
import buffers.RequestProtos.Request.RequestType;
import buffers.ResponseProtos.Response;

import java.io.FileWriter;
import java.io.FileReader;
import org.json.simple.parser.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class KittyImpl extends KittyGrpc.KittyImplBase {

    private JSONArray catList = new JSONArray();
    private JSONObject catOb = new JSONObject();
    private List<catObj> catList2 = new ArrayList();
    private List<catRespObj> peekList = new ArrayList();
    private List<catRespObj> patList = new ArrayList();
    private List<catRespObj> yesTreatList = new ArrayList();
    private List<catRespObj> noTreatList = new ArrayList();
    private List<catRespObj> callList = new ArrayList();
    catObj activeCat;

    public KittyImpl() {
        super();
        JSONParser parser = new JSONParser();
        boolean valid = false;
        Random randCat = new Random();

        do {
            valid = true;
            try {
                FileReader reader = new FileReader("cat.json");
                Object obj = parser.parse(reader);
                catList = (JSONArray) obj;
                // System.out.println(catList);
                catList.forEach(a -> parseObj((JSONObject) a));
                activeCat = catList2.get(randCat.nextInt(catList2.size()));

            } catch (FileNotFoundException e) {
                // File not found or does not exist
                // Create initial file
                System.out.println("File not found, initializing new cat list");
                try (FileWriter file = new FileWriter("cat.json")) {
                    catList = init();
                    file.write(catList.toString());
                    file.flush();
                    valid = false;
                } catch (IOException p) {
                    System.out.println("Error writing JSON file. (IO)");
                }
            } catch (IOException e) {
                System.out.println("Error reading JSON file (IO)");
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("Error reading JSON file (Parse Error)");
                e.printStackTrace();
            }
        } while (!valid);
    }

    private void parseObj(JSONObject obj) {
        if (obj.containsKey("cat")) {
            JSONObject catObject = (JSONObject) obj.get("cat");
            String catName = (String) catObject.get("name");
            long treatVal = (long) catObject.get("faveTreat");
            catList2.add(new catObj(catName, (int) treatVal));
            // System.out.println("added to cat list");
        }
        if (obj.containsKey("response")) {
            JSONObject responseObj = (JSONObject) obj.get("response");
            // respList.add(new catRespObj((String)responseObj.get("data"),
            // (String)responseObj.get("type")));

            if (responseObj.get("type").equals("peek")) {
                peekList.add(new catRespObj((String) responseObj.get("data"), (String) responseObj.get("type")));
            }
            if (responseObj.get("type").equals("pat")) {
                patList.add(new catRespObj((String) responseObj.get("data"), (String) responseObj.get("type")));
            }
            if (responseObj.get("type").equals("treat1")) {
                yesTreatList.add(new catRespObj((String) responseObj.get("data"), (String) responseObj.get("type")));
            }
            if (responseObj.get("type").equals("treat2")) {
                noTreatList.add(new catRespObj((String) responseObj.get("data"), (String) responseObj.get("type")));
            }
            if (responseObj.get("type").equals("call")) {
                callList.add(new catRespObj((String) responseObj.get("data"), (String) responseObj.get("type")));
            }
            // System.out.println("added to response list");
        }

       // System.out.println("Parsing from cat file completed.");
    }

    private JSONArray init() {
        JSONArray list = new JSONArray();
        list.add(addCat("Wumberly", 1));
        list.add(addCat("Meriweather", 1));
        list.add(addCat("Kingpin", 2));
        list.add(addCat("Bubby", 2));
        list.add(addCat("Chonky-Cheese", 1));
        list.add(addCat("Rebecca", 2));
        list.add(addCat("Ziggy", 1));
        list.add(addCat("Beavis", 2));
        list.add(addCat("Flip-Flop", 1));
        list.add(addCat("Elmonda", 2));
        // Peek responses
        list.add(addResp("stares back at you.", "peek"));
        list.add(addResp("is sound asleep.", "peek"));
        list.add(addResp("is fixated on something you can't see.", "peek"));
        list.add(addResp("is cleaning themself.", "peek"));
        list.add(addResp("meows at you when you make eye contact.", "peek"));
        list.add(addResp("looks like they might be hungry.", "peek"));
        list.add(addResp("is playing with a toy.", "peek"));
        // Treat responses
        list.add(addResp(
                "meows at you impatiently when they see the treat.  They eat it quickly when you place it on the floor.",
                "treat1"));
        list.add(addResp(
                "runs over to you and stands up on your legs to sniff the treat.  They eat it out of your hand as soon as it's within range.",
                "treat1"));
        list.add(addResp("purrs as they gently take the treat from you.", "treat1"));
        list.add(addResp("takes the treat out of your hand and goes off to eat it in solitude.", "treat1"));
        list.add(addResp("looks at the treat disinterestedly and walks in another direction.", "treat2"));
        list.add(addResp("sniffs the treat and looks the other way.", "treat2"));
        list.add(addResp(
                "sniffs the treat and tries to swat it out of your hand.  When you put the treat on the floor, they sniff it and walk away.",
                "treat2"));
        list.add(addResp("hisses at you.", "treat2"));
        // Pat Reponses
        list.add(addResp("hisses at you.  You decide not to pet them for now.", "pat"));
        list.add(addResp("swats at your hand playfully.", "pat"));
        list.add(addResp("purrs and nuzzles your hand.  They seem to enjoy it!", "pat"));
        list.add(addResp("meows at you.  You take it as a sign of approval.", "pat"));
        list.add(addResp("walks away before you're able to pet them.", "pat"));
        // Call responses
        list.add(addResp("meows in response but doesn't look at you.", "call"));
        list.add(addResp("does not respond.  Their ears don't even twitch.", "call"));
        list.add(addResp("looks over at you for a moment then turns back to the window.", "call"));
        list.add(addResp("starts walking towards you.", "call"));

        return list;
    }

    private JSONObject addResp(String resp, String type) {
        JSONObject respObj = new JSONObject();
        JSONObject respDeets = new JSONObject();
        respDeets.put("type", type);
        respDeets.put("data", resp);
        respObj.put("response", respDeets);
        return respObj;
    }

    private JSONObject addCat(String name, long faveTreat) {
        JSONObject cat = new JSONObject();
        JSONObject catDeets = new JSONObject();
        catDeets.put("name", name);
        catDeets.put("faveTreat", (int) faveTreat);
        cat.put("cat", catDeets);
        return cat;
    }

    @Override
    public void getName(None req, StreamObserver<Name> responseObserver) {
        System.out.println("Received from client: request for Cat Name");

        Name.Builder response = Name.newBuilder();
        try {
            response.setName(activeCat.getName());
        } catch (Exception e) {
            response.setError("Something went wrong while sending the name");
        }

        Name resp = response.build();
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }

    @Override
    public void pet(None req, StreamObserver<PetResp> responseObserver) {
        System.out.println("Received from client: Pet request");
        Random randPat = new Random();
        PetResp.Builder response = PetResp.newBuilder();
        try {
            String patresp = patList.get(randPat.nextInt(patList.size())).getResp();
            response.setResponse(patresp);
        } catch (Exception e) {
            response.setResponse("An error occurred");
        }

        PetResp resp = response.build();
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }

    @Override
    public void giveTreat(TreatReq req, StreamObserver<TreatResp> responseObserver) {
        System.out.println("Received from client: Treat offering");
        Random randTreat = new Random();
        TreatResp.Builder response = TreatResp.newBuilder();
        String treatresp;
        try {
            if ((int) req.getQuality() == activeCat.getFaveTreat()) {
                treatresp = yesTreatList.get(randTreat.nextInt(yesTreatList.size())).getResp();
                response.setResponse(treatresp);
            } else {
                treatresp = noTreatList.get(randTreat.nextInt(noTreatList.size())).getResp();
                response.setResponse(treatresp);
            }
        } catch (Exception e) {
            response.setResponse("An error occurred");
        }

        TreatResp resp = response.build();
        responseObserver.onNext(resp);
        responseObserver.onCompleted();

    }

    @Override
    public void peek(None req, StreamObserver<PeekResp> responseObserver) {
        System.out.println("Received from client: Peek request");
        Random randPeek = new Random();
        PeekResp.Builder response = PeekResp.newBuilder();
        try {
            String peekresp = peekList.get(randPeek.nextInt(peekList.size())).getResp();
            response.setResponse(peekresp);
        } catch (Exception e) {
            response.setResponse("An error occurred");
        }

        PeekResp resp = response.build();
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }

    @Override
    public void call(None req, StreamObserver<CallResp> responseObserver) {
        System.out.println("Received from client: Peek request");
        Random randCall = new Random();
        CallResp.Builder response = CallResp.newBuilder();
        try {
            String callresp = callList.get(randCall.nextInt(callList.size())).getResp();
            response.setResponse(callresp);
        } catch (Exception e) {
            response.setResponse("An error occurred");
        }

        CallResp resp = response.build();
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }


}
