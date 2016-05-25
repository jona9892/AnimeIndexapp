package animeindex.BE;

import java.io.Serializable;

/**
 * Created by JonathansPC on 26-04-2016.
 */
public class Picture implements Serializable{
    String filename;
    String title;
    String description;
    int Id;

    public Picture(int id, String filename, String title, String description ) {
        this.title = title;
        this.description = description;
        this.filename = filename;
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
}
