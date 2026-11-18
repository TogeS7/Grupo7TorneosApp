package service;

import java.util.List;
import model.Partido;
import model.Torneo;

public class TorneoConsultaService {

    private static final String ESTADO_EN_CURSO = "EN_CURSO";
    private static final String ESTADO_FINALIZADO = "FINALIZADO";

    // Historia 2 (Sprint 1): Listar y filtrar torneos por estado
    public void listarTorneos(List<Torneo> torneos, String filtroEstado) {
        System.out.println("\n--- LISTADO DE TORNEOS (" + filtroEstado.toUpperCase() + ") ---");
        boolean hayResultados = false;

        for (Torneo t : torneos) {
            if ("TODOS".equalsIgnoreCase(filtroEstado) || t.getEstado().equalsIgnoreCase(filtroEstado)) {
                hayResultados = true;
                imprimirDetalleTorneo(t);
            }
        }

        if (!hayResultados) {
            System.out.println("No hay torneos registrados con ese estado.");
        }
    }

    private void imprimirDetalleTorneo(Torneo t) {
        System.out.println("ID: " + t.getId() + " | " + t.getNombre() + " [" + t.getDisciplina() + "]"
                + " | Estado: " + t.getEstado()
                + " | Cupos: " + t.getParticipantes().size() + "/" + t.getCupoMaximo());
        System.out.println("   Inscritos: " + (t.getParticipantes().isEmpty() ? "Ninguno" : t.getParticipantes()));

        List<Partido> partidos = t.getPartidos();
        if (!partidos.isEmpty()) {
            System.out.println("   Fixture:");
            for (int i = 0; i < partidos.size(); i++) {
                System.out.println("     " + (i + 1) + ". " + partidos.get(i));
            }
        }
    }

    // Historia 5 (Sprint 2 - Strategy) + Cambio de estado (Sprint 3 - Observer)
    public boolean iniciarTorneoYGenerarFixture(Torneo torneo) {
        if (torneo == null) {
            System.out.println("Error: Torneo no encontrado.");
            return false;
        }
        if (!"ABIERTO".equals(torneo.getEstado())) {
            System.out.println("Error: Solo se puede iniciar un torneo en estado ABIERTO.");
            return false;
        }
        if (torneo.getParticipantes().size() < 2) {
            System.out.println("Error: Se necesitan al menos 2 participantes para generar el fixture.");
            return false;
        }

        List<Partido> cruces = torneo.getEstrategiaFixture().generarCruces(torneo.getParticipantes());
        torneo.setPartidos(cruces);
        torneo.setEstado(ESTADO_EN_CURSO); // Dispara automáticamente el patrón Observer
        return true;
    }

    // Historia SCRUM-13: Registrar resultado de un partido (dispara Observer)
    public boolean registrarResultadoPartido(Torneo torneo, int numeroPartido, int golesLocal, int golesVisitante) {
        if (torneo == null || !ESTADO_EN_CURSO.equals(torneo.getEstado())) {
            System.out.println("Error: El torneo debe existir y estar EN_CURSO para cargar resultados.");
            return false;
        }
        List<Partido> partidos = torneo.getPartidos();
        if (numeroPartido < 1 || numeroPartido > partidos.size()) {
            System.out.println("Error: Número de partido inválido.");
            return false;
        }
        if (golesLocal < 0 || golesVisitante < 0) {
            System.out.println("Error: Los puntajes no pueden ser negativos.");
            return false;
        }

        Partido p = partidos.get(numeroPartido - 1);
        if (ESTADO_FINALIZADO.equals(p.getEstado())) {
            System.out.println("Error: Ese partido ya tiene un resultado registrado.");
            return false;
        }

        p.registrarResultado(golesLocal, golesVisitante);
        torneo.notificarObservadores("Resultado registrado -> " + p);
        return true;
    }

    // Finalizar torneo (dispara Observer)
    public boolean finalizarTorneo(Torneo torneo) {
        if (torneo == null || !ESTADO_EN_CURSO.equals(torneo.getEstado())) {
            System.out.println("Error: Solo se puede finalizar un torneo que esté EN_CURSO.");
            return false;
        }
        torneo.setEstado(ESTADO_FINALIZADO);
        return true;
    }
}