package tn.codeit.restopic;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class ListeCouponsFragment extends Fragment {
    int code = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.liste_coupons_layout,  container, false);
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
            Intent intent = new Intent(getActivity(), PartagePhotoActivity.class);
            intent.putExtra("BitmapImage", bitmap);
            startActivity(intent);
        }
    }
}
