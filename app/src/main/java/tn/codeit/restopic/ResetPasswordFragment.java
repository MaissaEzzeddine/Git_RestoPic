package tn.codeit.restopic;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import tn.codeit.restopic.webservice.UserFunctions;


public class ResetPasswordFragment extends Fragment {

    private static final String TAG_FAIL = "error";
    EditText inputEmail;
    Button resetPassword , cancel ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

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
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ActionBar actionBar=((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(getActivity());

        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText("reinitialiser le mot de passe");

        ImageButton imageButton = (ImageButton) mCustomView
                .findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.container, new LogInFragment()).addToBackStack(null).commit();
            }
        });

        actionBar.setCustomView(mCustomView);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.show();
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