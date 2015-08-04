package tn.codeit.restopic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.Profile;

public class ListeCouponsFragment extends Fragment {
    int code = 1;
    SessionManager session ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(getActivity().getApplicationContext());
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.liste_coupons_layout,  container, false);
        TextView textView = (TextView) view.findViewById(R.id.codebarre);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "font/code.ttf");
        textView.setTypeface(font);

        // generate barcode string
        EAN13CodeBuilder bb = new EAN13CodeBuilder("124958761310");
        textView.setText(bb.getCode());
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ActionBar actionBar=((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Liste des coupons");
        actionBar.show();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_coupons, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Accueil:
                getFragmentManager().beginTransaction().replace(R.id.container, new ClientFragment()).addToBackStack(null).commit();
                return true;
            case R.id.deconnexion:
                logOut();
                session.logoutUser();
                getFragmentManager().beginTransaction().replace(R.id.container, new LogInFragment()).addToBackStack(null).commit();
                return true;
            case R.id.capture:
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(i, code);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
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

    public void logOut() {
        AccessToken.setCurrentAccessToken(null);
        Profile.setCurrentProfile(null);
    }
}
