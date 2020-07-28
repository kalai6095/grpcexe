package com.github.kalai.grpc.blog.server;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.proto.blog.*;
import com.proto.greet.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

public class BlogServiceImpl extends BlogServiceGrpc.BlogServiceImplBase {
    private MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
    private MongoDatabase mongoDatabase = mongoClient.getDatabase("grpc_testing");
    private MongoCollection<Document> collection = mongoDatabase.getCollection("blog");

    @Override
    public void createBlog(CreateBlogRequest request, StreamObserver<CreateBlogResponse> responseObserver) {
        //super.createBlog(request, responseObserver);
        Blog blog = request.getBlog();
        Document doc = new Document("author_id", blog.getAuthor())
                .append("title", blog.getTitle())
                .append("content", blog.getContent());
        collection.insertOne(doc);
        System.out.println("data inserted !.............................");
        String id = doc.getObjectId("_id").toString();
        CreateBlogResponse response = CreateBlogResponse.newBuilder()
                .setBlog(Blog.newBuilder()
                        .setAuthor(blog.getAuthor())
                        .setTitle(blog.getTitle())
                        .setContent(blog.getContent())
                        .setId(id)
                ).build();
        responseObserver.onNext(response);
        System.out.println("send the response data to client");
        responseObserver.onCompleted();
    }

    @Override
    public void readBlog(ReadBlogRequest request, StreamObserver<ReadBlogResponse> responseObserver) {
        // super.readBlog(request, responseObserver);

        String blogId = request.getBlogId();
        Document document = null;
        try {
            document = collection.find(eq("_id", new ObjectId(blogId))).first();
        } catch (Exception e) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("")
                    .augmentDescription(e.getLocalizedMessage())
                    .asRuntimeException());
        }

        if (document == null) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("")
                    .asRuntimeException());
        } else {
            System.out.println(document.toString());
            Blog blog = Blog.newBuilder()
                    .setAuthor(document.getString("author_id"))
                    .setTitle(document.getString("title"))
                    .setContent(document.getString("content"))
                    .setId(blogId).build();


            responseObserver.onNext(ReadBlogResponse.newBuilder().setBlog(blog).build());
            responseObserver.onCompleted();
        }
    }
}
