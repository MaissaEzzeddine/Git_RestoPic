package tn.codeit.restopic;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.facebook.FacebookSdk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tn.codeit.restopic.webservice.JSONParser;
import tn.codeit.restopic.webservice.UserFunctions;


@SuppressWarnings("ALL")
public class MesPhotosFragment extends Fragment {

    SessionManager session;
    int code = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String TAG_PICTURES = "pictures";
    private static final String TAG_URL = "url";
    private static final String TAG_DATE = "date_de_prise";
    JSONArray pictures = null;
    Uri fileUri ;
    int session_id ;
    String timeStampName , timeStamp , CurrentPhotoPath, url , date , session_name;
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_FAIL = "error";
    JSONObject json ;
    private GridView grid;
    private String[] urls ;
    private String[] dates ;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        session = new SessionManager(getActivity().getApplicationContext());
        session_name = session.getName() ;
        if (session_name != "empty") {
            session_id = Integer.parseInt(session_name);
        }
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.mes_photos_layout, container, false) ;
        grid = ( GridView) view.findViewById(R.id.grid_mes_photos);
        new getPicture().execute() ;
        return view;
    }

    class getPicture extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
            UserFunctions userFunctions=new UserFunctions();
            json = userFunctions.getMesPhotos(session_name);
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
                        grid.setAdapter(new MesPhotosAdapter(getActivity(), urls , dates));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ActionBar actionBar=((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.show();
    }

}