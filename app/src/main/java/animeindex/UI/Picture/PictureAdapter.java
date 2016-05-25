package animeindex.UI.Picture;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import animeindex.BE.Picture;
import animeindex.R;

/**
 * Created by JonathansPC on 15-05-2016.
 */
    /**
     * The arrayadapter for animelist
     */
    public class PictureAdapter extends ArrayAdapter<Picture>
    {
        /**
         * This is used to get all the widgets the arrayadapter and listview needs
         * So that they only get called one time
         */
        public static class ViewHolder {
            TextView txtPictureTitle;
            ImageView imgPicture;
        }


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
