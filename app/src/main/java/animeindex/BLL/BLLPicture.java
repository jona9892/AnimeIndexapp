package animeindex.BLL;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import animeindex.BE.Animelist;
import animeindex.BE.Picture;
import animeindex.DAL.DALC.Abstractions.ICrud;
import animeindex.DAL.DALC.Implementations.DALCPictures;

/**
 * Created by JonathansPC on 15-05-2016.
 */
public class BLLPicture {

    private ICrud<Picture> pictureDB;
    public static String TAG = "BLLPicture";

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

    /**
     * This will get file from the picture, and set the file info
     * @return the file
     */
    public File getOutputPhotoFile() {
        //This will create a directory on the phone where the photos will be stored.
        //This will save the picture on the phone, and for other app to get it
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "AnimeIndex");

        //This checks whether the directory that should be created is created.
        if (!mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d(TAG, "failed to create directory");
                return null;
            }
        }

        //This sets a date for when the picture is taking.
        //We need this to create a unique picture, otherwise it will get overridden
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        //This creates a file with the the directory path, img to show its a image, date to make it unique and the type of the image
        File mediaFile = new File(mediaStorageDir.getPath() +
                File.separator + "img" +
                "_"+ timeStamp + "." + "jpg");

        return mediaFile;
    }
}
