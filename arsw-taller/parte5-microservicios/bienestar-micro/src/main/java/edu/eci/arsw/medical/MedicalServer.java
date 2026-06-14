package edu.eci.arsw.medical;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.List;

public class MedicalServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(50062)
            .addService(new MedicalServiceImpl())
            .build();
        server.start();
        System.out.println("Medical gRPC Server iniciado en puerto 50062");
        server.awaitTermination();
    }

    static class MedicalServiceImpl extends MedicalServiceGrpc.MedicalServiceImplBase {
        @Override
        public void getSpecialties(SpecialtyRequest request, StreamObserver<SpecialtyList> responseObserver) {
            List<Specialty> specialties = new ArrayList<>();
            specialties.add(Specialty.newBuilder().setName("Medicina General").setDescription("Consulta general").setSlots(10).build());
            specialties.add(Specialty.newBuilder().setName("Psicologia").setDescription("Apoyo emocional").setSlots(5).build());
            specialties.add(Specialty.newBuilder().setName("Odontologia").setDescription("Cuidado dental").setSlots(8).build());

            SpecialtyList list = SpecialtyList.newBuilder().addAllSpecialties(specialties).build();
            responseObserver.onNext(list);
            responseObserver.onCompleted();
        }

        @Override
        public void getAvailability(AvailabilityRequest request, StreamObserver<AvailabilityResponse> responseObserver) {
            // Stub basico
            AvailabilityResponse response = AvailabilityResponse.newBuilder()
                .setAvailable(true)
                .setSlotsLeft(5)
                .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
