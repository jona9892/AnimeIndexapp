package animeindex.Model;

import java.io.Serializable;

/**
 * Created by jona9892 on 09-04-2016.
 */
public class Genre implements Serializable {
    private String name;

    public Genre(){

    }

    public Genre(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
