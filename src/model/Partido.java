package model;

public class Partido {
    private static final String ESTADO_FINALIZADO = "FINALIZADO";

    private final String local;
    private final String visitante;
    private final String ronda;
    private String estado;
    private int golesLocal;
    private int golesVisitante;

    public Partido(String local, String visitante, String ronda) {
        this.local = local;
        this.visitante = visitante;
        this.ronda = ronda;
        this.estado = "PROGRAMADO";
    }

    public void registrarResultado(int golesLocal, int golesVisitante) {
        this.golesLocal = golesLocal;
        this.golesVisitante = golesVisitante;
        this.estado = ESTADO_FINALIZADO;
    }

    public String getLocal() { return local; }
    public String getVisitante() { return visitante; }
    public String getEstado() { return estado; }

    @Override
    public String toString() {
        if (ESTADO_FINALIZADO.equals(estado)) {
            return "[" + ronda + "] " + local + " " + golesLocal + " - " + golesVisitante + " " + visitante + " (" + estado + ")";
        }
        return "[" + ronda + "] " + local + " vs " + visitante + " (" + estado + ")";
    }
}