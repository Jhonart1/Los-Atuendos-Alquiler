package iterators;

/**
 * Contrato genérico del patrón Iterator.
 * @param <T> tipo de elemento a iterar
 */
public interface IteratorGenerico<T> {
    boolean hasNext();

    T next();
}

