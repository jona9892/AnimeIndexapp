package animeindex.Controller.Activities.Anime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import animeindex.Controller.AsyncTasks.ImageLoadTask;
import animeindex.DAL.DALC.Abstractions.ICrud;
import animeindex.DAL.DALC.Implementations.DALCAnimelist;
import animeindex.Model.Anime;
import animeindex.Model.Animelist;
import animeindex.Model.Genre;
import animeindex.R;

public class AnimeInfoActivity extends AppCompatActivity {

    //-------------------------TextViews----------------------------
    private TextView txtInfoTitle;
    private TextView txtInfoEpisodes;
    private TextView txtInfoType;
    private TextView txtInfoRating;
    private TextView txtInfoDescription;
    private TextView txtInfoStartdate;
    private TextView txtInfoEnddate;
    private ImageView imgInfoImage;
    private LinearLayout pnlAnimeInfo;
    //----------------------Buttons-----------------------------

    //--------------------Variables---------------------------
    private Anime m_anime;
    private ICrud<Animelist> animelistDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_info);
        m_anime = new Anime();
        animelistDB = new DALCAnimelist(this);
        getWidgets();

        getFromIntent();
        setInfo();
        setupButtons();
        setBackButton();
    }

    /**
     * If an item in the menu is clicked this method will handle it
     * @param item The selected menuitem
     * @return the selected menuitem
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method will set a animelist object equal value from the Intent
     */
    public void getFromIntent(){
        m_anime = (Anime) getIntent().getSerializableExtra(AnimeSearchActivity.ANIMESEARCH_TAG);
    }

    /**
     * This method is used to get all the widgets the activty uses
     */
    public void getWidgets(){
        pnlAnimeInfo = (LinearLayout)findViewById(R.id.pnlAnimeInfo);
        txtInfoTitle = (TextView)findViewById(R.id.txtInfoTitle);
        txtInfoEpisodes = (TextView)findViewById(R.id.txtInfoEpisodes);
        txtInfoType = (TextView)findViewById(R.id.txtInfoType);
        txtInfoRating = (TextView)findViewById(R.id.txtInfoRating);
        txtInfoDescription = (TextView)findViewById(R.id.txtInfoDescription);
        txtInfoDescription.setMovementMethod(new ScrollingMovementMethod());

        txtInfoStartdate = (TextView)findViewById(R.id.txtInfoStartdate);
        txtInfoEnddate = (TextView)findViewById(R.id.txtInfoEnddate);
        imgInfoImage = (ImageView)findViewById(R.id.imgInfoImage);
    }

    /**
     * This method is used to set up the buttons the activity uses
     */
    public void setupButtons(){
        Button btnAddAnime = (Button) findViewById(R.id.btnAddAnime);
        if (btnAddAnime != null) {
            btnAddAnime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!checkAnime()) {
                        addAnime();
                    }
                }
            });
        }
    }

    /**
     * This method will make an anime object and send it to the DALC to be adde to the database
     */
    private void addAnime() {
        String title = m_anime.getTitle();
        String type = m_anime.getType();
        int episodeCount = m_anime.getEpisodeCount();
        String image = m_anime.getImage();

        String status = "Watching";
        int episodesSeen = 1;
        int rating = 1;

        Animelist animelist = new Animelist(0,title, type, episodeCount, image, status, episodesSeen, rating);
        animelistDB.add(animelist);
        Log.d("AnimeInfoActivity", "Anime succesfully added to list");

        Toast.makeText(this, animelist.getTitle()+" succesfully added",
                Toast.LENGTH_SHORT).show();

    }

    private boolean checkAnime(){
        for(Animelist al : animelistDB.readAll()){
            if(al.getTitle().equals(m_anime.getTitle())){
                Toast.makeText(this, al.getTitle()+ " is already in your list",
                        Toast.LENGTH_LONG).show();
                return true;
            }
        }
        return false;
    }

    /**
     * This method will set a back button to the actionbar
     */
    public void setBackButton(){
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * This method will take the information from the intent which was set equal to an anime object,
     * and set the values to the different variables
     */
    public void setInfo(){
        txtInfoTitle.setText(m_anime.getTitle() != null ? m_anime.getTitle().toString() : "");
        txtInfoEpisodes.setText(""+m_anime.getEpisodeCount());
        txtInfoType.setText(m_anime.getType() != null ? m_anime.getType().toString() : "");
        txtInfoRating.setText(""+m_anime.getRating());
        txtInfoDescription.setText(m_anime.getDescription() != null ? m_anime.getDescription().toString() : "");
        txtInfoStartdate.setText(m_anime.getAirdate() != null ? m_anime.getAirdate().toString() : "");
        txtInfoEnddate.setText(m_anime.getEnddate() != null ? m_anime.getEnddate().toString() : "");

        //new ImageLoadTask(m_anime.getImage(), imgInfoImage).execute();

        Picasso.with(this).load(m_anime.getImage()).into(imgInfoImage);

        //Dynamically add new textview for every genre in anime object
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView tv;

        for(Genre g : m_anime.getGenre()){
            tv = new TextView(this);
            tv.setLayoutParams(params);
            tv.setTextSize(16);
            if(g.equals(m_anime.getGenre().get(m_anime.getGenre().size()-1))){
                tv.setText(g.getName());
            } else {
                tv.setText(g.getName()+", ");
            }

            pnlAnimeInfo.addView(tv);
        }


    }
}
