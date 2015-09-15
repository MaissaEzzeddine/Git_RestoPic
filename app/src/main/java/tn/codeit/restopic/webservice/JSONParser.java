package tn.codeit.restopic.webservice;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class JSONParser {
    HttpEntity httpEntity= null ;
    static JSONObject jsonObject = null;
    static String json = "";
    InputStream inputStream = null;
    Boolean testConnection ;

    public boolean isConnectedToServer(String url, int timeout) {
        try{
            URL myUrl = new URL(url);
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(timeout);
            connection.connect();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public JSONObject makeHttpRequest(String url, String method, List<NameValuePair> params)  {
        testConnection = isConnectedToServer(url, 10);
        if (testConnection == true) {
            try {
                if (method == "POST") {
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(url);
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    httpEntity = httpResponse.getEntity();
                    inputStream = httpEntity.getContent();
                } else if (method == "GET") {
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    String paramString = URLEncodedUtils.format(params, "utf-8");
                    url += "?" + paramString;
                    HttpGet httpGet = new HttpGet(url);
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    httpEntity = httpResponse.getEntity();
                    inputStream = httpEntity.getContent();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                json = stringBuilder.toString();
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            try {
                jsonObject = new JSONObject(json);
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
            return jsonObject;
        }

        else {

            try {
                jsonObject = new JSONObject().put("TEST","serverDown");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;

        }
    }
}
