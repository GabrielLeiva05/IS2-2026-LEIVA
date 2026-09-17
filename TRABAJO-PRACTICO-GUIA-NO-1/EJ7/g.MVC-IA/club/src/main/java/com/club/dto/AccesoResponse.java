package com.club.dto;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
public record AccesoResponse(String id, String personaId, String personaNombre, LocalDate fecha, LocalTime horaEntrada, LocalTime horaSalida, long duracionMinutos) {
    public static AccesoResponse from(com.club.entity.RegistroAcceso a) {
        long minutos = 0;
        if (a.getHoraEntrada() != null && a.getHoraSalida() != null) minutos = Duration.between(a.getHoraEntrada(), a.getHoraSalida()).toMinutes();
        return new AccesoResponse(a.getId(), a.getPersona().getId(), a.getPersona().getNombre()+" "+a.getPersona().getApellido(), a.getFecha(), a.getHoraEntrada(), a.getHoraSalida(), minutos);
    }
}
