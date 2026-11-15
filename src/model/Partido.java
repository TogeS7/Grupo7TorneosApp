package model;

public class Partido {
    private String local;
    private String visitante;
    private String ronda;
    private String estado;

    public Partido(String local, String visitante, String ronda) {
        this.local = local;
        this.visitante = visitante;
        this.ronda = ronda;
        this.estado = "PROGRAMADO";
    }

    @Override
    public String toString() {
        return "[" + ronda + "] " + local + " vs " + visitante + " (" + estado + ")";
    }
}