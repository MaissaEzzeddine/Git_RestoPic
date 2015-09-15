package tn.codeit.restopic.webservice;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UserFunctions {

    JSONParser jsonParser = new JSONParser();
    private static String urlNewPassword = "http://SERVERADRESS/RestoPic/v1/newpassword";
    private static String urlCode = "http://SERVERADRESS/RestoPic/v1/code";
    private static String urlCreateAccount = "http://SERVERADRESS/RestoPic/v1/register";
    private static String urlCreateAccountFacebook = "http://SERVERADRESS/RestoPic/v1/register_facebook";
    private static String urlForgotPassword = "http://SERVERADRESS/RestoPic/v1/forgotpassword";
    private static String urlLogin = "http://SERVERADRESS/RestoPic/v1/login";
    private static String urlAllPictures = "http://SERVERADRESS/RestoPic/pictures/allpictures.php" ;
    private static String urlGetPicture = "http://SERVERADRESS/RestoPic/pictures/getpictures.php" ;
    private static String urlGetCoupon = "http://SERVERADRESS/RestoPic/pictures/getcoupon.php" ;
    private static String urlUpload = "http://SERVERADRESS/RestoPic/pictures/upload.php";


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

    public JSONObject getAllPhotos(){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        JSONObject json = jsonParser.makeHttpRequest(urlAllPictures, "POST", params);
        Log.e("All Pictures Response", json.toString());
        return json;
    }

    public HttpURLConnection PartagerPhoto(String fileName , String boundary) {
        HttpURLConnection conn = null;
        URL url = null;
        try {
            url = new URL(urlUpload);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        try {
            conn.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
        conn.setRequestProperty("uploaded_file", fileName);
        return  conn ;
    }


    public JSONObject getMesPhotos(String session_name) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("user_id", session_name));
        JSONObject json = jsonParser.makeHttpRequest(urlGetPicture, "POST", params);
        Log.e("Mes Photos Response", json.toString());
        return json;
    }
    public JSONObject getCoupon(String id_url) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("url", id_url));
        JSONObject json = jsonParser.makeHttpRequest(urlGetCoupon, "POST", params);
        Log.e("Coupon Response", json.toString());
        return json;
    }
}
