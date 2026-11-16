package patterns.strategy;

import java.util.ArrayList;
import java.util.List;
import model.Partido;

public class EstrategiaEliminacionDirecta implements EstrategiaFixture {

    @Override
    public List<Partido> generarCruces(List<String> participantes) {
        List<Partido> partidos = new ArrayList<>();
        for (int i = 0; i < participantes.size(); i += 2) {
            if (i + 1 < participantes.size()) {
                partidos.add(new Partido(participantes.get(i), participantes.get(i + 1), "Ronda 1"));
            }
        }
        return partidos;
    }
}