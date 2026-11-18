package model;

import java.util.ArrayList;
import java.util.List;
import patterns.observer.ObservadorTorneo;
import patterns.strategy.EstrategiaFixture;

public class Torneo {
    private final int id;
    private final String nombre;
    private final String disciplina;
    private final int cupoMaximo;
    private final String fechaInicio;
    private String estado; // "ABIERTO", "EN_CURSO", "FINALIZADO"
    private final EstrategiaFixture estrategiaFixture;
    private final List<String> participantes;
    private List<Partido> partidos;
    private final List<ObservadorTorneo> observadores;

    public Torneo(int id, String nombre, String disciplina, int cupoMaximo, String fechaInicio, EstrategiaFixture estrategiaFixture) {
        this.id = id;
        this.nombre = nombre;
        this.disciplina = disciplina;
        this.cupoMaximo = cupoMaximo;
        this.fechaInicio = fechaInicio;
        this.estado = "ABIERTO";
        this.estrategiaFixture = estrategiaFixture;
        this.participantes = new ArrayList<>();
        this.partidos = new ArrayList<>();
        this.observadores = new ArrayList<>();
    }

    // Patrón Observer (Sprint 3 - Desafío 2)
    public void agregarObservador(ObservadorTorneo obs) {
        observadores.add(obs);
    }

    public void notificarObservadores(String evento) {
        for (ObservadorTorneo obs : observadores) {
            obs.actualizar(this.nombre, evento);
        }
    }

    // Getters y Setters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDisciplina() { return disciplina; }
    public int getCupoMaximo() { return cupoMaximo; }
    public String getFechaInicio() { return fechaInicio; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) {
        this.estado = estado;
        notificarObservadores("El torneo cambió de estado a: " + estado);
    }
    public EstrategiaFixture getEstrategiaFixture() { return estrategiaFixture; }
    public List<String> getParticipantes() { return participantes; }
    public List<Partido> getPartidos() { return partidos; }
    public void setPartidos(List<Partido> partidos) { this.partidos = partidos; }
}