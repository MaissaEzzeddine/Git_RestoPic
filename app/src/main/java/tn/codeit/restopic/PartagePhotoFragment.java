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
import android.widget.ImageView;


public class PartagePhotoFragment extends Fragment {
    int code = 1;
    ImageView picture ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.partage_photo_layout, container, false);
        Intent intent = getActivity().getIntent();
        Bitmap bitmap = intent.getParcelableExtra("BitmapImage");
        picture = (ImageView) view.findViewById(R.id.picture);
        picture.setImageBitmap(bitmap);
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_partage, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.liste_coupons:
                getFragmentManager().beginTransaction().replace(R.id.partage_container, new ListeCouponsFragment()).addToBackStack(null).commit();
                return true;
            case R.id.deconnexion:
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.accueil:
                getFragmentManager().beginTransaction().replace(R.id.partage_container, new ClientFragment()).addToBackStack(null).commit();
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