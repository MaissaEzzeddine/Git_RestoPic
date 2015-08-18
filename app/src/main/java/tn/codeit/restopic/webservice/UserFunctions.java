package tn.codeit.restopic.webservice;

import android.util.Log;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class UserFunctions {

    JSONParser jsonParser = new JSONParser();
    private static String urlNewPassword = "http://restopic.esy.es/RestoPic/v1/nouveau";
    private static String urlCode = "http://restopic.esy.es/RestoPic/v1/code";
    private static String urlCreateAccount = "http://restopic.esy.es/RestoPic/v1/register";
    private static String urlCreateAccountFacebook = "http://restopic.esy.es/RestoPic/v1/register_facebook";
    private static String urlForgotPassword = "http://restopic.esy.es/RestoPic/v1/forpass";
    private static String urlLogin = "http://restopic.esy.es/RestoPic/v1/login";

    public JSONObject  changePassword ( String email ,String password) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("motdepasse", password));
        JSONObject json = jsonParser.makeHttpRequest(urlNewPassword, "POST", params);
        Log.e("New Password Response", json.toString());
        return json;
    }

    public  JSONObject inputCode (String email , String  code) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("code", code));
        JSONObject json = jsonParser.makeHttpRequest(urlCode, "POST", params);
        Log.e("Code Response", json.toString());
        return json;
    }

    public JSONObject createAccount(String nom , String prenom , String email , String password ){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("nom", nom));
        params.add(new BasicNameValuePair("prenom", prenom));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("motdepasse", password));
        JSONObject json = jsonParser.makeHttpRequest(urlCreateAccount, "POST", params);
        Log.e("Create Account Response", json.toString());
        return json;
    }

    public JSONObject createAccountFacebook(String nom , String prenom , String email){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("nom", nom));
        params.add(new BasicNameValuePair("prenom", prenom));
        params.add(new BasicNameValuePair("email", email));
        JSONObject json = jsonParser.makeHttpRequest(urlCreateAccountFacebook, "POST", params);
        Log.e("Create Account Facebook Response", json.toString());
        return json;
    }

    public JSONObject forgotPassword(String  email){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", email));
        JSONObject json = jsonParser.makeHttpRequest(urlForgotPassword, "POST", params);
        Log.e("Forgot Password Response", json.toString());
        return json;
    }

    public JSONObject connexion(String email , String password){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("motdepasse", password));
        JSONObject json = jsonParser.makeHttpRequest(urlLogin, "POST", params);
        Log.e("Connexion Response", json.toString());
        return json;
    }

}
