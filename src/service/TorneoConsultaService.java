package service;

import java.util.List;
import model.Partido;
import model.Torneo;

public class TorneoConsultaService {

    // Historia 2 (Sprint 1): Listar y filtrar torneos por estado
    public void listarTorneos(List<Torneo> torneos, String filtroEstado) {
        System.out.println("\n--- LISTADO DE TORNEOS (" + filtroEstado.toUpperCase() + ") ---");
        boolean hayResultados = false;

        for (Torneo t : torneos) {
            if ("TODOS".equalsIgnoreCase(filtroEstado) || t.getEstado().equalsIgnoreCase(filtroEstado)) {
                hayResultados = true;
                System.out.println("ID: " + t.getId() + " | " + t.getNombre() + " [" + t.getDisciplina() + "]"
                        + " | Estado: " + t.getEstado()
                        + " | Cupos: " + t.getParticipantes().size() + "/" + t.getCupoMaximo());
                System.out.println("   Inscritos: " + (t.getParticipantes().isEmpty() ? "Ninguno" : t.getParticipantes()));

                if (!t.getPartidos().isEmpty()) {
                    System.out.println("   Fixture:");
                    for (Partido p : t.getPartidos()) {
                        System.out.println("     - " + p);
                    }
                }
            }
        }

        if (!hayResultados) {
            System.out.println("No hay torneos registrados con ese estado.");
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
        torneo.setEstado("EN_CURSO"); // Dispara automáticamente el patrón Observer
        return true;
    }

    // Finalizar torneo (dispara Observer)
    public boolean finalizarTorneo(Torneo torneo) {
        if (torneo == null || !"EN_CURSO".equals(torneo.getEstado())) {
            System.out.println("Error: Solo se puede finalizar un torneo que esté EN_CURSO.");
            return false;
        }
        torneo.setEstado("FINALIZADO");
        return true;
    }
}