package com.omrobbie.worldweather;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;

import java.util.ArrayList;
import java.util.HashMap;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private ArrayList<HashMap<String, String>> data = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;
    private RequestBuilder<PictureDrawable> requestBuilder;

    /* setup constructor untuk class WeatherAdapter */
    public WeatherAdapter(Context context, ArrayList<HashMap<String, String>> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;

        /* setup Glide agar bisa membaca format SVG */
        requestBuilder = GlideApp.with(context)
                .as(PictureDrawable.class)
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_error)
                .transition(withCrossFade())
                .listener(new SvgSoftwareLayerSetter());
    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(ViewHolder, int)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.weather_listitems, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method
     * should update the contents of the {@link ViewHolder#itemView} to reflect the item at
     * the given position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Partial bind vs full bind:
     * <p>
     * The payloads parameter is a merge list from {@link #notifyItemChanged(int, Object)} or
     * {@link #notifyItemRangeChanged(int, int, Object)}.  If the payloads list is not empty,
     * the ViewHolder is currently bound to old data and Adapter may run an efficient partial
     * update using the payload info.  If the payload is empty,  Adapter must run a full bind.
     * Adapter should not assume that the payload passed in notify methods will be received by
     * onBindViewHolder().  For example when the view is not attached to the screen, the
     * payload in notifyItemChange() will be simply dropped.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HashMap<String, String> item = data.get(position);

        /* masukkan data ke semua komponen */
        requestBuilder.load(Uri.parse(item.get("flag"))).into(holder.imgFlag);
        holder.txtCountry.setText(item.get("name") + " (" + item.get("capital") + ")");
        Glide.with(context).load(item.get("icon")).into(holder.imgWeather);
        holder.txtWeather.setText(item.get("description") + ", " + item.get("temp"));
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return data.size();
    }

    /* setup inner class view untuk inflater listitem layout */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgFlag;
        public TextView txtCountry;
        public ImageView imgWeather;
        public TextView txtWeather;

        /* setup constructor untuk inner class ViewHolder */
        public ViewHolder(View itemView) {
            super(itemView);

            /* deklarasikan semua komponen yang dipakai di dalam layout */
            imgFlag = (ImageView) itemView.findViewById(R.id.imgFlag);
            txtCountry = (TextView) itemView.findViewById(R.id.txtCountry);
            imgWeather = (ImageView) itemView.findViewById(R.id.imgWeather);
            txtWeather = (TextView) itemView.findViewById(R.id.txtWeather);
        }
    }
}