package animeindex.Controller.Activities.Animelist;

import java.util.Comparator;

import animeindex.Model.Animelist;

/**
 * Created by JonathansPC on 24-04-2016.
 */
public class AnimelistComparator implements Comparator<Animelist>
{
    public int compare(Animelist left, Animelist right) {
        return left.getTitle().compareTo(right.getTitle());
    }
}