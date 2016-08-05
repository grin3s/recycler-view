package ru.yandex.yamblz.ui.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ru.yandex.yamblz.R;

class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentHolder> implements ContentItemTouchHelperCallback.ItemTouchHelperAdapter {

    private final Random rnd = new Random();
    private final List<Integer> colors = new ArrayList<>();

    private int longClicked1 = -1, longClicked2 = -1;
    private int lastSwapped1 = -1, lastSwapped2 = -1;

    private void clearLongClicked() {
        longClicked1 = -1;
        longClicked2 = -1;
    }

    @Override
    public ContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item, parent, false);
        ContentHolder holder = new ContentHolder(view);
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (longClicked1 == -1) {
                    longClicked1 = holder.getAdapterPosition();
                }
                else {
                    longClicked2 = holder.getAdapterPosition();
                    onSwap(longClicked1, longClicked2);
                }
                return false;
            }
        });
        return holder;
    }

    private void onSwap(int pos1, int pos2) {
        Collections.swap(colors, pos1, pos2);
        lastSwapped1 = pos1;
        lastSwapped2 = pos2;
        clearLongClicked();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ContentHolder holder, int position) {
        boolean swapped = false;
        if ((position == lastSwapped1) || (position == lastSwapped2)) {
            swapped = true;
        }
        holder.bind(createColorForPosition(position), swapped);
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    private Integer createColorForPosition(int position) {
        int prevSize = colors.size();
        if (position >= prevSize) {
            //when we rotate the screen the state is restored and we may need to recreate more than one element
            for (int i = prevSize; i <= position; i++) {
                colors.add(Color.rgb(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255)));
            }
        }
        return colors.get(position);
    }

    @Override
    public void onItemDismiss(int position) {
        colors.remove(position);
        clearLongClicked();
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(colors, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(colors, i, i - 1);
            }
        }
        clearLongClicked();

        notifyItemMoved(fromPosition, toPosition);
    }

    static class ContentHolder extends RecyclerView.ViewHolder {
        ContentHolder(View itemView) {
            super(itemView);
        }

        void bind(Integer color, boolean swapped) {
            itemView.setBackgroundColor(color);
            if (swapped) {
                ((TextView) itemView).setText("WAS SWAPPED");
            }
            else {
                ((TextView) itemView).setText("#".concat(Integer.toHexString(color).substring(2)));
            }
        }
    }
}
