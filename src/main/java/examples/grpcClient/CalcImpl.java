package example.grpcclient;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerMethodDefinition;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import service.*;
import java.util.Stack;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;

import buffers.RequestProtos.Request;
import buffers.RequestProtos.Request.RequestType;
import buffers.ResponseProtos.Response;

/* Implements the Calc service.  It has 4 services:
*   add
*   subtract
*   multiply
*   divide
*/
public class CalcImpl extends CalcGrpc.CalcImplBase {

    public CalcImpl() {
        super();
    }

    // Implements ADD service and sends back response
    @Override
    public void add(CalcRequest req, StreamObserver<CalcResponse> responseObserver) {
        System.out.println("Received from client: ");
        double sum = 0;
        CalcResponse.Builder response = CalcResponse.newBuilder();

        try {
            for (double input : req.getNumList()) {
                System.out.print(input + " + ");
                sum += input;
            }
            response.setSolution(sum);
            response.setIsSuccess(true);
        } catch (Exception e) {
            response.setError("Something went wrong while adding");
        }

        CalcResponse resp = response.build();
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }

    // Subtract service and sends back response
    @Override
    public void subtract(CalcRequest req, StreamObserver<CalcResponse> responseObserver) {
        System.out.println("Received from client: ");
        double sum;
        Queue<Double> q = new LinkedList<>();
        CalcResponse.Builder response = CalcResponse.newBuilder();

        try {
            for (double input : req.getNumList()) {
                System.out.println(input);
                q.add(input);
            }
            sum = q.poll();
            while (!q.isEmpty()) {
                sum -= q.poll();
            }
            response.setSolution(sum);
            response.setIsSuccess(true);
        } catch (Exception e) {
            response.setError("Something went wrong while subtracting");
        }

        CalcResponse resp = response.build();
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }

    // Multiply service and sends back response
    @Override
    public void multiply(CalcRequest req, StreamObserver<CalcResponse> responseObserver) {
        System.out.println("Received from client: ");
        double product;
        Queue<Double> q = new LinkedList<>();
        CalcResponse.Builder response = CalcResponse.newBuilder();

        try {
            for (double input : req.getNumList()) {
                System.out.println(input);
                q.add(input);
            }
            product = q.poll();
            while (!q.isEmpty()) {
                product *= q.poll();
            }
            response.setSolution(product);
            response.setIsSuccess(true);
        } catch (Exception e) {
            response.setError("Something went wrong while multiplying");
        }

        CalcResponse resp = response.build();
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }

    // Divide service and sends back response
    @Override
    public void divide(CalcRequest req, StreamObserver<CalcResponse> responseObserver) {
        System.out.println("Received from client: ");
        double quotient;
        Queue<Double> q = new LinkedList<>();
        CalcResponse.Builder response = CalcResponse.newBuilder();

        try {
            for (double input : req.getNumList()) {
                System.out.println(input);
                q.add(input);
            }
            double numer = q.poll();
            double denom = 0;
            while (!q.isEmpty()) {
                denom += q.poll();
            }

            quotient = numer / denom;
            response.setSolution(quotient);
            response.setIsSuccess(true);
        } catch (Exception e) {
            response.setError("Something went wrong while dividing");
        }

        CalcResponse resp = response.build();
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }
}
