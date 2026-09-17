package com.learnhub.service;

/** Evento publicado al completar el registro de un profesor. */
public record ProfesorRegistradoEvent(String email, String nombre) {}
