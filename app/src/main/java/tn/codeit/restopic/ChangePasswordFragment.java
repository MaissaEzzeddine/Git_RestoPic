package tn.codeit.restopic;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import tn.codeit.restopic.webservice.UserFunctions;

public class ChangePasswordFragment extends Fragment {

    Button continuer,cancel ;
    EditText inputPassword ;
    private static final String TAG_Fail = "error";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.change_password_layout, container, false);
        inputPassword = (EditText) view.findViewById(R.id.reset_password);
        continuer = (Button) view.findViewById(R.id.continuer);
        continuer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new forgotPassword().execute();
            }
        });
        cancel = (Button) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.container, new LogInFragment()).addToBackStack(null).commit();
            }
        });
        return view;
    }

    class forgotPassword extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
            String email = getArguments().getString("EmailPassage");
            String password = inputPassword.getText().toString();
            UserFunctions uf=new UserFunctions();
            JSONObject json = uf.changePassword(email, password);
            try {
                Boolean fail = json.getBoolean(TAG_Fail);
                if (!fail) {
                    getFragmentManager().beginTransaction().replace(R.id.container, new LogInFragment()).addToBackStack(null).commit();
                } else {
                    //
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String file_url) {
        }
    }



}
