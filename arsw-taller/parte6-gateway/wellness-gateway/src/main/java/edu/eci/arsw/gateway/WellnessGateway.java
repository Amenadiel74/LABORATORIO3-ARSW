package edu.eci.arsw.gateway;

import edu.eci.arsw.wellness.*;
import edu.eci.arsw.medical.*;
import edu.eci.arsw.gym.*;
import edu.eci.arsw.recreation.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Scanner;

public class WellnessGateway {
    private static AppointmentServiceGrpc.AppointmentServiceBlockingStub appointmentStub;
    private static MedicalServiceGrpc.MedicalServiceBlockingStub medicalStub;
    private static GymServiceGrpc.GymServiceBlockingStub gymStub;
    private static RecreationServiceGrpc.RecreationServiceBlockingStub recreationStub;

    public static void main(String[] args) {
        ManagedChannel apptChannel = ManagedChannelBuilder.forAddress("localhost", 50061).usePlaintext().build();
        ManagedChannel medChannel = ManagedChannelBuilder.forAddress("localhost", 50062).usePlaintext().build();
        ManagedChannel gymChannel = ManagedChannelBuilder.forAddress("localhost", 50063).usePlaintext().build();
        ManagedChannel recChannel = ManagedChannelBuilder.forAddress("localhost", 50064).usePlaintext().build();

        appointmentStub = AppointmentServiceGrpc.newBlockingStub(apptChannel);
        medicalStub = MedicalServiceGrpc.newBlockingStub(medChannel);
        gymStub = GymServiceGrpc.newBlockingStub(gymChannel);
        recreationStub = RecreationServiceGrpc.newBlockingStub(recChannel);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== Wellness Gateway ===");
            System.out.println("1. Solicitar cita médica");
            System.out.println("2. Ver resumen de bienestar del estudiante");
            System.out.println("3. Reservar sesión de gimnasio");
            System.out.println("4. Reservar recurso recreativo");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");
            
            int option = scanner.nextInt();
            scanner.nextLine();
            
            if (option == 5) break;

            System.out.print("Ingrese ID del estudiante (ej. EST-001): ");
            String studentId = scanner.nextLine();

            switch (option) {
                case 1:
                    System.out.print("Tipo de servicio (MEDICINE/PSYCHOLOGY/DENTISTRY): ");
                    String service = scanner.nextLine();
                    requestAppointment(studentId, service);
                    break;
                case 2:
                    getStudentWellnessSummary(studentId);
                    break;
                case 3:
                    System.out.print("Franja horaria (ej. Lunes 7am): ");
                    String timeSlot = scanner.nextLine();
                    reserveGymSession(studentId, timeSlot);
                    break;
                case 4:
                    System.out.print("ID Recurso (ej. Cancha-1): ");
                    String resourceId = scanner.nextLine();
                    reserveRecreationResource(studentId, resourceId);
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
        
        scanner.close();
        apptChannel.shutdown();
        medChannel.shutdown();
        gymChannel.shutdown();
        recChannel.shutdown();
    }

    private static void requestAppointment(String studentId, String serviceType) {
        ServiceType type;
        try {
            type = ServiceType.valueOf(serviceType);
        } catch (IllegalArgumentException e) {
            System.out.println("Servicio no válido.");
            return;
        }

        AppointmentResponse res = appointmentStub.requestAppointment(AppointmentRequest.newBuilder()
            .setStudentId(studentId)
            .setService(type)
            .setDate("2026-06-20")
            .build());
        System.out.println("Respuesta: " + res.getMessage());
    }

    private static void getStudentWellnessSummary(String studentId) {
        System.out.println("\n=== Resumen de Bienestar - Estudiante: " + studentId + " ===");
        
        System.out.println("Citas activas:");
        AppointmentList appts = appointmentStub.getAppointments(StudentRequest.newBuilder().setStudentId(studentId).build());
        for (Appointment a : appts.getAppointmentsList()) {
            System.out.println("  - " + a.getService() + " | " + a.getDate() + " | " + a.getStatus());
        }

        System.out.println("Sesiones de Gimnasio:");
        SessionList sessions = gymStub.getSessions(SessionListRequest.newBuilder().setStudentId(studentId).build());
        for (GymSession s : sessions.getSessionsList()) {
            System.out.println("  - " + s.getTimeSlot());
        }

        System.out.println("Recursos Recreativos disponibles (General):");
        ResourceList resources = recreationStub.getResources(ResourceListRequest.newBuilder().build());
        for (Resource r : resources.getResourcesList()) {
            System.out.println("  - " + r.getResourceId() + " (" + (r.getAvailable() ? "disponible" : "reservada") + ")");
        }
    }

    private static void reserveGymSession(String studentId, String timeSlot) {
        GymResponse res = gymStub.reserveSession(GymRequest.newBuilder()
            .setStudentId(studentId).setTimeSlot(timeSlot).build());
        System.out.println("Respuesta: " + res.getMessage());
    }

    private static void reserveRecreationResource(String studentId, String resourceId) {
        ReservationResponse res = recreationStub.reserveResource(ReservationRequest.newBuilder()
            .setStudentId(studentId).setResourceId(resourceId).build());
        System.out.println("Respuesta: " + res.getMessage());
    }
}
