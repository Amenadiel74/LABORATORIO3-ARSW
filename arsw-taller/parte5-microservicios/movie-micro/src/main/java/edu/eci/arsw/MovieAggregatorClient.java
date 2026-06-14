package edu.eci.arsw;

import edu.eci.arsw.movie.*;
import edu.eci.arsw.review.*;
import edu.eci.arsw.recommendation.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class MovieAggregatorClient {
    public static void main(String[] args) {
        ManagedChannel movieChannel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();
        ManagedChannel reviewChannel = ManagedChannelBuilder.forAddress("localhost", 50052).usePlaintext().build();
        ManagedChannel recommendationChannel = ManagedChannelBuilder.forAddress("localhost", 50053).usePlaintext().build();

        MovieServiceGrpc.MovieServiceBlockingStub movieStub = MovieServiceGrpc.newBlockingStub(movieChannel);
        ReviewServiceGrpc.ReviewServiceBlockingStub reviewStub = ReviewServiceGrpc.newBlockingStub(reviewChannel);
        RecommendationServiceGrpc.RecommendationServiceBlockingStub recStub = RecommendationServiceGrpc.newBlockingStub(recommendationChannel);

        int movieId = 1;
        
        MovieResponse movie = movieStub.getMovie(MovieRequest.newBuilder().setId(movieId).build());
        ReviewList reviews = reviewStub.getReviews(ReviewRequest.newBuilder().setMovieId(movieId).build());
        RecommendationList recommendations = recStub.getRecommendations(RecommendationRequest.newBuilder().setMovieId(movieId).build());

        System.out.println("Película: " + movie.getTitle());
        System.out.println("Director: " + movie.getDirector());
        System.out.println("Año: " + movie.getYear());
        System.out.println("Reseñas:");
        for (Review r : reviews.getReviewsList()) {
            System.out.println("  - " + r.getComment() + ". Rating: " + r.getRating());
        }
        System.out.println("Recomendaciones:");
        for (String t : recommendations.getTitlesList()) {
            System.out.println("  - " + t);
        }

        movieChannel.shutdown();
        reviewChannel.shutdown();
        recommendationChannel.shutdown();
    }
}
