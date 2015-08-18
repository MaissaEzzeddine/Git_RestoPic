package tn.codeit.restopic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import tn.codeit.restopic.webservice.JSONParser;
import tn.codeit.restopic.webservice.UserFunctions;

@SuppressWarnings("ALL")
public class CouponFragment extends Fragment {

    int code = 1;
    SessionManager session ;
    private static final String TAG_ID_PHOTO = "id_photo";
    private static final String TAG_ID_USER = "id_user";
    private static final String TAG_ID_COUPON = "id_coupon";
    private static final String TAG_DATE_ACTIVATION = "date_activation";
    private static final String TAG_DATE_EXPIRATION = "date_expiration";
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_FAIL = "error";
    JSONObject json ;
    String id_url , id_photo , id_user , id_coupon , date_activation , date_expiration ;
    TextView textCode , textDateActivation , textDateExpiration ;
    ImageView status ;
    ViewPager Tab;
    ActionBar actionBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(getActivity().getApplicationContext());
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.coupon_layout, container, false);
        id_url = getArguments().getString("url");
        textCode = (TextView) view.findViewById(R.id.codebarre);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "font/code.ttf");
        textCode.setTypeface(font);
        textDateActivation = (TextView) view.findViewById(R.id.date);
        textDateExpiration = (TextView) view.findViewById(R.id.expiration);
        status = (ImageView) view.findViewById(R.id.status);
        new getCoupon().execute() ;
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        actionBar=((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle("Details de la photo");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.show();
    }

    class getCoupon extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
            UserFunctions userFunctions=new UserFunctions();
            json = userFunctions.getCoupon(id_url);
            return null;
        }

        protected void onPostExecute(String file_url) {
            try {
                Boolean fail = json.getBoolean(TAG_FAIL);
                if (!fail) {

                    id_photo = json.getString(TAG_ID_PHOTO);
                    id_user = json.getString(TAG_ID_USER);
                    id_coupon = json.getString(TAG_ID_COUPON);
                    date_activation = json.getString(TAG_DATE_ACTIVATION);
                    date_expiration = json.getString(TAG_DATE_EXPIRATION);

                    String pattern = "dd-MM-yyyy HH:mm:ss";
                    SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                    Date dateExpiration = dateFormat.parse(date_expiration);
                    Date dateActivation = dateFormat.parse(date_activation);

                    if (dateExpiration.compareTo(dateActivation) > 0 )
                    {   status.setImageResource(R.drawable.circle_green);
                        Log.e("status" , "actif encore") ; }
                    else{
                        status.setImageResource(R.drawable.circle_red);
                        Log.e("status" , "desactive") ;
                    }
                    String ids = id_user+id_photo+id_coupon ;
                    String codeInput = "1234" + ids + "9876" ;

                    EAN13CodeBuilder code = new EAN13CodeBuilder(codeInput);
                    textCode.setText(code.getCode());
                    textDateActivation.setText(date_activation);
                    textDateExpiration.setText(date_expiration);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

        public  void  onActivityResult( int requestCode, int resultCode , Intent data ) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==code && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            Bundle bundle = new Bundle();
            bundle.putParcelable("picture", bitmap);
            PartagePhotoFragment partagePhotoFragment = new PartagePhotoFragment();
            partagePhotoFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.container, partagePhotoFragment).addToBackStack(null).commit();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}