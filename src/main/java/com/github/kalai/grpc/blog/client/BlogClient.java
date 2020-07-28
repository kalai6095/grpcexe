package com.github.kalai.grpc.blog.client;

import com.proto.blog.*;
import com.proto.greet.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class BlogClient {


    public void run(String hostname, Integer port) {

        System.out.println(hostname + "------------------------" + port);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(hostname, port)
                .usePlaintext()
                .build();

        createBlog(channel);


        channel.shutdown();


    }

    private void readBlog(ManagedChannel channel, String blogId) {
        BlogServiceGrpc.BlogServiceBlockingStub blogClient = BlogServiceGrpc.newBlockingStub(channel);
        ReadBlogResponse response = blogClient
                .readBlog(ReadBlogRequest.newBuilder()
                        .setBlogId(blogId).build());

        System.out.println(response.toString());
    }

    private void createBlog(ManagedChannel channel) {
        BlogServiceGrpc.BlogServiceBlockingStub blogClient = BlogServiceGrpc.newBlockingStub(channel);
        Blog blog = Blog.newBuilder()
                .setAuthor("Kalaiselvan asdfasdf")
                .setTitle("gRPC Impl adsfasdfadsf")
                .setContent("Monitor thread successfully connected to server with description ServerDescription")
                .build();

        CreateBlogResponse response = blogClient.createBlog(CreateBlogRequest.newBuilder().setBlog(blog).build());
        System.out.println(response.toString());
        System.out.println("====================read======================");
        readBlog(channel, "5f1fa5422f354a0a5c39a2da");

        System.out.println("====================read== Invalid blog====================");
        readBlog(channel, "abcd");
    }


    public static void main(String[] args) {
        Integer port = 0;
        String hostname = "";
        if (args.length >= 1) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ex) {
                System.err.println("Usage: [port [hostname]]");
                System.err.println("");
                System.err.println("  port      The listen port. Defaults to " + port);
                System.err.println("  hostname  The name clients will see in greet responses. ");
                System.err.println("            Defaults to the machine's hostname");
                System.exit(1);
            }
        }
        if (args.length >= 2) {
            hostname = args[1];
        }

        System.out.println("Hello i am grpc client");
        // create a channel

        System.out.println("Createing stub");
        BlogClient main = new BlogClient();
        main.run(hostname, port);
        //DummyServiceGrpc.DummyServiceBlockingStub syncClient = DummyServiceGrpc.newBlockingStub(managedChannel);
        //DummyServiceGrpc.DummyServiceFutureStub asyncClient = DummyServiceGrpc.newFutureStub(managedChannel);


    }
}
