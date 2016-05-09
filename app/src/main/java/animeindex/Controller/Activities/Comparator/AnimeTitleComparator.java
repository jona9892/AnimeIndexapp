package animeindex.Controller.Activities.Comparator;

import java.util.Comparator;

import animeindex.Model.Anime;

/**
 * Created by JonathansPC on 24-04-2016.
 */
public class AnimeTitleComparator implements Comparator<Anime>
    {
        public int compare(Anime left, Anime right) {
            return left.getTitle().compareTo(right.getTitle());
        }
    }
