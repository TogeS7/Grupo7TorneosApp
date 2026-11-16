package patterns.strategy;

import java.util.ArrayList;
import java.util.List;
import model.Partido;

public class EstrategiaLiga implements EstrategiaFixture {

    @Override
    public List<Partido> generarCruces(List<String> participantes) {
        List<Partido> partidos = new ArrayList<>();
        for (int i = 0; i < participantes.size(); i++) {
            for (int j = i + 1; j < participantes.size(); j++) {
                partidos.add(new Partido(participantes.get(i), participantes.get(j), "Fecha Única"));
            }
        }
        return partidos;
    }
}