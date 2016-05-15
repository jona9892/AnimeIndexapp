package animeindex.DAL.ServiceGateway.Implementation;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import animeindex.DAL.ServiceGateway.Abstraction.IGateway;
import animeindex.BE.Anime;
import animeindex.BE.Genre;

/**
 * Created by jona9892 on 09-04-2016.
 */
public class AnimeGateway implements IGateway<Anime> {

    private final String TAG = "AnimeIndex-animes";

    ArrayList<Anime> animeArray;
    private int m_limit = 10;
    public AnimeGateway(){
        animeArray = new ArrayList<Anime>();
    }

    /**
     * This method returns an arraylist of anime objects.
     * It gets the url by string and concerts the string to a JSONobject. And then get the array of this JSONObject that is wraped in "docs"
     * Then for every JSONobject in this array it will get this JSONobejct and get the value for each properties and set it the a new Anime object.
     * @param idx
     * @return
     */
    public ArrayList<Anime> getPage(int idx) {
        try {
            String URL = "http://192.168.0.19:9000/api/animes";

            String url = URL + "?" + "limit=" + m_limit + "&page=" + idx;
            String result = getContent(url);

            if (result == null)
            {
                Log.d(TAG, "Nothing returned...");
                return null;
            }

            JSONObject object = new JSONObject(result);

            JSONArray array = object.getJSONArray("docs");
            ArrayList<Anime> res = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);

                String title = o.getString("title");
                String type = o.getString("type");
                String status = o.getString("status");
                String description = o.getString("description");

                ArrayList<Genre> m_genres = new ArrayList<Genre>();

                JSONArray genreArray = o.getJSONArray("genres");

                for (int j = 0; j < genreArray.length(); j++){

                    JSONObject go = genreArray.getJSONObject(j);
                    Genre genre = new Genre();
                    genre.setName(go.getString("name"));
                    m_genres.add(genre);
                }

                String image = o.getString("image");
                Log.d("tag:", image);

                int episodeCount = Integer.parseInt(o.getString("episodeCount"));
                String airdate = o.getString("airdate");
                String enddate = o.getString("enddate");

                Anime a = new Anime(title, type, status, description, m_genres ,image, episodeCount, airdate, enddate);
               res.add(a);
            }
            //I do this because everytime this method gets called, it removes the objects, so it doesn't get the right position
            animeArray.addAll(res);
            Log.d("Repository:", "amount in array" + res.size());
            Log.d("Repository:", "amount in array" + animeArray.size());
            return res;


        } catch (JSONException e) {
            Log.e(TAG,
                    "There was an error parsing the JSON", e);
        } catch (Exception e)
        {  Log.d(TAG, "General exception in loadAll " + e.getMessage());
        }
        return null;
    }

    /**
     * This method return an arraylist containing all anime objects.
     * @return
     */
    public ArrayList<Anime> getAll(){
        return animeArray;
    }

    /**
     * Get the content of the url as a string. Based on using a scanner.
     * @param urlString - the url must return data typical in either json, xml, csv etc..
     * @return the content as a string. Null is something goes wrong.
     */
    private String getContent(String urlString)
    {
        StringBuilder sb = new StringBuilder();
        try {

            java.net.URL url = new URL(urlString);
            Scanner s = new Scanner(url.openStream());

            while (s.hasNextLine()) {
                String line = s.nextLine();
                sb.append(line);
            }
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return sb.toString();
    }
}
