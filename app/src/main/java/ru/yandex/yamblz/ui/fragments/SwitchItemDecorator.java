package ru.yandex.yamblz.ui.fragments;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by grin3s on 04.08.16.
 */

public class SwitchItemDecorator extends RecyclerView.ItemDecoration {
    private Paint mPaint = new Paint();

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(20);

        for (int i = 0; i < parent.getChildCount(); i++) {
            final View child = parent.getChildAt(i);
            if (parent.getChildAdapterPosition(child) % 2 != 0) {
                c.drawRect(child.getLeft(), child.getTop(), child.getRight(), child.getBottom(), mPaint);
            }
        }

    }
}
