package edu.eci.arsw.recommendation;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.List;

public class RecommendationServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(50053)
            .addService(new RecommendationServiceImpl())
            .build();
        server.start();
        System.out.println("Recommendation gRPC Server iniciado en puerto 50053");
        server.awaitTermination();
    }

    static class RecommendationServiceImpl extends RecommendationServiceGrpc.RecommendationServiceImplBase {
        @Override
        public void getRecommendations(RecommendationRequest request, StreamObserver<RecommendationList> responseObserver) {
            List<String> recommendations = new ArrayList<>();
            if (request.getMovieId() == 1) {
                recommendations.add("Inception");
                recommendations.add("Contact");
                recommendations.add("2001: A Space Odyssey");
            } else if (request.getMovieId() == 2) {
                recommendations.add("John Wick");
                recommendations.add("Ghost in the Shell");
            }

            RecommendationList list = RecommendationList.newBuilder().addAllTitles(recommendations).build();
            responseObserver.onNext(list);
            responseObserver.onCompleted();
        }
    }
}
