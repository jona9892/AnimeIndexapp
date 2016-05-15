package animeindex.BLL;

import java.lang.reflect.Array;
import java.util.ArrayList;

import animeindex.BE.Anime;
import animeindex.BE.Animelist;
import animeindex.DAL.ServiceGateway.Abstraction.IGateway;
import animeindex.DAL.ServiceGateway.Implementation.AnimeGateway;

/**
 * Created by JonathansPC on 15-05-2016.
 */
public class BLLAnime {
    IGateway<Anime> animeGateway;

    public BLLAnime(){
        animeGateway = new AnimeGateway();
    }

    public ArrayList<Anime> getAll(){
        return animeGateway.getAll();
    }

    public ArrayList<Anime> getPage(int idx){
        return animeGateway.getPage(idx);
    }
}
