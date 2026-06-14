package edu.eci.arsw.gateway;

import edu.eci.arsw.movie.*;
import edu.eci.arsw.review.*;
import edu.eci.arsw.recommendation.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class MovieGateway {
    public static void main(String[] args) {
        // 1. Crear canales hacia cada microservicio
        ManagedChannel movieChannel = ManagedChannelBuilder
            .forAddress("localhost", 50051).usePlaintext().build();
        ManagedChannel reviewChannel = ManagedChannelBuilder
            .forAddress("localhost", 50052).usePlaintext().build();
        ManagedChannel recommendationChannel = ManagedChannelBuilder
            .forAddress("localhost", 50053).usePlaintext().build();

        // 2. Crear stubs
        MovieServiceGrpc.MovieServiceBlockingStub movieStub =
            MovieServiceGrpc.newBlockingStub(movieChannel);
        ReviewServiceGrpc.ReviewServiceBlockingStub reviewStub =
            ReviewServiceGrpc.newBlockingStub(reviewChannel);
        RecommendationServiceGrpc.RecommendationServiceBlockingStub recStub =
            RecommendationServiceGrpc.newBlockingStub(recommendationChannel);

        // 3. Consultar servicios internos para película ID=1
        int movieId = 1;
        MovieResponse movie = movieStub.getMovie(
            MovieRequest.newBuilder().setId(movieId).build());
        ReviewList reviews = reviewStub.getReviews(
            ReviewRequest.newBuilder().setMovieId(movieId).build());
        RecommendationList recommendations = recStub.getRecommendations(
            RecommendationRequest.newBuilder().setMovieId(movieId).build());

        // 4. Construir respuesta integrada
        System.out.println("=== Gateway Response ===");
        System.out.println("Película: " + movie.getTitle());
        System.out.println("Director: " + movie.getDirector());
        System.out.println("Año: " + movie.getYear());
        System.out.println("Reseñas:");
        reviews.getReviewsList().forEach(r ->
            System.out.println("  - " + r.getComment() + ". Rating: " + r.getRating()));
        System.out.println("Recomendaciones:");
        recommendations.getTitlesList().forEach(t ->
            System.out.println("  - " + t));

        movieChannel.shutdown();
        reviewChannel.shutdown();
        recommendationChannel.shutdown();
    }
}
