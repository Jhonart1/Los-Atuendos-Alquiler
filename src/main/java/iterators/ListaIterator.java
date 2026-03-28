package iterators;

import java.util.List;

/**
 * Iterador concreto para recorrer listas.
 * @param <T> tipo de elemento a iterar
 */
public class ListaIterator<T> implements IteratorGenerico<T> {

    private final List<T> lista;
    private int index = 0;

    public ListaIterator(List<T> lista) {
        this.lista = lista;
    }

    @Override
    public boolean hasNext() {
        return lista != null && index < lista.size();
    }

    @Override
    public T next() {
        return lista.get(index++);
    }
}

