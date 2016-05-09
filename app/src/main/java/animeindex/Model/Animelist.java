package animeindex.Model;

import java.io.Serializable;

/**
 * Created by JonathansPC on 24-04-2016.
 */
public class Animelist implements Serializable {


    private int id;
    private String title;
    private String type;
    private String status;
    private String image;
    private int rating;
    private int episodeCount;
    private int episodesSeen;

    public Animelist(){

    }

    public Animelist(int id, String title, String type,int episodeCount,String image, String status, int episodesSeen, int rating  ) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.status = status;
        this.image = image;
        this.rating = rating;
        this.episodeCount = episodeCount;
        this.episodesSeen = episodesSeen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(int episodeCount) {
        this.episodeCount = episodeCount;
    }

    public int getEpisodesSeen() {
        return episodesSeen;
    }

    public void setEpisodesSeen(int episodesSeen) {
        this.episodesSeen = episodesSeen;
    }
}
