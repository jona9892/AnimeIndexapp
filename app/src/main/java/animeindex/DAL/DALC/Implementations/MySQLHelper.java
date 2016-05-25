package animeindex.DAL.DALC.Implementations;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jona9892 on 01-04-2016.
 */
public class MySQLHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "animeindex.db";
    private final static int DATABASE_VERSION = 1;
    public static final String TABLE_ANIMELIST = "Animelist";
    public static final String TABLE_PICTURES = "Picture";


    MySQLHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_ANIMELIST
                + "(id INTEGER PRIMARY KEY, title TEXT,type TEXT, episodeCount INTEGER, image TEXT," +
                " status TEXT, episodesSeen INTEGER, rating INTEGER)");

        db.execSQL("CREATE TABLE " + TABLE_PICTURES
                + "(id INTEGER PRIMARY KEY, filename TEXT, title TEXT, description TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
