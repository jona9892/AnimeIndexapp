package animeindex.Controller.Activities.Anime;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import animeindex.Controller.Activities.Animelist.AnimelistActivity;
import animeindex.Controller.Activities.Animelist.Tabhost.AnimelistTabActivity;
import animeindex.Controller.Activities.Comparator.AnimeTitleComparator;
import animeindex.Controller.Activities.Picture.PictureActivity;
import animeindex.Controller.AsyncTasks.ImageLoadTask;
import animeindex.Controller.AsyncTasks.AnimesLoadTask;
import animeindex.Model.Genre;
import animeindex.R;

import java.util.ArrayList;
import java.util.Collections;

import animeindex.DAL.Repositories.AnimeRepository;
import animeindex.Model.Anime;

public class AnimeSearchActivity extends AppCompatActivity {


    private AnimeAdapter animeAdapter;
    private ListView lstAnimes;
    private SearchView searchView;
    private ArrayList<Anime> animeArray;
    private Boolean reverse = true;
    public static String ANIMESEARCH_TAG = "ANIME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_search);
        getWidgets();
        AnimesLoadTask t = new AnimesLoadTask(this);
        t.execute(new AnimeRepository());
    }

    //Show menu in bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * If an item in the menu is clicked this method will handle it
     * @param item The selected menuitem
     * @return the selected menuitem
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sort)
        {
            titleSort();
            return true;
        }
        if(id == R.id.menuItemAnimelist){
            Intent intent = new Intent();
            intent.setClass(this, AnimelistActivity.class);
            startActivity(intent);
        }

        if(id == R.id.menuItemAnimeSearch){
            Intent intent = new Intent();
            intent.setClass(this, AnimeSearchActivity.class);
            startActivity(intent);
        }
        if(id == R.id.menuItemAlbum){
            Intent intent = new Intent();
            intent.setClass(this, PictureActivity.class);

            startActivity(intent);
        }

        if(id == R.id.menuItemTabs){
            Intent intent = new Intent();
            intent.setClass(this, AnimelistTabActivity.class);

            startActivity(intent);
        }

        if(id == R.id.action_close){
            finish();
            System.exit(0);
        }

        if(id == R.id.searchActionBarItem){
            Intent searchIntent = new Intent(this, SearchActivity.class);
            searchIntent.putExtra(ANIMESEARCH_TAG, animeArray);
            startActivity(searchIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method will sort an array using a comparator, and sort either asc or desc
     */
    private void titleSort() {
        AnimeTitleComparator animeComparator = new AnimeTitleComparator();

        if(reverse){
            Collections.sort(animeArray, animeComparator);
            setAdapter();
            reverse = false;
        }else {
            Collections.sort(animeArray, Collections.reverseOrder(animeComparator));
            setAdapter();
            reverse = true;
        }

        Log.d("ANIMELIST_TAG", "" + animeArray.get(0).getTitle());

    }

    /**
     * Gets all the widget.
     */
    private void getWidgets(){
        lstAnimes = (ListView)findViewById(R.id.lstAnimes);
    }

    /**
     * Sets the adapter to the listview
     * Call this to update the listview
     */
    private void setAdapter() {
        animeAdapter = new AnimeAdapter(this, R.layout.anime_search_cell, animeArray);
        lstAnimes.setAdapter(animeAdapter);
    }

    /**
     * This is used to get all the widgets the arrayadapter and listview needs
     * So that they only get called one time
     */
    public static class ViewHolder {
        public TextView title;
        public TextView episodes;
        public ImageView imgImage;
        public TextView type;
        public TextView status;
        public TextView description;
        public ImageLoadTask ilt;

    }

    class AnimeAdapter extends ArrayAdapter<Anime>
    {
        Context context;
        public AnimeAdapter(Context context, int resource, ArrayList<Anime> animes) {
            super(context, resource, animes);
            this.context = context;
        }

        @SuppressLint({"SetTextI18n", "InflateParams"})
        @Override
        public View getView(int position, View v, ViewGroup parent) {
            ViewHolder holder;
            if (v == null) {
                LayoutInflater li = (LayoutInflater) getContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                v = li.inflate(R.layout.anime_search_cell, null);
                Log.d("LIST", "Position: " + position + " View created");

                holder = new ViewHolder();
                holder.title = (TextView) v.findViewById(R.id.txtTitle);
                holder.episodes = (TextView)v.findViewById(R.id.txtEpisode);
                holder.status = (TextView) v.findViewById(R.id.txtStatus);
                holder.type = (TextView)v.findViewById(R.id.txtType);
                holder.description = (TextView)v.findViewById(R.id.txtDescrption);
                holder.imgImage = (ImageView)v.findViewById(R.id.imgImage);

                v.setTag(holder);
            }
            else
                Log.d("LIST", "Position: " + position + " View Reused");
            holder = (ViewHolder) v.getTag();

            Anime anime = getItem(position);

            holder.title.setText(" " + anime.getTitle());
            holder.episodes.setText("" + " " + anime.getEpisodeCount());
            holder.type.setText(" " + anime.getType());
            holder.status.setText(" " + anime.getStatus());
            holder.description.setText(" " + anime.getDescription());
            //holder.ilt = new ImageLoadTask(anime.getImage(), holder.imgImage);
            Picasso.with(context).load(anime.getImage()).into(holder.imgImage);
//            holder.ilt.execute();

            return v;
        }
    }

    /**
     *
     * @param m_animes the arraylist of all the animes from the database
     */
    public void initializeData(final ArrayList<Anime> m_animes)
    {
        animeArray = m_animes;
        setAdapter();
        Log.d(ANIMESEARCH_TAG, "Adapter attached");
        onClick();
    }

    public void onClick(){
        lstAnimes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ArrayList<Anime> col = animeArray;

                Intent intent = new Intent();
                intent.setClass(AnimeSearchActivity.this, AnimeInfoActivity.class);
                intent.putExtra(ANIMESEARCH_TAG, col.get(position));
                startActivity(intent);

            }
        });
    }
}
