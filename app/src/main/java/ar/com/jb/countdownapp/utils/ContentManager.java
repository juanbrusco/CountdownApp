package ar.com.jb.countdownapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import ar.com.jb.countdownapp.entities.Event;

/**
 * Created by juan.brusco on 10-Jan-18.
 */

public class ContentManager {
    private static ContentManager _instance;

    private Context context;
    public ArrayList<Event> eventsArray;

    public Context getContext() {
        return context;
    }


    public ArrayList<Event> geteventsArray() {
        return eventsArray;
    }

    public void seteventsArray(ArrayList<Event> eventsArray) {
        this.eventsArray = eventsArray;
    }

    public void showDialogError(String text, Activity activity) {

    }

    public void showDialogOk(String text, Activity activity) {

    }

    public static SharedPreferences getPrefs(Context ctx){
        return ctx.getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);
    }

    public static void set_instance(ContentManager _instance) {
        ContentManager._instance = _instance;
    }

    public synchronized static ContentManager getInstance() {
        if (_instance == null) {
            _instance = new ContentManager();
        }
        return _instance;
    }
}
