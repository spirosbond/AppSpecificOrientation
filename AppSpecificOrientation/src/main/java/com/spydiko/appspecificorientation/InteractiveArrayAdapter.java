package com.spydiko.appspecificorientation;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by PuR3v1L on 7/8/2013.
 */
public class InteractiveArrayAdapter extends ArrayAdapter<Model> {

    private final List<Model> list;
    private final Activity context;
    AppSpecificOrientation myapp ;

    public InteractiveArrayAdapter(Activity context, List<Model> list) {
        super(context, R.layout.app_row, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.app_row, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) view.findViewById(R.id.list_item_view);
            viewHolder.checkbox = (CheckBox) view.findViewById(R.id.list_item_check);
            viewHolder.label = (ImageView) view.findViewById(R.id.imageView);
            viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Model element = (Model) viewHolder.checkbox.getTag();
                    Log.d("Adapter", "enter");
                    Log.d("Adapter", element.getName()+ " " + element.isSelected());
                    element.setSelected(buttonView.isChecked());

                }
            });
            view.setTag(viewHolder);
            viewHolder.checkbox.setTag(list.get(position));
        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.text.setText(list.get(position).getName());
        holder.checkbox.setChecked(list.get(position).isSelected());
        holder.label.setImageDrawable(list.get(position).getLabel());
        return view;
    }

    static class ViewHolder {
        protected TextView text;
        protected CheckBox checkbox;
        protected ImageView label;
    }
}



