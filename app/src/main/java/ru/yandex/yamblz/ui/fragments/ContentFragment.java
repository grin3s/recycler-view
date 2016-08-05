package ru.yandex.yamblz.ui.fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import butterknife.BindString;
import butterknife.BindView;
import ru.yandex.yamblz.R;

public class ContentFragment extends BaseFragment {

    private static final String DEBUG_TAG = "ContentFragment";
    @BindView(R.id.rv)
    RecyclerView rv;

    @BindString(R.string.columns_count_pref)
    String columnsCountPrefStr;

    @BindString(R.string.decorate_pref)
    String decoratePrefStr;

    @BindView(R.id.button_columns_plus)
    Button buttonColumnsPlus;

    @BindView(R.id.button_columns_minus)
    Button buttonColumnsMinus;

    @BindView(R.id.checkBox_decorator)
    CheckBox checkBoxDecorator;

    GridLayoutManager mLayoutManager;
    ContentAdapter mAdapter;
    SwitchItemDecorator mDecorator = new SwitchItemDecorator();

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        int nCols = Integer.parseInt(prefs.getString(getResources().getString(R.string.columns_count_pref), "1"));
        Log.d(DEBUG_TAG, Integer.toString(nCols));
        mLayoutManager = new GridLayoutManager(getContext(), nCols);
        rv.setLayoutManager(mLayoutManager);
        mAdapter = new ContentAdapter();
        rv.setAdapter(mAdapter);

        if (prefs.getBoolean(decoratePrefStr, false)) {
            rv.addItemDecoration(new SwitchItemDecorator());
        }

        rv.addOnScrollListener(new ColorChangeScrollListener(view));

        checkBoxDecorator.setOnClickListener(v -> {
            if (((CheckBox) v).isChecked()) {
                rv.addItemDecoration(mDecorator);
            }
            else {
                rv.removeItemDecoration(mDecorator);
            }
        });

        buttonColumnsPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayoutManager.setSpanCount(mLayoutManager.getSpanCount() + 1);
                mAdapter.notifyItemChanged(0);
            }
        });

        buttonColumnsMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayoutManager.setSpanCount(mLayoutManager.getSpanCount() - 1);
                mAdapter.notifyItemChanged(0);
            }
        });


        ItemTouchHelper.Callback callback =
                new ContentItemTouchHelperCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rv);
    }


    //didn't understand the task correctly at first, but whatever...
    private class ColorChangeScrollListener extends RecyclerView.OnScrollListener {
        private ColorDrawable mDrawable = new ColorDrawable(Color.RED);
        private View mView;

        ColorChangeScrollListener(View view) {
            mView = view;
            mView.setBackground(mDrawable);
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int curPosition = rv.getChildAdapterPosition(rv.getChildAt(0));
            int curAlpha = curPosition;
            mDrawable.setAlpha(Math.min(curPosition, 255));
        }

    }

}
