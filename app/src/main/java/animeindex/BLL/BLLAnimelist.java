package animeindex.BLL;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import animeindex.BE.Animelist;
import animeindex.DAL.DALC.Abstractions.ICrud;
import animeindex.DAL.DALC.Implementations.DALCAnimelist;

/**
 * Created by JonathansPC on 15-05-2016.
 */
public class BLLAnimelist {

    private ICrud<Animelist> animeDB;

    public BLLAnimelist(Context context){
        animeDB = new DALCAnimelist(context);
    }

    public Animelist add(Animelist item){
        animeDB.add(item);
        return item;
    }

    public Animelist read(int id){
        return animeDB.read(id);
    }

    public Collection<Animelist> readAll(){
        return animeDB.readAll();
    }

    public Animelist update(Animelist item){
        return animeDB.update(item);
    }

    /**
     * This method sets an arraylist with integers, to use for the episodes spinner
     * @return Arraylist with integers
     */
    public ArrayList<Integer> getEpisodes(Animelist m_animelist) {
        int episodes = m_animelist.getEpisodeCount();

        ArrayList<Integer> result = new ArrayList<Integer>();
        //puts the values in the arraylist from 0 to the number of episodes the anime contains
        for (int i = 0; i <= episodes; i++) {
            result.add(i);
        }
        return result;
    }



}

