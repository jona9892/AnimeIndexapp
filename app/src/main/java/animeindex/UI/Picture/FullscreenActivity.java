package animeindex.UI.Picture;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import animeindex.BLL.BLLPicture;
import animeindex.DAL.DALC.Abstractions.ICrud;
import animeindex.DAL.DALC.Implementations.DALCPictures;
import animeindex.BE.Picture;
import animeindex.R;

public class FullscreenActivity extends AppCompatActivity {

    ImageView imgFSPicture;
    TextView txtFSTitle;
    Picture m_picture;
    TextView txtFSDescription;
    Button btnDelete;
    private BLLPicture bllPicture;

    String description;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        bllPicture = new BLLPicture(this);
        getFromIntent();
        getWidgets();
        setWidgets();
        setUpButtons();
        setUpTextViews();
        back();
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
     * This method will set a back button to the actionbar
     */
    public void setBackButton(){
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * When going back to mainActivity, will set the Result to ok
     */
    private void back(){
        setResult(RESULT_OK);
    }

    /**
     * This method gets the data from previous activty, and sets it to a picture object.
     */
    public void getFromIntent(){
        m_picture = (Picture) getIntent().getSerializableExtra(PictureActivity.FULLSCREEN_TAG);
    }

    public void getWidgets(){
        imgFSPicture = (ImageView) findViewById(R.id.imgFSPicture);
        txtFSTitle = (TextView)findViewById(R.id.txtFSTitle);
        txtFSDescription = (TextView)findViewById(R.id.txtFSDescription);
        btnDelete = (Button) findViewById(R.id.btnDelete);
    }

    public void setWidgets(){
        showInFullscreen(m_picture.getFilename(), imgFSPicture);
        txtFSTitle.setText(""+ m_picture.getTitle());
        txtFSDescription.setText(""+m_picture.getDescription());
    }

    /**
     * This method set up the listeners for the buttons, and also alertdialog for the deletion.
     */
    public void setUpButtons(){
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(FullscreenActivity.this);
                builder.setTitle("Are you sure, you wanna delete this picture?");
                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bllPicture.delete(m_picture.getId());
                        File fdelete = new File(m_picture.getFilename());
                        if (fdelete.exists()) {
                            if (fdelete.delete()) {
                                System.out.println("file Deleted :" + m_picture.getFilename());
                            } else {
                                System.out.println("file not Deleted :" + m_picture.getFilename());
                            }
                        }
                        Toast.makeText(FullscreenActivity.this, "Picture is deleted",
                                Toast.LENGTH_LONG).show();
                        finish();
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
        });
    }

    /**
     * This method sets up the listeners for the textviews.
     */
    public void setUpTextViews(){
        txtFSTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTitle();
            }
        });
        txtFSDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDescription();
            }
        });
    }

    /**
     * This method makes it possible for the user to edit the description of picture
     * It will open a alertdialog where the user can enter text.
     */
    private void editDescription() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit description");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(m_picture.getDescription());
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                description = input.getText().toString();
                Picture pic = new Picture(m_picture.getId(), m_picture.getFilename(), m_picture.getDate(), m_picture.getTitle(), description);
                bllPicture.update(pic);
                txtFSDescription.setText("" + description);
                Toast.makeText(FullscreenActivity.this, "Description changed",
                        Toast.LENGTH_LONG).show();
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
     * This method makes it possible for the user to edit the title of picture
     * It will open a alertdialog where the user can enter text.
     */
    private void editTitle() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit title");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(m_picture.getTitle());
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                title = input.getText().toString();
                Picture pic = new Picture(m_picture.getId(), m_picture.getFilename(), m_picture.getDate(), title, m_picture.getDescription());
                bllPicture.update(pic);
                txtFSTitle.setText("" + title);
                Toast.makeText(FullscreenActivity.this, "Title changed",
                        Toast.LENGTH_LONG).show();
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
     * This method will take the file and set it to a imageview, while scaling, set rotation and color of picture.
     * @param filename the file
     * @param myImage the imageview
     */
    private void showInFullscreen(String filename, ImageView myImage) {

        File f = new File(filename);
        if (!f.exists()) {
            return;
        }
        myImage.setImageURI(Uri.fromFile(f));
        myImage.setBackgroundColor(Color.WHITE);
        myImage.setRotation(270);
        myImage.setScaleType(ImageView.ScaleType.FIT_XY);
    }
}
