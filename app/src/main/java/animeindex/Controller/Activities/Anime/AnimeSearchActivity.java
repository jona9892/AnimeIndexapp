package animeindex.Controller.Activities.Anime;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import animeindex.Controller.Activities.Animelist.AnimelistActivity;
import animeindex.Controller.Activities.Animelist.Tabhost.AnimelistTabActivity;
import animeindex.Controller.Activities.Picture.PictureActivity;
import animeindex.DAL.ServiceGateway.Abstraction.IGateway;
import animeindex.R;

import java.util.ArrayList;
import java.util.List;

import animeindex.DAL.ServiceGateway.Implementation.AnimeGateway;
import animeindex.Model.Anime;

public class AnimeSearchActivity extends AppCompatActivity {


    private AnimeAdapter animeAdapter;
    private ListView lstAnimes;
    private Boolean reverse = true;
    public static String ANIMESEARCH_TAG = "ANIME";
    IGateway m_data;


    int nextPage = 1;
    boolean loading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_search);
        getWidgets();
        setAdapter();
        m_data = new AnimeGateway();
        setUpListViews();

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

        return super.onOptionsItemSelected(item);
    }

    /**
     * Gets all the widget.
     */
    private void getWidgets(){
        lstAnimes = (ListView)findViewById(R.id.lstAnimes);
    }

    /**
     * This method sets up the listview listeners
     */
    public void setUpListViews(){
        lstAnimes.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                boolean loadMore =
                        firstVisibleItem + visibleItemCount >= totalItemCount;

                if(loadMore && !loading) {
                    Log.d("AnimeSearch", "onScroll page to load = " + nextPage);
                    new LoadDataTask().execute(nextPage);
                    loading = true;
                    nextPage++;
                }

            }
        });

        //The setOnItemListener
        lstAnimes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClick((ListView)parent, view, position, id);
            }
        });
    }

    /**
     * This method is called when th user clicks on an anime
     * It will start a new intent with the positions of the anime objects in the array
     * @param parent the listview
     * @param v the view
     * @param position the position
     * @param id the id
     */
    public void onClick(ListView parent,
                        View v, int position, long id) {
        ArrayList<Anime> col = m_data.getAll();
        Log.d("asd", col.get(position).getTitle());

        Intent intent = new Intent();
        intent.setClass(AnimeSearchActivity.this, AnimeInfoActivity.class);
        intent.putExtra(ANIMESEARCH_TAG, col.get(position));
        startActivity(intent);
    }

    /**
     * Sets the adapter to the listview
     * Call this to update the listview
     */
    private void setAdapter() {
        animeAdapter = new AnimeAdapter(this);
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

    }

    /**
     * This class extends arrayAdapter which is going to be used for displaying data in listview
     */
    class AnimeAdapter extends ArrayAdapter<Anime>
    {
        Context context;
        public AnimeAdapter(Context context) {
            super(context, R.layout.anime_search_cell);
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
            Picasso.with(context).load(anime.getImage()).into(holder.imgImage);

            return v;
        }
    }

    /**
     * This class takes care of getting the data from the api, and be able to display the data
     * It needs three generic types: The Integer is the type of the parameter sen to the task when executed
     * The void is void because that is unused. It would be fx Integer if the progress needs to be specified
     * The List<Anime> is the result of the doInBackground.
     *
     */
    class LoadDataTask extends AsyncTask<Integer, Void, List<Anime>> {

        /**
         * This method will return a List of Anime getting the data from api
         * Use this for operations that takes some time, fx gets a lot of data
         * @param page the page of api
         * @return list
         */
        @Override
        protected List<Anime> doInBackground(Integer... page) {
            return m_data.getPage(page[0]);
        }

        /**
         * This method will after getting the data, add the data to arrayAdapter and display it
         * @param animes list containing th data
         */
        @Override
        protected void onPostExecute(List<Anime> animes) {
            Log.d("Repo", ""+animes.size());
            animeAdapter.addAll(animes);
            loading = false;
        }
    }
}
