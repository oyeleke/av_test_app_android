package com.avtestapp.android.androidbase.custom_views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;

import com.avtestapp.android.androidbase.R;
import com.avtestapp.android.androidbase.utils.Listable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;

public class ClickToSelectEditText<T extends Listable> extends AppCompatEditText {

    private List<T> mItems;
    private String[] mListableItems;
    private CharSequence mHint;

    private @Nullable T currentSelection;

    private OnItemSelectedListener<T> onItemSelectedListener;
    private HashSet<OnItemSelectedListener<T>> onItemSelectedListeners = new HashSet<>();

    public ClickToSelectEditText(Context context) {
        super(context);

        mHint = getHint();
    }

    public ClickToSelectEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        mHint = getHint();
    }

    public ClickToSelectEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mHint = getHint();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ClickToSelectEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);

        mHint = getHint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setFocusable(false);
        setClickable(true);
    }

    public void setItems(List<T> items) {
        this.mItems = items;
        this.mListableItems = new String[items.size()];

        int i = 0;

        for (T item : mItems) {
            mListableItems[i++] = item.getLabel();
        }

        configureOnClickListener();
    }

    public @Nullable T getSelection() {
        return currentSelection;
    }

    public void setRawSelection(Object rawItem) {
        setSelection(getCorrespondingItem(rawItem));
    }

    public void setSelection(T item) {
        setText(item.getLabel());
        notifyListeners(item, mItems.indexOf(item));
    }

    T getCorrespondingItem(Object rawItem) {
        for (int i = 0; i < mItems.size(); i++) {
            if (mItems.get(i) == rawItem) {
                return mItems.get(i);
            }
        }

        return (T) new Listable<Object>() {
            @NotNull
            @Override
            public String getLabel() {
                return "###";
            }

            @Override
            public Object getObj() {
                return rawItem;
            }
        };
    }

    public void setSelection(T item, int selectionIndex) {
        setText(item.getLabel());
        notifyListeners(item, selectionIndex);
    }

    public void setSelectionIndex(int index) {
        if (mItems.size() <= index) {
            clearSelection();
            return;
        }
        setText("<font color='#022b61'>"+mListableItems[index]+"</font>");
        //setText(mListableItems[index]);
        notifyListeners(mItems.get(index), index);
    }

    public void clearSelection() {
        setText("");
        notifyListeners(null, -1);
    }

    private void configureOnClickListener() {
        setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle(mHint);
            builder.setItems(mListableItems, (dialogInterface, selectedIndex) -> {
                setSelection(mItems.get(selectedIndex), selectedIndex);
            });
            builder.setPositiveButton(R.string.close, null);
            builder.create().show();
        });
    }

    private void notifyListeners(T selectedItem, int selectedIndex) {

        currentSelection = selectedItem;

        if (onItemSelectedListener != null) {
            onItemSelectedListener.onItemSelectedListener(selectedItem, selectedIndex);
        }

        for (OnItemSelectedListener<T> listener : onItemSelectedListeners) {
            listener.onItemSelectedListener(selectedItem, selectedIndex);
        }
    }

    public void setOnItemSelectedListener(OnItemSelectedListener<T> onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public void addOnItemSelectedListener(OnItemSelectedListener<T> onItemSelectedListener) {
        this.onItemSelectedListeners.add(onItemSelectedListener);
    }

    public void removeOnItemSelectedListener(OnItemSelectedListener<T> onItemSelectedListener) {
        this.onItemSelectedListeners.remove(onItemSelectedListener);
    }

    public void removeAllListeners() {
        this.onItemSelectedListeners.clear();
    }

    public interface OnItemSelectedListener<T> {
        void onItemSelectedListener(T item, int selectedIndex);
    }
}