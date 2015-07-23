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


public class ResetPasswordFragment extends Fragment {

    private static final String TAG_FAIL = "error";
    EditText inputEmail;
    Button resetPassword , cancel ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.reset_password_layout, container, false);
        inputEmail = (EditText) view.findViewById(R.id.email_reset);
        resetPassword = (Button) view.findViewById(R.id.reset);
        resetPassword.setOnClickListener(new View.OnClickListener() {
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
            String email = inputEmail.getText().toString();
            UserFunctions uf=new UserFunctions();
            JSONObject json = uf.resetPassword(email);
            try {
                Boolean fail = json.getBoolean(TAG_FAIL);
                if (!fail) {
                    Bundle bundle = new Bundle();
                    bundle.putString( "EmailPassage" , email);
                    CodeFragment codeFragment = new CodeFragment();
                    codeFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.container, codeFragment).addToBackStack(null).commit();
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