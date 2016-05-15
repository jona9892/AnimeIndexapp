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
import animeindex.BE.Picture;

/**
 * Created by JonathansPC on 26-04-2016.
 */
public class DALCPictures implements ICrud<Picture> {

    private Context context;
    private SQLiteDatabase db;
    private SQLiteStatement insertStmt;
    private static final String INSERT = "insert into " + MySQLHelper.TABLE_PICTURES
            + "(filename, date, title, description) values (?,?,?,?)";

    public DALCPictures(Context context) {
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
    public Picture add(Picture item) {
        this.insertStmt.bindString(1, item.getFilename());
        this.insertStmt.bindString(2, item.getDate());
        this.insertStmt.bindString(3, item.getTitle());
        this.insertStmt.bindString(4, item.getDescription());

        item.setId((int) insertStmt.executeInsert());
        return item;
    }

    /**
     * This method will get a specific animelist object from the database
     * @param id the object containing this id
     * @return the animelist object
     */
    @Override
    public Picture read(int id) {
        List<Picture> list = new ArrayList<Picture>();
        Cursor cursor = db.query(MySQLHelper.TABLE_PICTURES, new String[] { "Id", "filename", "date", "title", "description"},
                "id = " + id, null, null, null, "title asc");
        if (cursor.moveToFirst()) {
            do {
                list.add(new Picture(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
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
    public Collection<Picture> readAll() {
        List<Picture> list = new ArrayList<Picture>();
        Cursor cursor = db.query(MySQLHelper.TABLE_PICTURES, new String[] { "Id", "filename", "date", "title", "description"},
                null, null, null, null, "title asc");
        if (cursor.moveToFirst()) {
            do {
                list.add(new Picture(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    @Override
    public void delete(int id) {
        db.delete(MySQLHelper.TABLE_PICTURES, "id=" + id, null);
    }

    /**
     * This method will update a specific animelist object
     * @param item the specific animelist object
     * @return the specific animelist
     */
    @Override
    public Picture update(Picture item) {
        ContentValues cv = new ContentValues();
        cv.put("filename", item.getFilename());
        cv.put("date", item.getDate());
        cv.put("title", item.getTitle());
        cv.put("description", item.getDescription());
        db.update(MySQLHelper.TABLE_PICTURES, cv, "id=" + item.getId(), null);
        return item;
    }
}