package com.avtestapp.android.androidbase.av_test;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.avtestapp.android.androidbase.R;

import java.util.List;
import java.util.zip.Inflater;

public class CustomSpinnerAdapter<T> extends ArrayAdapter<T> {

    private List<T> data;
    private LayoutInflater inflater;

    public CustomSpinnerAdapter(@NonNull Context context, @LayoutRes int resource,
                                @NonNull List<T> objects) {
        super(context, resource, objects);
        this.data = objects;
        this.inflater = LayoutInflater.from(context.getApplicationContext());
    }

    @Override
    public boolean isEnabled(int position) {
        return position != 0 && super.isEnabled(position);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        TextView tv = (TextView) view;
        if (position == 0) {
            tv.setTextColor(Color.GRAY);
        } else {
            tv.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.colorPrimary));
        }
        return view;
    }

    @Nullable
    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView tv = (TextView) view;
        if (position == 0) {
            tv.setTextColor(Color.GRAY);
        } else {
            tv.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.colorPrimary));
        }
        return view;
    }

    public List<T> getData() {
        return data;
    }

    public void replaceData(List<T> data, T hint) {
        this.data.clear();
        this.data.add(hint);
        this.data.addAll(data);
        notifyDataSetChanged();
    }

}
