package org.cientopolis.samplers.persistence;

import java.util.List;

/*
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;
*/


/**
 * Created by Xavier on 09/02/2017.
 */

public interface DAO<E,K> {

    //@Insert
    K insert(E object);

    //@Update
    K update(E object);

    //@Delete
    void delete(E object);

    E find(K key);
    List<E> list();
}
