package animeindex.UI.Animelist.Tabhost;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TabHost;

import animeindex.R;

public class AnimelistTabActivity extends AppCompatActivity {

    TabHost host;
    private SearchView searchView;
    private static final String COMPLETED_SPEC ="Completed";
    private static final String Watching_SPEC ="Watching";
    private static final String Dropped_SPEC ="Dropped";
    public static String ANIME_COMPLETED_TAG = "ANIMELISTCOMPLETED";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animelist_tab);
        setUpTabs(savedInstanceState);
        setUpTabCompleted();
        setUpTabWatching();
        setUpTabDropped();
        setBackButton();


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

        if (id==android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * The method that will be called when the started activity returns
     * @param requestCode the code of the activity
     * @param resultCode how the action went
     * @param data the information
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

                if(resultCode == RESULT_OK) {
                    Log.d("Thiss", "tabactivty");

                }
                else {

                }



    }
    /**
     * This method will set a back button to the actionbar
     */
    public void setBackButton(){
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void setUpTabs(Bundle savedInstanceState){
        host =  (TabHost)findViewById(R.id.tabHost);
        LocalActivityManager mlam = new LocalActivityManager(this, false);
        mlam.dispatchCreate(savedInstanceState);
        host.setup(mlam);
    }

    public void setUpTabCompleted(){
        //Tab 1
        TabHost.TabSpec specCompleted = host.newTabSpec(COMPLETED_SPEC);
        Intent completedIntent = new Intent(this, AnimelistCompleted.class);
        specCompleted.setContent(completedIntent);
        specCompleted.setIndicator("Completed");
        host.addTab(specCompleted);
    }

    public void setUpTabWatching(){
        //Tab 2
        TabHost.TabSpec specWatching = host.newTabSpec(Watching_SPEC);
        Intent watchingIntent = new Intent(this, AnimelistWatching.class);
        specWatching.setContent(watchingIntent);
        specWatching.setIndicator("Watching");
        host.addTab(specWatching);
    }

    public void setUpTabDropped(){
        //Tab 3
        TabHost.TabSpec specDropped = host.newTabSpec(Dropped_SPEC);
        Intent droppedIntent = new Intent(this, AnimelistDropped.class);

        specDropped.setContent(droppedIntent);
        specDropped.setIndicator("Dropped");
        host.addTab(specDropped);
    }
}
