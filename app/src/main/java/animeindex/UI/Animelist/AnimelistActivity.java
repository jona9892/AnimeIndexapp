package animeindex.UI.Animelist;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import animeindex.BLL.BLLAnimelist;
import animeindex.UI.Anime.AnimeSearchActivity;
import animeindex.UI.Animelist.Tabhost.AnimelistTabActivity;
import animeindex.UI.Picture.PictureActivity;
import animeindex.DAL.DALC.Abstractions.ICrud;
import animeindex.DAL.DALC.Implementations.DALCAnimelist;
import animeindex.BE.Animelist;
import animeindex.R;

public class AnimelistActivity extends AppCompatActivity {
    //-------------------------Variables-----------------------
    private AnimelistAdapter listAdapter;
    private ListView lstAnimelist;
    private BLLAnimelist bllAnimelist;
    private SearchView searchView;
    private Boolean reverse = false;
    private Boolean changeGroup = false;
    //-------------------RadioButtons/group----------------------
    private RadioButton rdbAll;
    private RadioGroup radioGroup1;
    RadioGroup radioGroup2;
    //------------------TextViewz-------------------------------
    private TextView txtUnhold;
    private TextView txtPlanToWatch;
    private TextView txtCompleted;
    private TextView txtWatching;
    private TextView txtDropped;
    private TextView txtAll;
    //-------------------------ArrayLists-------------------------
    private ArrayList<Animelist> animelistArray;
    private ArrayList<Animelist> completedArray = new ArrayList<>();
    private ArrayList<Animelist> watchingArray = new ArrayList<>();
    private ArrayList<Animelist> droppedArray = new ArrayList<>();
    private ArrayList<Animelist> unholdArray = new ArrayList<>();
    private ArrayList<Animelist> planToWatchArray = new ArrayList<>();
    //--------------------ANIMESEARCH_TAG - RequestCode-------------------------
    private final int EDIT_REQUEST_CODE = 1;
    public static String ANIMELIST_TAG = "ANIMELIST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animelist);
        bllAnimelist = new BLLAnimelist(this);

        getWidgets();
        setAdapter();
        setUpList(animelistArray);
        fillArrays();
        setTextViews();

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

        switch (requestCode){
            case EDIT_REQUEST_CODE:
                if(resultCode == RESULT_OK) {
                    editAnimelist((Animelist) data.getSerializableExtra(ANIMELIST_TAG));
                    setAdapter();
                    fillArrays();
                    setTextViews();
                    rdbAll.setChecked(true);
                }
                break;
        }
    }

    //Show menu in bar
    //Will show a "textView" in menu when clicked
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_animelist, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                listAdapter.getFilter().filter(newText);

                return true;
            }
        });
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

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method will sort an array using a comparator, and sort either asc or desc
     */
    private void titleSort() {
        AnimelistComparator animeComparator = new AnimelistComparator();

        if(reverse){
            Collections.sort(animelistArray, animeComparator);
            setAdapter();
            reverse = false;
        }else {
            Collections.sort(animelistArray, Collections.reverseOrder(animeComparator));
            setAdapter();
            reverse = true;
        }
    }

    /**
     * This method is called from the cml file - Onclick on radiobuttons
     * This method will check if a radiobutton is selected, and set a new adapter populate the listview
     * @param view the radiobutton
     */
    public void setUpRadioButtons(View view){

        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()){
            case R.id.rdbAll:
                if(checked){
                    onCheckedChanged(radioGroup1);
                    setAdapter();
                    fillArrays();
                    setUpList(animelistArray);
                }
                break;
            case R.id.rdbCompleted:
                if(checked){
                    onCheckedChanged(radioGroup1);
                    setCompletedAdapter();
                    fillArrays();
                    setUpList(completedArray);
                }
                break;
            case R.id.rdbWatching:
                if(checked){
                    onCheckedChanged(radioGroup1);
                    setWatchingAdapter();
                    fillArrays();
                    setUpList(watchingArray);
                }
                break;
            case R.id.rdbDropped:
                if(checked){
                    onCheckedChanged(radioGroup2);
                    setDroppedAdapter();
                    fillArrays();
                    setUpList(droppedArray);
                }
                break;
            case R.id.rdbUnhold:
                if(checked){
                    onCheckedChanged(radioGroup2);
                    setUnholdAdapter();
                    fillArrays();
                    setUpList(unholdArray);
                }
                break;
            case R.id.rdbPlanToWatch:
                if(checked){
                    onCheckedChanged(radioGroup2);
                    setPlanToWatchAdapter();
                    fillArrays();
                    setUpList(planToWatchArray);
                }
                break;
        }
    }

    /**
     * Because i use two radiogroups, this means that when one is selected it will no be disselected
     * This method should flag the the group that is selcted, and disselected the previous selected radiogroup
     * @param group The radiogroup the button belongs to
     */
    public void onCheckedChanged(RadioGroup group){
        if (group != null && !changeGroup){
            if(group == radioGroup1){
                changeGroup = true;
                radioGroup2.clearCheck();
                changeGroup = false;
            }else if(group == radioGroup2){
                changeGroup = true;
                radioGroup1.clearCheck();
                changeGroup = false;
            }
        }
    }

    /**
     * This method should be called when the array adapter and listview needs to be updated
     */
    public void setAdapter(){
        animelistArray = (ArrayList<Animelist>) bllAnimelist.readAll();
        listAdapter = new AnimelistAdapter(this, R.layout.animelist_cell, animelistArray);
        lstAnimelist.setAdapter(listAdapter);
    }

    /**
     * This method will clear the array, and populate the listview with the array of completed
     */
    public void setCompletedAdapter(){
        completedArray.clear();
        listAdapter = new AnimelistAdapter(this, R.layout.animelist_cell, completedArray);
        lstAnimelist.setAdapter(listAdapter);
    }

    /**
     * This method will clear the array, and populate the listview with the array of watching
     */
    public void setWatchingAdapter(){
        watchingArray.clear();
        listAdapter = new AnimelistAdapter(this, R.layout.animelist_cell, watchingArray);
        lstAnimelist.setAdapter(listAdapter);
    }

    /**
     * This method will clear the array, and populate the listview with the array of dropped
     */
    public void setDroppedAdapter(){
        droppedArray.clear();
        listAdapter = new AnimelistAdapter(this, R.layout.animelist_cell, droppedArray);
        lstAnimelist.setAdapter(listAdapter);
    }

    /**
     * This method will clear the array, and populate the listview with the array of unhold
     */
    public void setUnholdAdapter(){
        unholdArray.clear();
        listAdapter = new AnimelistAdapter(this, R.layout.animelist_cell, unholdArray);
        lstAnimelist.setAdapter(listAdapter);
    }

    /**
     * This method will clear the array, and populate the listview with the array of plan to watch
     */
    public void setPlanToWatchAdapter(){
        planToWatchArray.clear();
        listAdapter = new AnimelistAdapter(this, R.layout.animelist_cell, planToWatchArray);
        lstAnimelist.setAdapter(listAdapter);
    }

    /**
     * This method should fill the different arrays with right objects from status
     */
    public void fillArrays(){
        completedArray.clear();
        watchingArray.clear();
        unholdArray.clear();
        droppedArray.clear();
        planToWatchArray.clear();
        //Fill array with the animelist objects that contains a status: completed
        for(Animelist animel : bllAnimelist.readAll()){
            if(animel.getStatus().equals("Completed")){
                completedArray.add(animel);
            }
        }

        //Fill array with the animelist objects that contains a status: watching
        for(Animelist animel : bllAnimelist.readAll()) {
            if (animel.getStatus().equals("Watching")) {
                watchingArray.add(animel);
            }
        }

        //Fill array with the animelist objects that contains a status: unhold
        for(Animelist animel : bllAnimelist.readAll()) {
               if (animel.getStatus().equals("Unhold")) {
                    unholdArray.add(animel);
               }
        }

        //Fill array with the animelist objects that contains a status: dropped
        for(Animelist animel : bllAnimelist.readAll()) {
            if (animel.getStatus().equals("Dropped")) {
                droppedArray.add(animel);
            }
        }

        //Fill array with the animelist objects that contains a status: dropped
        for(Animelist animel : bllAnimelist.readAll()) {
            if (animel.getStatus().equals("Plan to watch")) {
                planToWatchArray.add(animel);
            }
        }
    }

    /**
     * This method will get all the widgets this activity needs to use
     */
    public void getWidgets(){
        lstAnimelist = (ListView)findViewById(R.id.lstAnimelist);

        rdbAll = (RadioButton)findViewById(R.id.rdbAll);
        radioGroup1 = (RadioGroup)findViewById(R.id.radioGroup);
        radioGroup2 = (RadioGroup)findViewById(R.id.radioGroup2);

        txtCompleted = (TextView)findViewById(R.id.txtCompleted);
        txtWatching = (TextView)findViewById(R.id.txtWatching);
        txtDropped = (TextView)findViewById(R.id.txtDropped);
        txtAll = (TextView)findViewById(R.id.txtAll);
        txtUnhold = (TextView)findViewById(R.id.txtUnhold);
        txtPlanToWatch = (TextView)findViewById(R.id.txtPlanToWatch);
    }

    /**
     * This method will set the number of object inside a specific array
     */
    @SuppressLint("SetTextI18n")
    public void setTextViews(){
        txtCompleted.setText("("+completedArray.size() +")");
        txtWatching.setText("("+ watchingArray.size()+")");
        txtDropped.setText("(" + droppedArray.size() + ")");
        txtAll.setText("("+ animelistArray.size()+")");
        txtUnhold.setText("("+ unholdArray.size()+")");
        txtPlanToWatch.setText("(" + planToWatchArray.size() + ")");
    }

    /**
     * This method is used to set up listviews
     */
    private void setUpList(final ArrayList<Animelist> arrayType){
        lstAnimelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClick((ListView) parent, view, position, id, arrayType);
            }
        });
    }

    /**
     * This method is used to get a specific Animelist object and send it to DALV to be updated
     * @param alist the animelist object to be edited
     */
    private void editAnimelist(Animelist alist) {
        bllAnimelist.update(alist);
        Toast.makeText(this, alist.getTitle()+" is updated",
                Toast.LENGTH_LONG).show();
    }

    /**
     * This method is used when ever an element in the listview is clicked
     * When an element is clicked it will open a new actvity, with an Animelist object out from
     * the position of the arraylist.
     * @param parent The listview
     * @param v The view
     * @param position The postion of the element
     * @param id the id
     */
    public void onClick(ListView parent,
                        View v, int position, long id, ArrayList<Animelist> arrayType) {

        ArrayList<Animelist> col = arrayType;
        Intent intent = new Intent();
        intent.setClass(this, AnimelistEditActivity.class);
        intent.putExtra(ANIMELIST_TAG, col.get(position));

        startActivityForResult(intent, EDIT_REQUEST_CODE);
    }
}
