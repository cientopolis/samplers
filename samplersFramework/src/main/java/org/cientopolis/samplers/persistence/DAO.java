package org.cientopolis.samplers.persistence;

import java.util.List;

/**
 * Created by Xavier on 09/02/2017.
 */

public interface DAO<E,K> {
    public K save(E object);
    public E find(K key);
    public boolean delete(E object);
    public List<E> list();
}
