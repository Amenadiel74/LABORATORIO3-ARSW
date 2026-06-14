package edu.eci.arsw.gym;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GymServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(50063)
            .addService(new GymServiceImpl())
            .build();
        server.start();
        System.out.println("Gym gRPC Server iniciado en puerto 50063");
        server.awaitTermination();
    }

    static class GymServiceImpl extends GymServiceGrpc.GymServiceImplBase {
        private ConcurrentHashMap<String, GymSession> sessions = new ConcurrentHashMap<>();

        public GymServiceImpl() {
            // Franjas de ejemplo
        }

        @Override
        public void reserveSession(GymRequest request, StreamObserver<GymResponse> responseObserver) {
            String sessionId = UUID.randomUUID().toString();
            GymSession session = GymSession.newBuilder()
                .setSessionId(sessionId)
                .setStudentId(request.getStudentId())
                .setTimeSlot(request.getTimeSlot())
                .build();
            
            sessions.put(sessionId, session);
            
            GymResponse response = GymResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Sesión reservada exitosamente")
                .setSessionId(sessionId)
                .build();
                
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void getSessions(SessionListRequest request, StreamObserver<SessionList> responseObserver) {
            List<GymSession> studentSessions = new ArrayList<>();
            for (GymSession session : sessions.values()) {
                if (session.getStudentId().equals(request.getStudentId())) {
                    studentSessions.add(session);
                }
            }
            
            SessionList list = SessionList.newBuilder()
                .addAllSessions(studentSessions)
                .build();
            responseObserver.onNext(list);
            responseObserver.onCompleted();
        }
    }
}
