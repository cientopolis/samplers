package org.cientopolis.samplers.persistence;

import java.util.List;

/**
 * Created by Xavier on 09/02/2017.
 */

public interface DAO<E,K> {

    K insert(E object);

    K update(E object);

    void delete(E object);

    E find(K key);

    List<E> list();
}
