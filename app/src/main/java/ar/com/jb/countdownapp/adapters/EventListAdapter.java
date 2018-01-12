package ar.com.jb.countdownapp.adapters;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ar.com.jb.countdownapp.R;
import ar.com.jb.countdownapp.entities.Event;
import ar.com.jb.countdownapp.utils.ContentManager;
import ar.com.jb.countdownapp.utils.MyAppClass;

/**
 * Created by juan.brusco on 10-Jan-18.
 */

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.MyViewHolder> {
    private Context context;
    private List<Event> eventList;
    private Typeface custom_font_light;
    private Typeface custom_font_regular;
    private Typeface custom_font_thin;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, limit_date, countdown;
        public ImageView thumbnail;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            custom_font_light = ContentManager.getInstance().getFontLight(context);
            custom_font_regular = ContentManager.getInstance().getFontRegular(context);
            custom_font_thin = ContentManager.getInstance().getFontThin(context);

            name = view.findViewById(R.id.name);
            limit_date = view.findViewById(R.id.limitDate);
            countdown = view.findViewById(R.id.countdown);

            name.setTypeface(custom_font_regular);
            limit_date.setTypeface(custom_font_thin);
            countdown.setTypeface(custom_font_thin);

            thumbnail = view.findViewById(R.id.thumbnail);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }


    public EventListAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Event event = eventList.get(position);
        final String color = event.getColor();

        holder.name.setText(event.getTitle());
        holder.name.setTextColor(Color.parseColor(color.toString()));

        holder.limit_date.setText(event.getLimit_date());

        holder.countdown.setText(getCountDown(event.getLimit_date()));
        holder.countdown.setTextColor(Color.parseColor(color.toString()));

//        holder.thumbnail.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_airplane));
        holder.thumbnail.setImageResource(getImage(event.getImage().toString()));
        holder.thumbnail.setColorFilter(Color.parseColor(color.toString()));
//        Glide.with(context)
//                .load(getImage(event.getImage()))
//                .into(holder.thumbnail);
    }

    private String getCountDown(String limit_date) {
        return "200";
    }

    public int getImage(String imageName) {
        int drawableResourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        return drawableResourceId;
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void removeItem(int position) {
        eventList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Event event, int position) {
        eventList.add(position, event);
        // notify item added by position
        notifyItemInserted(position);
    }
}