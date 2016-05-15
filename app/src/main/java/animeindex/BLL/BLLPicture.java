package animeindex.BLL;

import android.content.Context;

import java.util.Collection;

import animeindex.BE.Animelist;
import animeindex.BE.Picture;
import animeindex.DAL.DALC.Abstractions.ICrud;
import animeindex.DAL.DALC.Implementations.DALCPictures;

/**
 * Created by JonathansPC on 15-05-2016.
 */
public class BLLPicture {

    private ICrud<Picture> pictureDB;

    public BLLPicture(Context context){
        pictureDB = new DALCPictures(context);
    }

    public Picture add(Picture item){
        pictureDB.add(item);
        return item;
    }

    public Picture read(int id){
        return pictureDB.read(id);
    }

    public Collection<Picture> readAll(){
        return pictureDB.readAll();
    }

    public Picture update(Picture item){
        return pictureDB.update(item);
    }

    public void delete(int id){
         pictureDB.delete(id);
    }
}
