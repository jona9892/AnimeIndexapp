package animeindex.UI.Animelist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import animeindex.BE.Animelist;
import animeindex.R;

/**
 * Created by JonathansPC on 28-04-2016.
 */
public class AnimelistAdapter extends ArrayAdapter<Animelist> implements Filterable
{
    private ArrayList<Animelist> animelistArray;
    private ArrayList<Animelist> animelistFiltered;
    private AnimeFilter animeFilter;
    private Context context;

    public AnimelistAdapter(Context context, int resource, ArrayList<Animelist> animelistsArray) {
        super(context, resource, animelistsArray);
        this.animelistFiltered = animelistsArray;
        this.animelistArray = animelistsArray;
        this.context = context;

        animeFilter = new AnimeFilter();
    }

    /**
     * Gets the number of object in the filtered arraylist
     */
    @Override
    public int getCount() {
        return animelistFiltered.size();
    }

    /**
     * Used to get the object at a specific postion
     *
     * @param position int
     */
    @Override
    public Animelist getItem(int position) {
        return super.getItem(position);
    }

    /**
     * used to get the right filter
     */
    @Override
    public Filter getFilter() {
        if(animeFilter == null){
            animeFilter = new AnimeFilter();
        }
        return animeFilter;
    }

    /**
     *
     * @param position the position of the object in the list
     * @param v the view
     * @param parent the parent
     * @return the view
     */
    @Override
    public View getView(int position, View v, ViewGroup parent) {
        ViewHolderAnimelist holder;
        if (v == null) {
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.animelist_cell, null);
            Log.d("LIST", "Position: " + position + " View created");

            holder = new ViewHolderAnimelist();
            holder.txtAnimelistTitle = (TextView) v.findViewById(R.id.txtAnimelistTitle);
            holder.txtAnimelistType = (TextView)v.findViewById(R.id.txtAnimelistType);
            holder.txtAnimelistStatus = (TextView)v.findViewById(R.id.txtAnimelistStatus);
            holder.txtAnimelistEpisodesSeen = (TextView) v.findViewById(R.id.txtAnimelistEpisodesSeen);
            holder.txtAnimelistEpisodeCount = (TextView)v.findViewById(R.id.txtAnimelistEpisodeCount);
            holder.txtAnimelistRating = (TextView)v.findViewById(R.id.txtAnimelistRating);
            holder.txtNumber = (TextView)v.findViewById(R.id.txtNumber);
            holder.imgAnimelistImage = (ImageView)v.findViewById(R.id.imgAnimelistImage);

            v.setTag(holder);
        }
        else
            Log.d("LIST", "Position: " + position + " View Reused");
        holder = (ViewHolderAnimelist) v.getTag();


        Animelist animelist = animelistFiltered.get(position);

        holder.txtAnimelistTitle.setText(" " + animelist.getTitle());
        holder.txtAnimelistType.setText("" +animelist.getType());
        holder.txtAnimelistStatus.setText(""+animelist.getStatus());

        holder.txtAnimelistEpisodesSeen.setText("" + " " + animelist.getEpisodesSeen());
        holder.txtAnimelistEpisodeCount.setText(""+ " "+animelist.getEpisodeCount());
        holder.txtAnimelistRating.setText(""+ " "+animelist.getRating());
        int pos = position+1;
        holder.txtNumber.setText(""+ " "+pos+" ");
        Picasso.with(context).load(animelist.getImage()).into(holder.imgAnimelistImage);


        return v;
    }


    /**
     * Used to filter the incoming string form searchview, and will add new object to arraylist
     */
    private class AnimeFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();

            if (constraint!=null && constraint.length()>0) {
                ArrayList<Animelist> tempList = new ArrayList<Animelist>();

                // search content in anime list
                for (Animelist al : animelistArray) {
                    if (al.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(al);
                        Log.d("TAGg", "addd: " + al.getTitle());
                    }
                }
                //Set the values to filter result
                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = animelistArray.size();
                filterResults.values = animelistArray;
            }

            return filterResults;
        }

        /**
         * Notify about filtered list to ui
         * This is called when filter is working, and invoked in the ui thread
         * @param constraint text
         * @param results filtered result
         */
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            animelistFiltered = (ArrayList<Animelist>) results.values;
            notifyDataSetChanged();
        }
    }


}
