package animeindex.UI.Picture;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import animeindex.BLL.BLLPicture;
import animeindex.UI.Anime.AnimeSearchActivity;
import animeindex.UI.Animelist.AnimelistActivity;
import animeindex.BE.Picture;
import animeindex.R;

public class PictureActivity extends AppCompatActivity {

    private PictureAdapter pictureAdapter;
    private BLLPicture bllPicture;
    private GridView grdPictures;
    private ImageButton btnPicture;


    private final int CAMERA_REQUEST_CODE = 0;
    private final int FULLSCREEN_REQUEST_CODE = 1;
    public static String TAG = "PICTURE_ACTIVITY";
    public static String FULLSCREEN_TAG = "FULLSCREEN";

    private String timeStamp;
    private String title;
    private File file;
    private String filename;
    private String description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        bllPicture = new BLLPicture(this);
        getWidgets();
        setUpButtons();
        setUpGrid();
        setAdapter();

    }

    //Show menu in bar
    //Will show a "textView" in menu when clicked
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_picture, menu);

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

        if(id == R.id.action_close){
            finish();
            System.exit(0);
        }

        return super.onOptionsItemSelected(item);
    }

    public void getWidgets(){
        btnPicture = (ImageButton)findViewById(R.id.btnPicture);
        grdPictures = (GridView)findViewById(R.id.grdPictures);
    }

    public void setUpButtons(){
        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TakePicture();
            }
        });
    }

    public void setUpGrid(){
     grdPictures.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             onClick((GridView) parent, view, position, id);
         }
     });
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
    public void onClick(GridView parent,
                        View v, int position, long id) {
        ArrayList<Picture> col = (ArrayList<Picture>) bllPicture.readAll();

        Intent intentFullscreen = new Intent();
        intentFullscreen.setClass(this, FullscreenActivity.class);
        intentFullscreen.putExtra(FULLSCREEN_TAG, col.get(position));

        startActivityForResult(intentFullscreen, FULLSCREEN_REQUEST_CODE);
        Log.d("asd", col.get(position).getFilename());

    }

    /**
     * starts an intent, to start the picture.
     */
    private void TakePicture() {

        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // create a file to save the image
        file = bllPicture.getOutputPhotoFile();
        // makes it possible to store the image to the set directory, set the image file name to the uri
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

        // start the image capture Intent
        startActivityForResult(pictureIntent, CAMERA_REQUEST_CODE);
    }



    /**
     * This method used to get the result back from the camera intent if it is ok, the
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if(resultCode == RESULT_OK){
                    filename = file.toString();
                    setPictureTitle();
                    if(!file.exists()){
                        Log.d("PICTURE", "THE FILE DOESN'T EXIST");
                    }
                }
                break;
            case FULLSCREEN_REQUEST_CODE:
                if(resultCode == RESULT_OK){
                    getWidgets();
                    setAdapter();
                }
        }
    }

    /**
     * This method is used to pop up a dialog to write the title of the picture
     */
    public void setPictureTitle(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                title = input.getText().toString();
                setPictureDescription();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    /**
     * This method is used to pop a dialog to write the description on the picture
     */
    public void setPictureDescription(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Description");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                description = input.getText().toString();
                Picture pic = new Picture(0, filename, title, description);
                bllPicture.add(pic);
                setAdapter();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    /**
     * sets the adapter, of the list view
     */
    private void setAdapter() {
        pictureAdapter = new PictureAdapter(this, R.layout.picture_cell, (ArrayList<Picture>) bllPicture.readAll());
        grdPictures.setAdapter(pictureAdapter);
    }


}
