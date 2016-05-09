package animeindex.Controller.Activities.Animelist;

import android.widget.ImageView;
import android.widget.TextView;

import animeindex.Controller.AsyncTasks.ImageLoadTask;

/**
 * Created by JonathansPC on 28-04-2016.
 */
public class ViewHolderAnimelist {
    TextView txtAnimelistTitle;
    TextView txtAnimelistType;
    TextView txtNumber;
    TextView txtAnimelistStatus;

    TextView txtAnimelistEpisodesSeen;
    TextView txtAnimelistEpisodeCount;
    TextView txtAnimelistRating;
    ImageView imgAnimelistImage;
    ImageLoadTask ilt;
}
