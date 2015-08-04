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
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
    Uri fileUri ;
    int id;
    String timeStamp ,  CurrentPhotoPath ;
    private static String url_getpicture = "http://restopic.16mb.com/RestoPic/v1/getpicture" ;
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_FAIL = "error";
    private static final String TAG_URL = "url";
    ImageView picture ;
    String url_picture;
    //GridLayout gridLayout ;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        session = new SessionManager(getActivity().getApplicationContext());
        String name = session.getName() ;
        id = Integer.parseInt(name);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.client_layout, container, false) ;
        picture = (ImageView) view.findViewById(R.id.picture);
        new GetPicture().execute() ;

        /*gridLayout = (GridLayout) view.findViewById(R.id.grid);

        gridLayout.removeAllViews();

        int total = 4;
        int column = 2;
        int row = total / column;
        gridLayout.setColumnCount(column);
        gridLayout.setRowCount(row + 1);
        for (int i = 0, c = 1, r = 1; i < total; i++, c++) {
            if (c == column) {
                c = 0;
                r++;
            }
            ImageView oImageView = new ImageView(getActivity());
            oImageView.setImageResource(R.drawable.ic_launcher);


            GridLayout.Spec rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 1);
            GridLayout.Spec colspan = GridLayout.spec(GridLayout.UNDEFINED, 1);

            oImageView.setLayoutParams(new GridLayout.LayoutParams(rowSpan, colspan));

            if (r == 0 && c == 0) {
                Log.e("", "spec");
                colspan = GridLayout.spec(GridLayout.UNDEFINED, 2);
                rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 2);
            }
            GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(rowSpan, colspan);
            gridLayout.addView(oImageView, gridParam);

        }*/

        return view;
    }

    class GetPicture extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
            String id = "70" ;
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("picture_id", id));
            JSONObject json = jsonParser.makeHttpRequest(url_getpicture, "POST", params);
            Log.e("Entity Response", json.toString());
            try {
                Boolean fail = json.getBoolean(TAG_FAIL);
                if (!fail) {
                     url_picture = json.getString(TAG_URL);
                } else {
                }
            }
                catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
            }
        protected void onPostExecute(String file_url) {
            Log.e("url",url_picture);
            Picasso.with(getActivity()).load(url_picture).fit().into(picture, new Callback() {
                @Override
                public void onSuccess() {
                    // do something
                }

                @Override
                public void onError() {
                }
            });

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
            case R.id.liste_coupons:
                getFragmentManager().beginTransaction().replace(R.id.container, new ListeCouponsFragment()).addToBackStack(null).commit();
                return true;
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
        // Create an image file name
        timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String user_id = "" + id +"_" ;
        File storageDir = Environment.getExternalStorageDirectory()  ;
        File image = new File(storageDir + "/" + user_id + timeStamp + ".jpg");
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