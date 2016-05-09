package animeindex.DAL.DALC.Implementations;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import animeindex.DAL.DALC.Abstractions.ICrud;
import animeindex.Model.Animelist;

/**
 * Created by JonathansPC on 22-04-2016.
 */
public class DALCAnimelist implements ICrud<Animelist> {

    private Context context;
    private SQLiteDatabase db;
    private SQLiteStatement insertStmt;
    private static final String INSERT = "insert into " + MySQLHelper.TABLE_ANIMELIST
            + "(title, type, episodeCount, image, status, episodesSeen, rating) values (?,?,?,?,?,?,?)";

    public DALCAnimelist(Context context) {
        this.context = context;
        MySQLHelper mySQLHelper = new MySQLHelper(this.context);
        this.db = mySQLHelper.getWritableDatabase();
        this.insertStmt = db.compileStatement(INSERT);
    }

    /**
     * This method will add a new anielsit objet to the SQLlite database using the INSERT string which is the same as sql statement
     * @param item The animelist object to be added
     * @return The animelist that were added
     */
    @Override
    public Animelist add(Animelist item) {
        insertStmt.bindString(1, item.getTitle());
        insertStmt.bindString(2, item.getType());
        insertStmt.bindDouble(3, item.getEpisodeCount());
        insertStmt.bindString(4, item.getImage());
        insertStmt.bindString(5, item.getStatus());
        insertStmt.bindDouble(6, item.getEpisodesSeen());
        insertStmt.bindDouble(7, item.getRating());

        item.setId((int) insertStmt.executeInsert());
        return item;
    }

    /**
     * This method will get a specific animelist object from the database
     * @param id the object containing this id
     * @return the animelist object
     */
    @Override
    public Animelist read(int id) {
        List<Animelist> list = new ArrayList<Animelist>();
        Cursor cursor = db.query(MySQLHelper.TABLE_ANIMELIST, new String[] { "Id", "title", "type", "episodeCount", "image", "status", "episodesSeen", "rating" },
                "id = " + id, null, null, null, "title asc");
        if (cursor.moveToFirst()) {
            do {
                list.add(new Animelist(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getInt(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6), cursor.getInt(7)));
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return list.get(0);
    }

    /**
     * This method gets all animelist objects from the SQLlite database
     * @return all animelist objects
     */
    @Override
    public Collection<Animelist> readAll() {
        List<Animelist> list = new ArrayList<Animelist>();
        Cursor cursor = db.query(MySQLHelper.TABLE_ANIMELIST, new String[] { "Id", "title", "type", "episodeCount", "image", "status", "episodesSeen", "rating" },
                null, null, null, null, "title asc");
        if (cursor.moveToFirst()) {
            do {
                list.add(new Animelist(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getInt(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6), cursor.getInt(7)));
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    @Override
    public void delete(int id) {
        db.delete(MySQLHelper.TABLE_ANIMELIST, null, null);
    }

    /**
     * This method will update a specific animelist object
     * @param item the specific animelist object
     * @return the specific animelist
     */
    @Override
    public Animelist update(Animelist item) {
        ContentValues cv = new ContentValues();
        cv.put("title", item.getTitle());
        cv.put("type", item.getType());
        cv.put("episodeCount", item.getEpisodeCount());
        cv.put("image", item.getImage());
        cv.put("status", item.getStatus());
        cv.put("episodesSeen", item.getEpisodesSeen());
        cv.put("rating", item.getRating());
        db.update(MySQLHelper.TABLE_ANIMELIST, cv, "id=" + item.getId(), null);
        return item;
    }
}
