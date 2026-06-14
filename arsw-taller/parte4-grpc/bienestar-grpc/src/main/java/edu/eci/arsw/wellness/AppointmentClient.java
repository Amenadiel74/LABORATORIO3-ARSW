package edu.eci.arsw.wellness;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class AppointmentClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder
            .forAddress("localhost", 50061)
            .usePlaintext()
            .build();

        AppointmentServiceGrpc.AppointmentServiceBlockingStub stub =
            AppointmentServiceGrpc.newBlockingStub(channel);

        String studentId = "EST-001";
        
        System.out.println("1. Solicitando cita de MEDICINE...");
        AppointmentResponse res1 = stub.requestAppointment(AppointmentRequest.newBuilder()
            .setStudentId(studentId)
            .setStudentName("Juan Perez")
            .setEmail("juan@eci.edu.co")
            .setService(ServiceType.MEDICINE)
            .setDate("2026-06-20")
            .build());
        System.out.println("Respuesta: " + res1.getMessage() + " ID: " + res1.getAppointmentId());
        
        System.out.println("\n2. Solicitando cita de PSYCHOLOGY...");
        AppointmentResponse res2 = stub.requestAppointment(AppointmentRequest.newBuilder()
            .setStudentId(studentId)
            .setStudentName("Juan Perez")
            .setEmail("juan@eci.edu.co")
            .setService(ServiceType.PSYCHOLOGY)
            .setDate("2026-06-22")
            .build());
        System.out.println("Respuesta: " + res2.getMessage() + " ID: " + res2.getAppointmentId());
        
        System.out.println("\n3. Consultando citas de EST-001...");
        AppointmentList list = stub.getAppointments(StudentRequest.newBuilder()
            .setStudentId(studentId)
            .build());
        System.out.println("Citas activas encontradas: " + list.getAppointmentsCount());
        for (Appointment a : list.getAppointmentsList()) {
            System.out.println(" - " + a.getService() + " el " + a.getDate());
        }
        
        System.out.println("\n4. Cancelando la primera cita (" + res1.getAppointmentId() + ")...");
        CancelResponse cancelRes = stub.cancelAppointment(CancelRequest.newBuilder()
            .setAppointmentId(res1.getAppointmentId())
            .build());
        System.out.println("Respuesta cancelación: " + cancelRes.getMessage());
        
        System.out.println("\n5. Consultando citas de EST-001...");
        list = stub.getAppointments(StudentRequest.newBuilder()
            .setStudentId(studentId)
            .build());
        System.out.println("Citas activas encontradas: " + list.getAppointmentsCount());
        for (Appointment a : list.getAppointmentsList()) {
            System.out.println(" - " + a.getService() + " el " + a.getDate());
        }

        channel.shutdown();
    }
}
