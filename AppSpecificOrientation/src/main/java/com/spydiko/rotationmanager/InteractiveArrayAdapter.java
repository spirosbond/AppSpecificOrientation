package com.spydiko.rotationmanager;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by PuR3v1L on 7/8/2013.
 */
public class InteractiveArrayAdapter extends ArrayAdapter<Model> {

    private final ArrayList<Model> list;
    private final Activity context;
    AppSpecificOrientation myapp;

    public InteractiveArrayAdapter(Activity context, ArrayList<Model> list, AppSpecificOrientation myapp) {
        super(context, R.layout.app_row, list);
        this.context = context;
        this.list = list;
        this.myapp = myapp;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.app_row, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) view.findViewById(R.id.list_item_view);
            viewHolder.checkbox = (ImageView) view.findViewById(R.id.list_item_check);
            viewHolder.landscape = (ImageView) view.findViewById(R.id.list_item_landscape);
            viewHolder.label = (ImageView) view.findViewById(R.id.imageView);
            viewHolder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageView tmp = (ImageView) view;
                    Log.d("adapter", "mpika checkbox");
                    Model element = (Model) viewHolder.checkbox.getTag();
                    Log.d("adapter",""+myapp.loadPreferences(element.getPackageName(), element.isSelectedPortrait()));
                    if (myapp.loadPreferences(element.getPackageName(), true)) {
                        myapp.savePreferences(element.getPackageName(), false, true);
                        element.setSelectedPortrait(false);
                        tmp.setImageDrawable(myapp.getResources().getDrawable(R.drawable.port_off));
                    } else {
                        myapp.savePreferences(element.getPackageName(), true, true);
                        element.setSelectedPortrait(true);
                        tmp.setImageDrawable(myapp.getResources().getDrawable(R.drawable.port_on));
                    }
                    Log.d("adapter", element.getPackageName()+" "+element.isSelectedPortrait());
                }
            });
            viewHolder.landscape.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageView tmp = (ImageView) view;
                    Log.d("adapter", "mpika landscape");
                    Model element = (Model) viewHolder.landscape.getTag();
                    if (myapp.loadPreferences(element.getPackageName(), false)) {
                        myapp.savePreferences(element.getPackageName(), false, false);
                        element.setSelectedLandscape(false);
                        tmp.setImageDrawable(myapp.getResources().getDrawable(R.drawable.land_off));
                    } else {
                        myapp.savePreferences(element.getPackageName(), true, false);
                        element.setSelectedLandscape(true);
                        tmp.setImageDrawable(myapp.getResources().getDrawable(R.drawable.land_on));
                    }
                    Log.d("adapter", element.getPackageName()+" "+element.isSelectedLandscape());
                }
            });
            view.setTag(viewHolder);
            viewHolder.checkbox.setTag(list.get(position));
            viewHolder.landscape.setTag(list.get(position));
        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
            ((ViewHolder) view.getTag()).landscape.setTag(list.get(position));
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.text.setText(list.get(position).getName());
        if (list.get(position).isSelectedPortrait()) holder.checkbox.setImageDrawable(myapp.getResources().getDrawable(R.drawable.port_on));
        else holder.checkbox.setImageDrawable(myapp.getResources().getDrawable(R.drawable.port_off));

        if (list.get(position).isSelectedLandscape())
            holder.landscape.setImageDrawable(myapp.getResources().getDrawable(R.drawable.land_on));
        else holder.landscape.setImageDrawable(myapp.getResources().getDrawable(R.drawable.land_off));
        holder.label.setImageDrawable(list.get(position).getLabel());
        return view;
    }

    static class ViewHolder {
        protected TextView text;
        protected ImageView checkbox;
        protected ImageView landscape;
        protected ImageView label;
    }
}



