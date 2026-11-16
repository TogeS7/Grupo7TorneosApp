package patterns.observer;

public class NotificadorEmail implements ObservadorTorneo {

    @Override
    public void actualizar(String nombreTorneo, String evento) {
        System.out.println("[EMAIL] Enviando correo a participantes de '" + nombreTorneo + "': " + evento);
    }
}