package patterns.observer;

public class NotificadorConsola implements ObservadorTorneo {

    @Override
    public void actualizar(String nombreTorneo, String evento) {
        System.out.println("[PUSH/CONSOLA] Torneo '" + nombreTorneo + "' -> " + evento);
    }
}