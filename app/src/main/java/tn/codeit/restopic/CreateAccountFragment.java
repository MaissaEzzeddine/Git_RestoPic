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
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import tn.codeit.restopic.webservice.UserFunctions;

public class CreateAccountFragment extends Fragment {

    EditText inputNom , inputPrenom , inputEmail , inputPassword ;
    private static final String TAG_FAIL = "error";
    ActionBar actionBar;
    private static final String TAG_TESTCONNECTION = "TEST" ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.create_account_layout, container, false) ;
        inputNom = (EditText) view.findViewById(R.id.nom);
        inputPrenom = (EditText) view.findViewById(R.id.prenom);
        inputEmail = (EditText) view.findViewById(R.id.email);
        inputPassword = (EditText) view.findViewById(R.id.create_password);
        Button buttonCreateAccount = (Button) view.findViewById(R.id.create_button);
        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CreateNewUser().execute();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        actionBar=((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Creation d'un compte");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.show();
    }

    class CreateNewUser extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
            String nom = inputNom.getText().toString();
            String prenom = inputPrenom.getText().toString();
            String email = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();
            UserFunctions userFunctions=new UserFunctions();
            JSONObject json = userFunctions.createAccount(nom,prenom,email,password);
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
                    getFragmentManager().beginTransaction().replace(R.id.container, new LogInFragment()).addToBackStack(null).commit();
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(), "Votre compte a ete cee avec succes", Toast.LENGTH_LONG).show();
                        }
                    });
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

        MenuItem captureItem = menu.findItem(R.id.capture);
        MenuItem deconnexionItem = menu.findItem(R.id.deconnexion);
        MenuItem aideItem = menu.findItem(R.id.aide);
        captureItem.setVisible(false);
        deconnexionItem.setVisible(false);
        aideItem.setVisible(false);
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