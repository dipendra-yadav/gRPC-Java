package com.github.aspiredipendra.grpc.blog.client;

import com.proto.blog.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BlogClient {
    public static void main(String[] args) {
        System.out.println("Hello I'm a gRPC Blog client");
        BlogClient main = new BlogClient();
        main.run();
    }

    private void run() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        BlogServiceGrpc.BlogServiceBlockingStub blogClient = BlogServiceGrpc.newBlockingStub(channel);
        Blog blog = Blog.newBuilder()
                .setAuthorId("dipendra")
                .setTitle("latest blog")
                .setContent("this is updated blog")
                .build();


        //1.
        System.out.println("Creating Blog...");
        CreateBlogResponse createResponse = blogClient.createBlog(
                CreateBlogRequest.newBuilder()
                        .setBlog(blog)
                        .build()
        );
        System.out.println(createResponse.toString());




        //2.
        System.out.println("Reading blog....");
        String blogId=createResponse.getBlog().getId();
        ReadBlogResponse readBlogResponse = blogClient.readBlog(ReadBlogRequest.newBuilder()
                .setBlogId(blogId)
                .build());
        System.out.println(readBlogResponse.toString());

        //2.1
          System.out.println("Reading blog with non existence ID....");
             ReadBlogResponse readBlogResponse1 = blogClient.readBlog(ReadBlogRequest.newBuilder()
                     .setBlogId("fake-id")
                       .build());
             System.out.println(readBlogResponse.toString());


        //3.
        System.out.println("Updating blog ....");
        Blog newBlog = Blog.newBuilder()
                .setId(blogId)
                .setAuthorId("Changed Author")
                .setTitle("New blog (updated)!")
                .setContent("I've added some more content")
                .build();


        UpdateBlogResponse updateBlogResponse = blogClient.updateBlog(
                UpdateBlogRequest.newBuilder().setBlog(newBlog).build());
        System.out.println(updateBlogResponse.toString());




        //4.
        System.out.println("Deleting blog");
        DeleteBlogResponse deleteBlogResponse = blogClient.deleteBlog(
                DeleteBlogRequest.newBuilder().setBlogId(blogId).build()
        );


        //5.we list the blogs in our database
        blogClient.listBlog(ListBlogRequest.newBuilder().build()).forEachRemaining(
                listBlogResponse -> System.out.println(listBlogResponse.getBlog().toString())
        );




    }
}
