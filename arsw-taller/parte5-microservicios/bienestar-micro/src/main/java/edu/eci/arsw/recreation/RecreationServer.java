package edu.eci.arsw.recreation;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.List;

public class RecreationServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(50064)
            .addService(new RecreationServiceImpl())
            .build();
        server.start();
        System.out.println("Recreation gRPC Server iniciado en puerto 50064");
        server.awaitTermination();
    }

    static class RecreationServiceImpl extends RecreationServiceGrpc.RecreationServiceImplBase {
        @Override
        public void getResources(ResourceListRequest request, StreamObserver<ResourceList> responseObserver) {
            List<Resource> resources = new ArrayList<>();
            resources.add(Resource.newBuilder().setResourceId("Cancha-1").setName("Cancha 1").setAvailable(true).build());
            resources.add(Resource.newBuilder().setResourceId("Cancha-2").setName("Cancha 2").setAvailable(true).build());
            resources.add(Resource.newBuilder().setResourceId("Sala-Juegos").setName("Sala de Juegos").setAvailable(false).build());

            ResourceList list = ResourceList.newBuilder().addAllResources(resources).build();
            responseObserver.onNext(list);
            responseObserver.onCompleted();
        }

        @Override
        public void reserveResource(ReservationRequest request, StreamObserver<ReservationResponse> responseObserver) {
            ReservationResponse response = ReservationResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Recurso " + request.getResourceId() + " reservado")
                .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void returnResource(ReturnRequest request, StreamObserver<ReturnResponse> responseObserver) {
            ReturnResponse response = ReturnResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Recurso " + request.getResourceId() + " devuelto")
                .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
