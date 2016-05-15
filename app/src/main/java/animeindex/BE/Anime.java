package animeindex.BE;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jona9892 on 09-04-2016.
 */
public class Anime implements Serializable {
    private String title;
    private String type;
    private String status;
    private String description;
    private int rating;
    private String airdate;
    private String enddate;
    private int episodeCount;
    private String image;
    private ArrayList<Genre> genre;

    public Anime(){

    }

    public Anime(String title, String type, String status, String description,
                 ArrayList<Genre> genre, String image, int episodeCount, String airdate , String enddate) {
        this.title = title;
        this.type = type;
        this.status = status;
        this.description = description;

        this.genre = genre;
        this.image = image;
        this.episodeCount = episodeCount;
        this.enddate = enddate;
        this.airdate = airdate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getAirdate() {
        return airdate;
    }

    public void setAirdate(String airdate) {
        this.airdate = airdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public int getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(int episodeCount) {
        this.episodeCount = episodeCount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public ArrayList<Genre> getGenre() {
        return genre;
    }

    public void setGenre(ArrayList<Genre> genre) {
        this.genre = genre;
    }
}
