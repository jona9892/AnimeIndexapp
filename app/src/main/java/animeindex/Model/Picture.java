package animeindex.Model;

import java.io.Serializable;

/**
 * Created by JonathansPC on 26-04-2016.
 */
public class Picture implements Serializable{
    String filename;
    String date;
    String title;
    String description;
    int Id;

    public Picture(int id, String filename, String date, String title, String description ) {
        this.title = title;
        this.description = description;
        this.filename = filename;
        this.date = date;
        Id = id;
    }

    public int getId() {
        return Id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getFilename() {

        return filename;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}