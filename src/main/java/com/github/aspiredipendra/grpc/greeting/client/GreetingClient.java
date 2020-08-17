package com.github.aspiredipendra.grpc.greeting.client;


import com.proto.dummy.DummyServiceGrpc;
import com.proto.greet.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreetingClient {

    public static void main(String[] args) {
        System.out.println("Hello I'm a gRPC client");
        GreetingClient main = new GreetingClient();
        main.run();


    }
    private void run() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext() //deactive SSL
                .build();
        System.out.println("Creating stub!");

        //old and dummy
        // DummyServiceGrpc.DummyServiceBlockingStub syncClient = DummyServiceGrpc.newBlockingStub(channel);


        //do something
       // doUnaryCall(channel);

        doServerStreamingCall(channel);

        System.out.println("Shutting Down channel!!");
        channel.shutdown();


    }

    private void doUnaryCall(ManagedChannel channel) {
        // created a greet service client (blocking - synchronous)
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);

        // Unary
        // created a protocol buffer greeting message
        Greeting greeting = Greeting.newBuilder()
                .setFirstName("dipendra")
                .setLastName("yadav")
                .build();

        // do the same for a GreetRequest
        GreetRequest greetRequest = GreetRequest.newBuilder()
                .setGreeting(greeting)
                .build();

        // call the RPC and get back a GreetResponse (protocol buffers)
        GreetResponse greetResponse = greetClient.greet(greetRequest);

        System.out.println(greetResponse.getResult());

    }

    private void doServerStreamingCall(ManagedChannel channel) {
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);

        // Server Streaming
        // we prepare the request
        GreetManyTimesRequest greetManyTimesRequest =
                GreetManyTimesRequest.newBuilder()
                        .setGreeting(Greeting.newBuilder().setFirstName("Dipendra"))
                        .build();

        // we stream the responses (in a blocking manner)
        greetClient.greetManyTimes(greetManyTimesRequest)
                .forEachRemaining(greetManyTimesResponse -> {
                    System.out.println(greetManyTimesResponse.getResult());
                });

    }
}


