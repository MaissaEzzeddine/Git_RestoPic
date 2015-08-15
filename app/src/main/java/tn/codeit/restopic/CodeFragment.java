package tn.codeit.restopic;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import tn.codeit.restopic.webservice.UserFunctions;

public class CodeFragment extends Fragment {

    Button continuer;
    EditText inputCode ;
    private static final String TAG_FAIL = "error";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.code_layout, container, false);
        inputCode = (EditText) view.findViewById(R.id.code);
        continuer = (Button) view.findViewById(R.id.valider);
        continuer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new forgotPassword().execute();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ActionBar actionBar=((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Reinitiliser le mot de passe");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.show();
    }

    class forgotPassword extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
            String  email = getArguments().getString("EmailPassage");
            String code = inputCode.getText().toString();
            UserFunctions uf=new UserFunctions();
            JSONObject json = uf.forgotPassword(email, code);
            try {
                Boolean fail = json.getBoolean(TAG_FAIL);
                if (!fail) {
                    Bundle bundle = new Bundle();
                    bundle.putString( "EmailPassage" , email);
                    ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
                    changePasswordFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.container, changePasswordFragment ).addToBackStack(null).commit();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String file_url) {
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        MenuItem item1 = menu.findItem(R.id.capture);
        MenuItem item2 = menu.findItem(R.id.deconnexion);
        MenuItem item3 = menu.findItem(R.id.aide);
        item1.setVisible(false);
        item2.setVisible(false);
        item3.setVisible(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {


            case android.R.id.home:
                getFragmentManager().beginTransaction().replace(R.id.container, new LogInFragment()).addToBackStack(null).commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
