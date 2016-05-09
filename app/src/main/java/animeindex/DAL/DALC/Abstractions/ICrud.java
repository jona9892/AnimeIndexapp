package animeindex.DAL.DALC.Abstractions;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by JonathansPC on 22-04-2016.
 */
public interface ICrud<T> extends Serializable {
    T add(T item);
    T read(int id);
    Collection<T> readAll();
    void delete(int id);
    T update(T item);
}
