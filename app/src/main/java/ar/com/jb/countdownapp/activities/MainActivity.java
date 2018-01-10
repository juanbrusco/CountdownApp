package ar.com.jb.countdownapp.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.amitshekhar.DebugDB;

import org.greenrobot.greendao.async.AsyncOperation;
import org.greenrobot.greendao.async.AsyncOperationListener;
import org.greenrobot.greendao.async.AsyncSession;

import java.util.ArrayList;
import java.util.List;

import ar.com.jb.countdownapp.R;
import ar.com.jb.countdownapp.entities.DaoMaster;
import ar.com.jb.countdownapp.entities.DaoSession;
import ar.com.jb.countdownapp.entities.Event;
import ar.com.jb.countdownapp.utils.MyAppClass;

public class MainActivity extends AppCompatActivity {

    DaoSession daoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        daoSession = ((MyAppClass)getApplication()).getDaoSession();

        //LOG de la URL para debug de la BD
        DebugDB.getAddressLog();

        TextView textView = (TextView) findViewById(R.id.textView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRecord(view);
            }
        });
    }

    public void addRecord(View v) {
        Event event = new Event(null, "Movie" );
        long eventId = daoSession.getEventDao().insert(event);
    }

    public void listRecords(View v) {
        List<Event> events = daoSession.getEventDao().loadAll();
        List<String> eventsList = new ArrayList<>();
        for (Event e : events) {
            eventsList.add(e.getTitle());
            Log.d("TAG", String.format("%s (%s)", e.getTitle()));
        }
    }

    public void deleteRecords(View v) {
        daoSession.getEventDao().deleteAll();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
