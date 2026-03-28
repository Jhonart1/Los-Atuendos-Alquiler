package iterators;

import java.util.List;
import model.Prenda;

/**
 * Iterador concreto que recorre una lista de {@link Prenda}.
 * (Patrón Iterator)
 */
public class ListaPrendasIterator implements PrendaIterator {

    private final List<Prenda> prendas;
    private int index = 0;

    public ListaPrendasIterator(List<Prenda> prendas) {
        this.prendas = prendas;
    }

    @Override
    public boolean hasNext() {
        return prendas != null && index < prendas.size();
    }

    @Override
    public Prenda next() {
        return prendas.get(index++);
    }
}

