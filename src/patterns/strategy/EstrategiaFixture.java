package patterns.strategy;

import java.util.List;
import model.Partido;

public interface EstrategiaFixture {
    List<Partido> generarCruces(List<String> participantes);
}