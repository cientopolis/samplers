package org.cientopolis.samplers.persistence;

import java.util.List;

/**
 * Created by Xavier on 09/02/2017.
 */

public interface DAO<E,K> {

    K save(E object);
    E find(K key);
    boolean delete(E object);
    List<E> list();
}
