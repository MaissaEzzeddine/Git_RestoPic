package tn.codeit.restopic;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tn.codeit.restopic.webservice.UserFunctions;

public class AllPhotosFragment extends Fragment{

    GridView grid ;
    private static final String TAG_FAIL = "error";
    private static final String TAG_TESTCONNECTION = "TEST" ;

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
            UserFunctions userFunctions=new UserFunctions();
            json = userFunctions.getAllPhotos();
            return null;
        }

        protected void onPostExecute(String file_url) {
            try {

                String testConnection = json.getString(TAG_TESTCONNECTION);
                if (testConnection == "serverDown")
                {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(), "serveur non disponible maintenant", Toast.LENGTH_LONG).show();
                        }
                    });
                }
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
