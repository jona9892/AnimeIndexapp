package animeindex.DAL.ServiceGateway.Abstraction;

import java.util.ArrayList;

/**
 * Created by JonathansPC on 14-05-2016.
 */
public interface IGateway<T> {
    ArrayList<T> getPage(int idx);
    ArrayList<T> getAll();
}
