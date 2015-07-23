package tn.codeit.restopic;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import tn.codeit.restopic.webservice.UserFunctions;

public class LogInFragment extends Fragment  {

    EditText inputEmail  , inputPassword;
    private static final String TAG_FAIL = "error";
    private AccessTokenTracker accessTokenTracker;
    private AccessToken accessToken ;
    private  ProfileTracker profileTracker;
    CallbackManager callbackManager;
    SessionManager session;
    TextView tv1;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity) this.getActivity();
        ActionBar aBar = activity.getSupportActionBar();
        aBar.hide();
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        session = new SessionManager(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
            }
        };
        accessToken = AccessToken.getCurrentAccessToken();

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {

            }
        };
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.login_layout, container, false);
        TextView forgotPassword = (TextView) view.findViewById(R.id.forgot_password);
        forgotPassword.setPaintFlags(forgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        inputEmail = (EditText) view.findViewById(R.id.email);
        inputPassword = (EditText) view.findViewById(R.id.login_password);
        tv1=(TextView)view.findViewById(R.id.restopic);

        Typeface face= Typeface.createFromAsset(getActivity().getAssets(), "font/font.ttf");
        tv1.setTypeface(face);

        Button ButtonLogin = (Button) view.findViewById(R.id.connect);
        ButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View view) {
                new Connect().execute();
            }
        });

        LoginButton ButtonFacebook = (LoginButton) view.findViewById(R.id.facebook);
        ButtonFacebook.setReadPermissions("user_friends", "email", "user_birthday");
        ButtonFacebook.setFragment(this);
        ButtonFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                session.createLoginSession("CompteFacebook");
                getFragmentManager().beginTransaction().replace(R.id.container, new ClientFragment()).addToBackStack(null).commit();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity(), "Login canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getActivity(), "Login error", Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), exception.getMessage().toString(), Toast.LENGTH_SHORT).show();
                Log.d("connectedStateLabel", exception.getMessage().toString());
            }

        });
        return view;
    }


    class Connect extends AsyncTask<String, String, String> {
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                        }
                        protected String doInBackground(String... args) {
                            String email = inputEmail.getText().toString();
                            String password = inputPassword.getText().toString();
                            UserFunctions uf=new UserFunctions();
                            JSONObject json = uf.connexion(email,password);
                            try {
                                Boolean fail = json.getBoolean(TAG_FAIL);
                                if (!fail) {
                                    session.createLoginSession("CompteApplicatipn");
                                    getFragmentManager().beginTransaction().replace(R.id.container, new ClientFragment()).addToBackStack(null).commit();
                } else {
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String file_url) {
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}