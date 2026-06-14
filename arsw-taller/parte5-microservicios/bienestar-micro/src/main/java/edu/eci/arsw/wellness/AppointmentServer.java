package edu.eci.arsw.wellness;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AppointmentServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(50061)
            .addService(new AppointmentServiceImpl())
            .build();
        server.start();
        System.out.println("Appointment gRPC Server iniciado en puerto 50061");
        server.awaitTermination();
    }

    static class AppointmentServiceImpl extends AppointmentServiceGrpc.AppointmentServiceImplBase {
        private ConcurrentHashMap<String, Appointment> appointments = new ConcurrentHashMap<>();

        @Override
        public void requestAppointment(AppointmentRequest request,
                                       StreamObserver<AppointmentResponse> responseObserver) {
            String id = UUID.randomUUID().toString();
            Appointment appt = Appointment.newBuilder()
                .setAppointmentId(id)
                .setStudentId(request.getStudentId())
                .setService(request.getService())
                .setDate(request.getDate())
                .setStatus(AppointmentStatus.REQUESTED)
                .build();
                
            appointments.put(id, appt);
            
            AppointmentResponse response = AppointmentResponse.newBuilder()
                .setAppointmentId(id)
                .setSuccess(true)
                .setMessage("Cita agendada exitosamente")
                .build();
                
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void cancelAppointment(CancelRequest request,
                                      StreamObserver<CancelResponse> responseObserver) {
            String id = request.getAppointmentId();
            Appointment appt = appointments.get(id);
            
            CancelResponse response;
            if (appt != null) {
                appointments.put(id, appt.toBuilder().setStatus(AppointmentStatus.CANCELLED).build());
                response = CancelResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Cita cancelada")
                    .build();
            } else {
                response = CancelResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Cita no encontrada")
                    .build();
            }
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void getAppointments(StudentRequest request,
                                    StreamObserver<AppointmentList> responseObserver) {
            List<Appointment> studentAppts = new ArrayList<>();
            for (Appointment appt : appointments.values()) {
                if (appt.getStudentId().equals(request.getStudentId()) && 
                    appt.getStatus() == AppointmentStatus.REQUESTED) {
                    studentAppts.add(appt);
                }
            }
            
            AppointmentList list = AppointmentList.newBuilder()
                .addAllAppointments(studentAppts)
                .build();
                
            responseObserver.onNext(list);
            responseObserver.onCompleted();
        }
    }
}
