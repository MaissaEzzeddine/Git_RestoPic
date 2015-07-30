package tn.codeit.restopic.webservice;

import android.util.Log;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class UserFunctions {

    JSONParser jsonParser = new JSONParser();
    private static String NewPasswordURL = "http://restopic.16mb.com/RestoPic/v1/nouveau";
    private static String codeURL = "http://restopic.16mb.com/RestoPic/v1/code";
    private static String url_create_account = "http://restopic.16mb.com/RestoPic/v1/register";
    private static String url_create_account_facebook = "http://restopic.16mb.com/RestoPic/v1/register_facebook";
    private static String ForgotPasswordURL = "http://restopic.16mb.com/RestoPic/v1/forpass";
    private static String LoginUrl = "http://restopic.16mb.com/RestoPic/v1/login";

    public JSONObject  changePassword ( String email ,String password) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("motdepasse", password));
        JSONObject json = jsonParser.makeHttpRequest(NewPasswordURL, "POST", params);
        Log.e("Entity Response", json.toString());
        return json;
    }

    public  JSONObject forgotPassword (String email , String  code) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("code", code));
        JSONObject json = jsonParser.makeHttpRequest(codeURL, "POST", params);
        Log.e("Entity Response", json.toString());
        return json;
    }

    public JSONObject createAccount(String nom , String prenom , String email , String password ){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("nom", nom));
        params.add(new BasicNameValuePair("prenom", prenom));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("motdepasse", password));
        JSONObject json = jsonParser.makeHttpRequest(url_create_account, "POST", params);
        Log.e("Entity Response", json.toString());
        return json;
    }

    public JSONObject createAccountFacebook(String nom , String prenom , String email){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("nom", nom));
        params.add(new BasicNameValuePair("prenom", prenom));
        params.add(new BasicNameValuePair("email", email));
        JSONObject json = jsonParser.makeHttpRequest(url_create_account_facebook, "POST", params);
        Log.e("Entity Response", json.toString());
        return json;
    }

    public JSONObject resetPassword(String  email){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", email));
        JSONObject json = jsonParser.makeHttpRequest(ForgotPasswordURL, "POST", params);
        Log.e("Entity Response", json.toString());
        return json;
    }

    public JSONObject connexion(String email , String password){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("motdepasse", password));
        JSONObject json = jsonParser.makeHttpRequest(LoginUrl, "POST", params);
        Log.e("Entity Response", json.toString());
        return json;
    }
}
