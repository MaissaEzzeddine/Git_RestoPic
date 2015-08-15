package tn.codeit.restopic;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {

    SharedPreferences preferences;
    Editor editor;
    Context context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "RestoPicPreference";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_NAME = "nom";
    public static final String KEY_EMAIL = "email";

    public SessionManager(Context context){
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = preferences.edit();

    }
    public void createLoginSession(String nom){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NAME, nom);
        editor.commit();
    }

    public boolean checkLogin(){
        return ( this.isLoggedIn() ) ;
    }
    public void logoutUser(){
        editor.clear();
        editor.commit();
    }

    public boolean isLoggedIn(){
        return preferences.getBoolean(IS_LOGIN, false);
    }
    public String getName () { return  preferences.getString(KEY_NAME , "empty") ; }

}
