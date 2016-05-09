package animeindex.Controller.Activities.Animelist.Tabhost;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import animeindex.Controller.Activities.Animelist.AnimelistAdapter;
import animeindex.Controller.Activities.Animelist.AnimelistEditActivity;
import animeindex.DAL.DALC.Abstractions.ICrud;
import animeindex.DAL.DALC.Implementations.DALCAnimelist;
import animeindex.Model.Animelist;
import animeindex.R;

public class AnimelistDropped extends ListActivity {
    private ICrud<Animelist> animelistDB;
    private AnimelistAdapter listAdapter;
    private ArrayList<Animelist> droppedArray = new ArrayList<>();

    //--------------------ANIMESEARCH_TAG - RequestCode-------------------------
    private final int EDIT_REQUEST_CODE = 0;
    public static String ANIME_DROPPED_TAG = "ANIMELISTDROPPED";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        animelistDB = new DALCAnimelist(this);
        setAdapter();
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
                    editAnimelist((Animelist)data.getSerializableExtra(ANIME_DROPPED_TAG));
                    setAdapter();
                }
                else {

                }
                break;
        }

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        onClick(l, v, position, id);
    }

    public void setAdapter(){
        //Fill array with the animelist objects that contains a status: completed
        for(Animelist animel : animelistDB.readAll()) {
            if (animel.getStatus().equals("Dropped")) {
                droppedArray.add(animel);
            }
        }
        listAdapter = new AnimelistAdapter(this, R.layout.animelist_cell, droppedArray);
        this.setListAdapter(listAdapter);
    }

    /**
     * This method is used to get a specific Animelist object and send it to DALV to be updated
     * @param alist
     */
    private void editAnimelist(Animelist alist) {
        animelistDB.update(alist);
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
                        View v, int position, long id) {

        ArrayList<Animelist> col = droppedArray;
        /*String name = col.get(position).getName();
        int phoneNumber = col.get(position).getPhoneNumber();
        String email = col.get(position).getEmail();
        String address = col.get(position).getAddress();
        String url = col.get(position).getUrl();*/

        Intent intent = new Intent();
        intent.setClass(this, AnimelistEditActivity.class);
        intent.putExtra(ANIME_DROPPED_TAG, col.get(position));

        startActivityForResult(intent, EDIT_REQUEST_CODE);
    }

}
