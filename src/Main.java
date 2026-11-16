import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.Torneo;
import patterns.observer.NotificadorConsola;
import patterns.observer.NotificadorEmail;
import patterns.strategy.EstrategiaEliminacionDirecta;
import patterns.strategy.EstrategiaFixture;
import patterns.strategy.EstrategiaLiga;
import service.TorneoConsultaService;
import service.TorneoGestionService;

public class Main {
    public static void main(String[] args) {
        List<Torneo> torneos = new ArrayList<>();
        TorneoGestionService gestionService = new TorneoGestionService();
        TorneoConsultaService consultaService = new TorneoConsultaService();

        // Precarga inicial para demostración rápida de Sprints 1, 2 y 3
        Torneo copaEliminacion = gestionService.crearTorneo(
                torneos, "Copa Relampago CS2", "Counter-Strike 2", 4, "20/11/2026", new EstrategiaEliminacionDirecta()
        );
        copaEliminacion.agregarObservador(new NotificadorConsola());
        copaEliminacion.agregarObservador(new NotificadorEmail());
        gestionService.inscribirParticipante(copaEliminacion, "Equipo Alfa");
        gestionService.inscribirParticipante(copaEliminacion, "Equipo Beta");
        gestionService.inscribirParticipante(copaEliminacion, "Equipo Gamma");
        gestionService.inscribirParticipante(copaEliminacion, "Equipo Delta");

        Torneo ligaValorant = gestionService.crearTorneo(
                torneos, "Liga Universitaria", "Valorant", 3, "25/11/2026", new EstrategiaLiga()
        );
        ligaValorant.agregarObservador(new NotificadorConsola());
        gestionService.inscribirParticipante(ligaValorant, "Los Pumas");
        gestionService.inscribirParticipante(ligaValorant, "Halcones");

        Scanner sc = new Scanner(System.in);
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n=== GESTIÓN DE TORNEOS - GRUPO 7 ===");
            System.out.println("1. Crear torneo");
            System.out.println("2. Inscribir equipo en torneo");
            System.out.println("3. Listar torneos (filtrar por estado)");
            System.out.println("4. Iniciar torneo y generar fixture (Strategy)");
            System.out.println("5. Registrar resultado de partido (SCRUM-13)");
            System.out.println("6. Finalizar torneo (Observer)");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            if (!sc.hasNextLine()) {
                break;
            }

            try {
                opcion = Integer.parseInt(sc.nextLine().trim());
                switch (opcion) {
                    case 1 -> {
                        System.out.print("Nombre: ");
                        String nombre = sc.nextLine();
                        System.out.print("Disciplina: ");
                        String disc = sc.nextLine();
                        System.out.print("Cupo máximo: ");
                        int cupo = Integer.parseInt(sc.nextLine().trim());
                        System.out.print("Formato (1 = Eliminación Directa, 2 = Liga): ");
                        int tipo = Integer.parseInt(sc.nextLine().trim());
                        EstrategiaFixture est = (tipo == 2) ? new EstrategiaLiga() : new EstrategiaEliminacionDirecta();

                        Torneo nuevo = gestionService.crearTorneo(torneos, nombre, disc, cupo, "20/11/2026", est);
                        nuevo.agregarObservador(new NotificadorConsola());
                        nuevo.agregarObservador(new NotificadorEmail());
                    }
                    case 2 -> {
                        System.out.print("ID del torneo: ");
                        int id = Integer.parseInt(sc.nextLine().trim());
                        Torneo t = buscarPorId(torneos, id);
                        System.out.print("Nombre del equipo: ");
                        gestionService.inscribirParticipante(t, sc.nextLine());
                    }
                    case 3 -> {
                        System.out.println("Filtrar por estado: 1. TODOS | 2. ABIERTO | 3. EN_CURSO | 4. FINALIZADO | 0. Volver");
                        System.out.print("Seleccione estado: ");
                        int estOp = Integer.parseInt(sc.nextLine().trim());
                        String filtro = switch (estOp) {
                            case 1 -> "TODOS";
                            case 2 -> "ABIERTO";
                            case 3 -> "EN_CURSO";
                            case 4 -> "FINALIZADO";
                            case 0 -> null;
                            default -> {
                                System.out.println("Opción de estado inválida.");
                                yield null;
                            }
                        };
                        if (filtro != null) {
                            consultaService.listarTorneos(torneos, filtro);
                        }
                    }
                    case 4 -> {
                        System.out.print("ID del torneo a iniciar: ");
                        int id = Integer.parseInt(sc.nextLine().trim());
                        Torneo t = buscarPorId(torneos, id);
                        if (consultaService.iniciarTorneoYGenerarFixture(t)) {
                            System.out.println("Fixture generado:");
                            for (int i = 0; i < t.getPartidos().size(); i++) {
                                System.out.println("  " + (i + 1) + ". " + t.getPartidos().get(i));
                            }
                        }
                    }
                    case 5 -> {
                        System.out.print("ID del torneo: ");
                        int id = Integer.parseInt(sc.nextLine().trim());
                        Torneo t = buscarPorId(torneos, id);
                        if (t == null || t.getPartidos().isEmpty()) {
                            System.out.println("Error: Torneo no encontrado o sin fixture generado.");
                            break;
                        }
                        for (int i = 0; i < t.getPartidos().size(); i++) {
                            System.out.println("  " + (i + 1) + ". " + t.getPartidos().get(i));
                        }
                        System.out.print("Número de partido: ");
                        int numPartido = Integer.parseInt(sc.nextLine().trim());
                        System.out.print("Puntaje Local: ");
                        int gLocal = Integer.parseInt(sc.nextLine().trim());
                        System.out.print("Puntaje Visitante: ");
                        int gVisitante = Integer.parseInt(sc.nextLine().trim());
                        consultaService.registrarResultadoPartido(t, numPartido, gLocal, gVisitante);
                    }
                    case 6 -> {
                        System.out.print("ID del torneo a finalizar: ");
                        int id = Integer.parseInt(sc.nextLine().trim());
                        consultaService.finalizarTorneo(buscarPorId(torneos, id));
                    }
                    case 0 -> System.out.println("Cerrando sistema...");
                    default -> System.out.println("Opción inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("[AVISO] Debe ingresar un número válido.");
            } catch (Exception e) {
                System.out.println("[AVISO] " + e.getMessage());
            }
        }
        sc.close();
    }

    private static Torneo buscarPorId(List<Torneo> torneos, int id) {
        for (Torneo t : torneos) {
            if (t.getId() == id) return t;
        }
        return null;
    }
}