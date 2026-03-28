package iterators;

import model.Prenda;

/**
 * Iterator dedicado para recorrer prendas sin acoplar el recorrido a la estructura interna de la lista.
 * (Patrón Iterator)
 */
public interface PrendaIterator {
    boolean hasNext();

    Prenda next();
}

