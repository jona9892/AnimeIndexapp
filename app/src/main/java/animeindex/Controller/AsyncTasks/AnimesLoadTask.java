package animeindex.Controller.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import animeindex.Controller.Activities.Anime.AnimeSearchActivity;
import animeindex.DAL.Repositories.AnimeRepository;
import animeindex.Model.Anime;

/**
 * Created by jona9892 on 10-04-2016.
 */
public class AnimesLoadTask extends AsyncTask<
        AnimeRepository, // collection of PoliceDistricts to execute
        Void, // to type of progress info
        ArrayList<Anime>> // output of doInBackground
{

    AnimeSearchActivity m_context;

    public AnimesLoadTask(AnimeSearchActivity context)
    {
        m_context = context;
    }



    @Override
    protected ArrayList<Anime> doInBackground(AnimeRepository... params) {
       // params[0].getPage(1);
        return null;

    }

    // onPostExecute displays the results of the AsyncTask.doInBackground().
    // this method is invoked by the GUI thread
    @Override
    protected void onPostExecute(final ArrayList<Anime> animes) {
        //m_context.initializeData(animes);
    }
}
