package animeindex.Controller.Activities.Picture;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import animeindex.Controller.Activities.Anime.AnimeSearchActivity;
import animeindex.Controller.Activities.Animelist.AnimelistActivity;
import animeindex.DAL.DALC.Abstractions.ICrud;
import animeindex.DAL.DALC.Implementations.DALCPictures;
import animeindex.Model.Picture;
import animeindex.R;

public class PictureActivity extends AppCompatActivity {

    private PictureAdapter pictureAdapter;
    private ICrud<Picture> pictureDb;
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
        pictureDb = new DALCPictures(this);
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
        ArrayList<Picture> col = (ArrayList<Picture>) pictureDb.readAll();

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
        file = getOutputPhotoFile();
        // makes it possible to store the image to the set directory, set the image file name to the uri
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

        // start the image capture Intent
        startActivityForResult(pictureIntent, CAMERA_REQUEST_CODE);
    }

    /**
     * This will get file from the picture, and set the file info
     * @return the file
     */
    private File getOutputPhotoFile() {
        //This will create a directory on the phone where the photos will be stored.
        //This will save the picture on the phone, and for other app to get it
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "AnimeIndex");

        //This checks whether the directory that should be created is created.
        if (!mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d(TAG,"failed to create directory");
                return null;
            }
        }

        //This sets a date for when the picture is taking.
        //We need this to create a unique picture, otherwise it will get overridden
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        //This creates a file with the the directory path, img to show its a image, date to make it unique and the type of the image
        File mediaFile = new File(mediaStorageDir.getPath() +
                File.separator + "img" +
                "_"+ timeStamp + "." + "jpg");

        return mediaFile;
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
                Picture pic = new Picture(0, filename, timeStamp, title, description);
                pictureDb.add(pic);
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


    //----------------------------------------------------------------------------------------------

    /**
     * sets the adapter, of the list view
     */
    private void setAdapter() {
        pictureAdapter = new PictureAdapter(this, R.layout.picture_cell, (ArrayList<Picture>) pictureDb.readAll());
        grdPictures.setAdapter(pictureAdapter);
    }


    /**
     * This is used to get all the widgets the arrayadapter and listview needs
     * So that they only get called one time
     */
    public static class ViewHolder {
        TextView txtPictureTitle;
        ImageView imgPicture;
    }

    /**
     * The arrayadapter for animelist
     */
    class PictureAdapter extends ArrayAdapter<Picture>
    {
        public PictureAdapter(Context context, int resource, ArrayList<Picture> pictures) {
            super(context, resource, pictures);
        }

        private void showPictureTaken(String filename, ImageView myImage) {

            File f = new File(filename);
            if (!f.exists()) {
                return;
            }
            myImage.setImageURI(Uri.fromFile(f));
            myImage.setBackgroundColor(Color.WHITE);
            myImage.setRotation(90);
            scaleImage(myImage);
        }

        //This will scale the image
        private void scaleImage(ImageView myImage)
        {
            final Display display = getWindowManager().getDefaultDisplay();
            Point p = new Point();
            display.getSize(p);
            myImage.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {
            ViewHolder holder;
            if (v == null) {
                LayoutInflater li = (LayoutInflater) getContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                v = li.inflate(R.layout.picture_cell, null);
                Log.d("LIST", "Position: " + position + " View created");

                holder = new ViewHolder();
                holder.txtPictureTitle = (TextView) v.findViewById(R.id.txtPictureTitle);
                holder.imgPicture = (ImageView) v.findViewById(R.id.imgPicture);

                v.setTag(holder);
            }
            else
                Log.d("LIST", "Position: " + position + " View Reused");
            holder = (ViewHolder) v.getTag();

            Picture picture = getItem(position);
            holder.txtPictureTitle.setText("" + picture.getTitle());
            showPictureTaken(picture.getFilename() != null ? picture.getFilename() : "", holder.imgPicture);

            return v;
        }


    }
}
