package animeindex.DAL.Repositories;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import animeindex.Model.Anime;
import animeindex.Model.Genre;

/**
 * Created by jona9892 on 09-04-2016.
 */
public class AnimeRepository {
    private final String URL = "http://192.168.0.19:9000/api/animes";

    private final String TAG = "AnimeIndex-animes";

    ArrayList<Anime> m_animes;

    public AnimeRepository(){
        m_animes = new ArrayList<Anime>();
    }

    public void loadAllAnimes()
    {
        try {
            String result = getContent(URL);

            if (result == null) return;


            JSONArray array = new JSONArray(result);

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

                Anime a = new Anime(title, type, status, description, m_genres, image, episodeCount, airdate, enddate);
                m_animes.add(a);
            }

        } catch (JSONException e) {
            Log.e(TAG,
                    "There was an error parsing the JSON", e);
        } catch (Exception e)
        {  Log.d(TAG, "General exception in loadAll " + e.getMessage());
        }
    }

    public ArrayList<Anime> getAll()
    { return m_animes; }


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
