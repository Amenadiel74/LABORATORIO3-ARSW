package edu.eci.arsw.review;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.List;

public class ReviewServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(50052)
            .addService(new ReviewServiceImpl())
            .build();
        server.start();
        System.out.println("Review gRPC Server iniciado en puerto 50052");
        server.awaitTermination();
    }

    static class ReviewServiceImpl extends ReviewServiceGrpc.ReviewServiceImplBase {
        @Override
        public void getReviews(ReviewRequest request, StreamObserver<ReviewList> responseObserver) {
            List<Review> reviews = new ArrayList<>();
            if (request.getMovieId() == 1) {
                reviews.add(Review.newBuilder().setAuthor("User1").setComment("Excelente ciencia ficción").setRating(5).build());
                reviews.add(Review.newBuilder().setAuthor("User2").setComment("Visualmente impresionante").setRating(4).build());
            } else if (request.getMovieId() == 2) {
                reviews.add(Review.newBuilder().setAuthor("User3").setComment("Clásico del cine").setRating(5).build());
            }

            ReviewList list = ReviewList.newBuilder().addAllReviews(reviews).build();
            responseObserver.onNext(list);
            responseObserver.onCompleted();
        }
    }
}
