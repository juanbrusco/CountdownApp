package ar.com.jb.countdownapp.activities;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.amitshekhar.DebugDB;

import java.util.ArrayList;
import java.util.List;

import ar.com.jb.countdownapp.R;
import ar.com.jb.countdownapp.adapters.EventListAdapter;
import ar.com.jb.countdownapp.entities.DaoSession;
import ar.com.jb.countdownapp.entities.Event;
import ar.com.jb.countdownapp.helpers.RecyclerItemTouchHelper;
import ar.com.jb.countdownapp.utils.ContentManager;
import ar.com.jb.countdownapp.utils.MyAppClass;

public class MainActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    DaoSession daoSession;
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<Event> eventList;
    private EventListAdapter mAdapter;
    private CoordinatorLayout coordinatorLayout;
    private Typeface custom_font_light;
    private Typeface custom_font_regular;
    private Typeface custom_font_thin;
    private Typeface custom_font_bold;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_launcher_background));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        changeToolbarFont(toolbar, this);

        //Fonts
        custom_font_light = ContentManager.getInstance().getFontLight(getApplicationContext());
        custom_font_regular = ContentManager.getInstance().getFontRegular(getApplicationContext());
        custom_font_thin = ContentManager.getInstance().getFontThin(getApplicationContext());
        custom_font_bold = ContentManager.getInstance().getFontBold(getApplicationContext());

        //BD session
        daoSession = ((MyAppClass) getApplication()).getDaoSession();
        //BD url log
        DebugDB.getAddressLog();

        //Config list
        recyclerView = findViewById(R.id.recycler_view_events);
        coordinatorLayout = findViewById(R.id.coordinator_main);
        eventList = new ArrayList<>();
        mAdapter = new EventListAdapter(this, eventList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        //Load data
        loadData();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRecord(view);
            }
        });
    }

    private void loadData() {
        List<Event> events = daoSession.getEventDao().loadAll();
        eventList.clear();
        eventList.addAll(events);
        mAdapter.notifyDataSetChanged();
    }

    public static void changeToolbarFont(Toolbar toolbar, Activity context) {
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                if (tv.getText().equals(toolbar.getTitle())) {
                    applyFont(tv, context);
                    break;
                }
            }
        }
    }

    public static void applyFont(TextView tv, Activity context) {
        tv.setTypeface(ContentManager.getInstance().getFontLight(context));
    }

    public void addRecord(View v) {
        Event event = new Event(null, "Movie", "17/03/2018", "Esta es la descripción", "#3fb0ac", "ic_airplane", true);
        long eventId = daoSession.getEventDao().insert(event);
        loadData();
    }

    public void deleteRecords(View v) {
        daoSession.getEventDao().deleteAll();
    }

    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     * undo option will be provided in snackbar to restore the item
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof EventListAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = eventList.get(viewHolder.getAdapterPosition()).getTitle();

            // backup of removed item for undo purpose
            final Event deletedItem = eventList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
