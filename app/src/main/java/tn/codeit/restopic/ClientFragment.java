package tn.codeit.restopic;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.Profile;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tn.codeit.restopic.webservice.JSONParser;

public class ClientFragment extends Fragment {

    SessionManager session;
    int code = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String TAG_PICTURES = "pictures";
    private static final String TAG_URL = "url";
    private static final String TAG_DATE = "date_de_prise";

    JSONArray pictures = null;
    Uri fileUri ;
    int id ;
    String timeStampName , timeStamp , CurrentPhotoPath, url , date , name;
    private static String url_getpicture = "http://restopic.esy.es/RestoPic/pictures/getpictures.php" ;
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
        name = session.getName() ;
        id = Integer.parseInt(name);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.client_layout, container, false) ;
        grid = ( GridView) view.findViewById(R.id.grid);
        new GetPicture().execute() ;
        return view;
    }
    class GetPicture extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_id", name));
            json = jsonParser.makeHttpRequest(url_getpicture, "POST", params);
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
                        grid.setAdapter(new ImageListAdapter(getActivity(), urls , dates));
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
        actionBar.setTitle("Accueil");
        actionBar.show();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {

            case R.id.deconnexion:
                logOut();
                session.logoutUser();
                getFragmentManager().beginTransaction().replace(R.id.container, new LogInFragment()).addToBackStack(null).commit();
                return true;
            case R.id.capture:
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(getActivity().getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createPicture();
                    } catch (IOException ex) {}
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        fileUri = Uri.fromFile(photoFile) ;
                        i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                        startActivityForResult(i, code);
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private File createPicture() throws IOException
    {
        timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        timeStampName = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String user_id = "" + id +"_" ;
        File storageDir = Environment.getExternalStorageDirectory()  ;
        File image = new File(storageDir + "/" + user_id + timeStampName + ".jpg");
        // Save a file: path for use with ACTION_VIEW intents
        CurrentPhotoPath = image.getAbsolutePath();
        Log.e(TAG, "photo path = " + CurrentPhotoPath);
        return image;
    }

    private void launchPartagerPhoto(boolean isImage ,  String path)
    {
        Bundle bundle = new Bundle();
        bundle.putString("filePath" , path);
        bundle.putString("datePrise", timeStamp);
        bundle.putBoolean("isImage", isImage);
        bundle.putInt("id", id);
        PartagePhotoFragment partagePhotoFragment = new PartagePhotoFragment();
        partagePhotoFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.container, partagePhotoFragment).addToBackStack(null).commit();
    }

    public  void  onActivityResult( int requestCode, int resultCode , Intent data )
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==code && resultCode == Activity.RESULT_OK) {
            launchPartagerPhoto(true , CurrentPhotoPath);
        }
    }

    public void logOut()
    {
        AccessToken.setCurrentAccessToken(null);
        Profile.setCurrentProfile(null);
    }
}