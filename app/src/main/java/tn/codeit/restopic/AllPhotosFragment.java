package tn.codeit.restopic;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tn.codeit.restopic.webservice.JSONParser;

public class AllPhotosFragment extends Fragment{

    GridView grid ;
    private static String urlAllPictures = "http://restopic.esy.es/RestoPic/pictures/allpictures.php" ;
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_FAIL = "error";
    JSONObject json ;
    private String[] urls ;
    private String[] dates ;
    private static final String TAG_PICTURES = "pictures";
    private static final String TAG_URL = "url";
    private static final String TAG_DATE = "date_de_prise";
    JSONArray pictures = null;
    String  url , date ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.all_photos_layout, container, false) ;
        grid = (GridView) view.findViewById(R.id.grid_all_photos);
        new allPictures().execute() ;
        return view;
    }

    class allPictures extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            json = jsonParser.makeHttpRequest(urlAllPictures, "POST", params);
            Log.e("Entity Response", json.toString());
            return null;
        }

        protected void onPostExecute(String file_url) {
            try {
                Boolean fail = json.getBoolean(TAG_FAIL);
                if (!fail) {
                    pictures = json.getJSONArray(TAG_PICTURES);
                    urls = new String[pictures.length()] ;
                    dates = new String[pictures.length()] ;
                    for (int j = 0; j < pictures.length(); j++) {
                        JSONObject s = null;
                        try {
                            s = pictures.getJSONObject(j);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        url = s.getString(TAG_URL);
                        urls[j] = new String(url) ;

                        date = s.getString(TAG_DATE);
                        dates[j] = new String(date) ;
                    }
                    if(urls!=null) {
                        grid.setAdapter(new AllPhotosAdapter(getActivity(), urls , dates));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
