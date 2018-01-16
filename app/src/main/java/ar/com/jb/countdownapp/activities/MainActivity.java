package ar.com.jb.countdownapp.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.amitshekhar.DebugDB;

import org.greenrobot.greendao.query.DeleteQuery;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ar.com.jb.countdownapp.R;
import ar.com.jb.countdownapp.adapters.EventListAdapter;
import ar.com.jb.countdownapp.entities.DaoSession;
import ar.com.jb.countdownapp.entities.Event;
import ar.com.jb.countdownapp.entities.EventDao;
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

    private Button openCalendar;
    private EditText dateSelected;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        changeToolbarFont(toolbar, this);

        openCalendar = (Button) findViewById(R.id.btn_date);
        openCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickCalendarButton(view);
            }
        });
        dateSelected = (EditText) findViewById(R.id.txt_date);

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        currentDate = mDay + "/" + (mMonth + 1) + "/" + mYear;

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
        mAdapter = new EventListAdapter(this, eventList, currentDate);

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

        //load data from database
        loadData();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRecord(view);
            }
        });
    }


    public void onClickCalendarButton(View v) {
        if (v == openCalendar) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            dateSelected.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
//        if (v == btnTimePicker) {
//
//            // Get Current Time
//            final Calendar c = Calendar.getInstance();
//            mHour = c.get(Calendar.HOUR_OF_DAY);
//            mMinute = c.get(Calendar.MINUTE);
//
//            // Launch Time Picker Dialog
//            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
//                    new TimePickerDialog.OnTimeSetListener() {
//
//                        @Override
//                        public void onTimeSet(TimePicker view, int hourOfDay,
//                                              int minute) {
//
//                            txtTime.setText(hourOfDay + ":" + minute);
//                        }
//                    }, mHour, mMinute, false);
//            timePickerDialog.show();
//        }
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
        Event event = new Event(null, "Movie", currentDate.toString(), "Esta es la descripción", "#3fb0ac", "ic_airplane", true);
        long eventId = daoSession.getEventDao().insert(event);
        loadData();
    }

    public void deleteRecord(Long id) {
        final DeleteQuery<Event> tableDeleteQuery = daoSession.queryBuilder(Event.class)
                .where(EventDao.Properties.Id.eq(id))
                .buildDelete();
        tableDeleteQuery.executeDeleteWithoutDetachingEntities();
        daoSession.clear();
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
            Long id = eventList.get(viewHolder.getAdapterPosition()).getId();

            // backup of removed item for undo purpose
            final Event deletedItem = eventList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            //delete from database
            deleteRecord(id);

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Eliminado!", Snackbar.LENGTH_LONG);
//            snackbar.setAction("UNDO", new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    // undo is selected, restore the deleted item
//                    mAdapter.restoreItem(deletedItem, deletedIndex);
//                }
//            });
//            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

}
