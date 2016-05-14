package animeindex.Controller.Activities.Animelist;

        import android.app.Activity;
        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.Spinner;
        import android.widget.TextView;

        import com.squareup.picasso.Picasso;

        import java.util.ArrayList;

        import animeindex.Controller.Activities.Animelist.Tabhost.AnimelistCompleted;
        import animeindex.Controller.Activities.Animelist.Tabhost.AnimelistDropped;
        import animeindex.Controller.Activities.Animelist.Tabhost.AnimelistTabActivity;
        import animeindex.Controller.Activities.Animelist.Tabhost.AnimelistWatching;
        import animeindex.Controller.AsyncTasks.ImageLoadTask;
        import animeindex.Model.Animelist;
        import animeindex.R;

public class AnimelistEditActivity extends AppCompatActivity {
    //-------------------Spinner-------------------
    private Spinner spnStatus;
    private Spinner spnEpisodesSeen;
    private Spinner spnRating;
    //------------------Buttons--------------------
    private Button btnSave;
    private Button btnCancel;
    //----------------Textview - Imageview---------
    private TextView txtAnimeEditTitle;
    private ImageView imgEditImage;
    //-----------Variables-------------------------
    private Animelist m_animelist;
    //---------------------------------------------
 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animelist_edit);
        getFromIntent();
        getWidgets();
        setUpSpinners();
        setUpButtons();
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
        //obtain  Intent Object send  from SenderActivity
        Intent intent = this.getIntent();
        m_animelist = (Animelist) getIntent().getSerializableExtra((AnimelistActivity.ANIMELIST_TAG));
    }

    /**
     * This method is used to get all the widgets the activty uses
     */
    public void getWidgets(){
        spnEpisodesSeen = (Spinner)findViewById(R.id.spnEpisodesSeen);
        spnStatus = (Spinner)findViewById(R.id.spnStatus);
        spnRating = (Spinner)findViewById(R.id.spnRating);
        btnCancel = (Button)findViewById(R.id.btnCancel);
        btnSave = (Button)findViewById(R.id.btnSave);
        txtAnimeEditTitle = (TextView)findViewById(R.id.txtEditAnimeTitle);
        imgEditImage = (ImageView)findViewById(R.id.imgEditImage);

        txtAnimeEditTitle.setText(m_animelist.getTitle());

        imgEditImage.setScaleType(ImageView.ScaleType.FIT_XY);
        //new ImageLoadTask(m_animelist.getImage(), imgEditImage).execute();
        Picasso.with(this).load(m_animelist.getImage()).into(imgEditImage);

    }

    /**
     * This method is used to set up the buttons the activity uses
     */
    public void setUpButtons(){
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit();
            }
        });
    }

    /**
     * This method will send a result back which is canceld, and use finish funtion to close the activity
     */
    private void cancel() {
        setResult(Activity.RESULT_CANCELED);
        finish();
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
     * This method is used to populate and set spinners the activity uses
     */
    public void setUpSpinners(){
        //----------------------Status Spinner---------------------------------
        String compareStatus = m_animelist.getStatus();
        String[] statuses = {"Watching", "Completed", "Dropped", "Unhold", "Plan to watch"};
        ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(this, R.layout.spinner_layout, statuses);
        spnStatus.setAdapter(adapterStatus);

        if (!compareStatus.equals(null)) {
            int spinnerPosition = adapterStatus.getPosition(compareStatus);
            spnStatus.setSelection(spinnerPosition);
        }
        ///--------------------------------Rating Spinner-------------------------------------------
        int compareRating = m_animelist.getRating();
        Log.d("ratings", ""+compareRating);
        Integer[] ratings = {1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapterRating = new ArrayAdapter<Integer>(this, R.layout.spinner_layout, ratings);

        spnRating.setAdapter(adapterRating);
        spnRating.setSelection(compareRating-1);
        //-----------------------------------Episodes seen Spinner-----------------------------------
        int compareEpisode = m_animelist.getEpisodesSeen();
        ArrayList<Integer> episodes = getEpisodes();
        ArrayAdapter<Integer> adapterEpisode = new ArrayAdapter<Integer>(this, R.layout.spinner_layout, episodes);
        spnEpisodesSeen.setAdapter(adapterEpisode);
        spnEpisodesSeen.setSelection(compareEpisode);
    }

    /**
     * This method sets an arraylist with integers, to use for the episodes spinner
     * @return Arraylist with integers
     */
    private ArrayList<Integer> getEpisodes() {
        int episodes = m_animelist.getEpisodeCount();

        ArrayList<Integer> result = new ArrayList<Integer>();

        //puts the values in the arraylist from 0 to the number of episodes the anime contains
        for (int i = 0; i <= episodes; i++) {
            result.add(i);
        }
        return result;
    }

    /**
     * This method is used edit an animelist object.
     * Sets variable equal to the right value, makes an Animelist object, and sends the object
     * through the Intent back to the animelistActivity with a ANIMESEARCH_TAG
     */
    public void edit(){
        String title = m_animelist.getTitle();
        String type = m_animelist.getType();
        int episodeCount = m_animelist.getEpisodeCount();
        String image = m_animelist.getImage();

        int episodesSeen = (Integer)spnEpisodesSeen.getSelectedItem();
        String status = spnStatus.getSelectedItem().toString();
        int rating = (Integer)spnRating.getSelectedItem();
        Animelist al = new Animelist(m_animelist == null ? 0: m_animelist.getId(),title, type, episodeCount, image, status, episodesSeen, rating);

        Intent intent = new Intent();
        intent.putExtra(AnimelistActivity.ANIMELIST_TAG, al);

        setResult(RESULT_OK, intent);
        finish();

    }


}
