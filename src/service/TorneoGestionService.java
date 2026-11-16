package service;

import model.Torneo;
import patterns.strategy.EstrategiaFixture;
import java.util.List;

public class TorneoGestionService {

    /**
     * Historia 1 — Sprint 1: Crear un nuevo torneo
     */
    public Torneo crearTorneo(List<Torneo> torneos, String nombre, String disciplina, int cupoMaximo, String fechaInicio, EstrategiaFixture estrategiaFixture) {
        // Validaciones
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del torneo no puede estar vacío.");
        }
        if (cupoMaximo < 2) {
            throw new IllegalArgumentException("El cupo máximo debe ser al menos de 2 participantes.");
        }

        // Generar ID autoincremental basado en el tamaño de la lista
        int nuevoId = torneos.size() + 1;

        // Instanciar el torneo
        Torneo nuevoTorneo = new Torneo(nuevoId, nombre, disciplina, cupoMaximo, fechaInicio, estrategiaFixture);

        // Agregar a la lista general
        torneos.add(nuevoTorneo);
        System.out.println("Torneo creado exitosamente con ID: " + nuevoId);

        return nuevoTorneo;
    }

    /**
     * Historia 3 — Sprint 1 + Patrón Observer: Inscribir un participante
     */
    public void inscribirParticipante(Torneo torneo, String nombreEquipo) {
        // Validaciones
        if (torneo == null) {
            throw new IllegalArgumentException("El torneo especificado no existe.");
        }
        if (!"ABIERTO".equalsIgnoreCase(torneo.getEstado())) {
            throw new IllegalStateException("No se puede inscribir: el torneo no se encuentra abierto.");
        }
        if (torneo.getParticipantes().size() >= torneo.getCupoMaximo()) {
            throw new IllegalStateException("Inscripción rechazada: el torneo ha alcanzado su cupo máximo.");
        }
        if (nombreEquipo == null || nombreEquipo.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del equipo no puede estar vacío.");
        }
        if (torneo.getParticipantes().contains(nombreEquipo)) {
            throw new IllegalArgumentException("El equipo '" + nombreEquipo + "' ya se encuentra inscripto en este torneo.");
        }

        // Acción: Agregar participante a la lista del torneo
        torneo.getParticipantes().add(nombreEquipo);
        System.out.println("Equipo '" + nombreEquipo + "' inscripto correctamente en el torneo: " + torneo.getNombre());

        // Notificar a los observadores (Patrón Observer)
        torneo.notificarObservadores("Nuevo equipo inscripto: " + nombreEquipo);
    }
}