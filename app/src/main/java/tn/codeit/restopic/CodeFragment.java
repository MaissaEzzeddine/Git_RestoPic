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
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater Inflater = LayoutInflater.from(getActivity());
        View CustomView = Inflater.inflate(R.layout.custom_actionbar, null);
        TextView TitleTextView = (TextView) CustomView.findViewById(R.id.title_text);
        TitleTextView.setText("reinitialiser le mot de passe");
        ImageButton imageButton = (ImageButton) CustomView
                .findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.container, new LogInFragment()).addToBackStack(null).commit();
            }
        });
        actionBar.setCustomView(CustomView);
        actionBar.setDisplayShowCustomEnabled(true);
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
}
