package tn.codeit.restopic;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
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
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
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
    TextView title;
    String nom,prenom,email;
    private static final String NAME = "name";
    private static final String GENDER = "gender";
    private static final String EMAIL = "email";
    private static final String BIRTHDAY = "birthday";
    private static final String FIELDS = "fields";
    private static final String REQUEST_FIELDS = TextUtils.join(",", new String[]{NAME, GENDER, EMAIL, BIRTHDAY});
    private JSONObject user;
    ActionBar actionBar ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        actionBar=((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.hide();
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
        title=(TextView)view.findViewById(R.id.app_name);
        Typeface face= Typeface.createFromAsset(getActivity().getAssets(), "font/font.ttf");
        title.setTypeface(face);

        Button ButtonLogin = (Button) view.findViewById(R.id.button_connect);
        ButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Connect().execute();
            }
        });
        LoginButton ButtonFacebook = (LoginButton) view.findViewById(R.id.button_facebook);
        ButtonFacebook.setReadPermissions("user_friends", "email", "user_birthday");
        ButtonFacebook.setFragment(this);

        ButtonFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                fetchUserInfo();
                updateUI();
                new CreateNewUserFb().execute();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity(), "Login canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getActivity(), "Login error", Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), exception.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
    class CreateNewUserFb extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
            if (AccessToken.getCurrentAccessToken() != null) {
                if (user != null) {
                    nom = user.optString("name");
                    prenom = user.optString("name");
                    email = user.optString("email");
                }
            }
            UserFunctions uf=new UserFunctions();
            JSONObject json = uf.createAccountFacebook(nom, prenom, email);
            try {
                Boolean fail = json.getBoolean(TAG_FAIL);
                int id = json.getInt("id");
                if (!fail) {
                    session.createLoginSession(""+id);
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String file_url) {
        }
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
                int id = json.getInt("id");
                if (!fail) {
                    session.createLoginSession(""+id);
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
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

    @Override
    public void onResume() {
        super.onResume();
        fetchUserInfo();
        updateUI();
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchUserInfo();
        updateUI();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    private void fetchUserInfo() {
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken, new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject me, GraphResponse response) {
                            user = me;
                            updateUI();
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString(FIELDS, REQUEST_FIELDS);
            request.setParameters(parameters);
            GraphRequest.executeBatchAsync(request);
        } else {
            user = null;
        }
    }

    private void updateUI() {
        if (!isAdded()) {
            return;
        }
        if (AccessToken.getCurrentAccessToken() != null) {
            if (user != null) {
            }
        }
    }

}