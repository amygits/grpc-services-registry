package example.grpcclient;

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

import buffers.RequestProtos.Request;
import buffers.RequestProtos.Request.RequestType;
import buffers.ResponseProtos.Response;

import java.io.FileWriter;
import java.io.FileReader;
import org.json.simple.parser.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class StoryImpl extends StoryGrpc.StoryImplBase {

    // List of sentences comprising the story

    private JSONArray storyList = new JSONArray();
    private JSONObject storyObj;
    private Stack<String> senBox = new Stack();

    public StoryImpl() {
        super();
        JSONParser parser = new JSONParser();

        try {
            FileReader reader = new FileReader("story.json");
            Object obj = parser.parse(reader);
            storyList = (JSONArray) obj;

            System.out.println("The story so far: ");
            System.out.println(storyList);
            storyList.forEach(sent -> parseSentences((String) sent));

        } catch (FileNotFoundException e) {
            // File not found or does not exist
            // Create initial file
            System.out.println("FILE NOT FOUND, CREATING NEW STORY");
            JSONArray storyProg = new JSONArray();
            storyProg.add("-STORY START-");
            try (FileWriter file = new FileWriter("story.json")) {
                file.write(storyProg.toString());
                file.flush();
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
    }

    // Adds each sentence read from the JSONArray to sentBox stack
    private void parseSentences(String sent) {
        senBox.push(sent);
        System.out.println("Peeking: " + senBox.peek());
    }

    @Override
    public synchronized void write(WriteRequest req, StreamObserver<WriteResponse> responseObserver) {
        System.out.println("Received from client: " + req.getNewSentence());
        storyList.add(req.getNewSentence());
        parseSentences(req.getNewSentence());
        WriteResponse.Builder response = WriteResponse.newBuilder();
        String newStory = storyList.toString();

        try {
            response.setStory(newStory);
            response.setIsSuccess(true);
            try (FileWriter file = new FileWriter("story.json")) {
                // We can write any JSONArray or JSONObject instance to the file
                file.write(storyList.toString());
                file.flush();

            } catch (IOException e) {
                response.setError("Something went wrong while writing");
            }
        } catch (Exception e) {
            response.setError("Something went wrong while writing");
        }

        WriteResponse resp = response.build();
        responseObserver.onNext(resp);
        responseObserver.onCompleted();

    }

    @Override
    public synchronized void read(Empty req, StreamObserver<ReadResponse> responseObserver) {
        System.out.println("Received from client: Read request");

        ReadResponse.Builder response = ReadResponse.newBuilder();

        try {
            response.setSentence(senBox.peek());
            response.setIsSuccess(true);
        } catch (Exception e) {
            response.setError("Something went wrong while reading");
        }

        ReadResponse resp = response.build();
        responseObserver.onNext(resp);
        responseObserver.onCompleted();

    }

}
