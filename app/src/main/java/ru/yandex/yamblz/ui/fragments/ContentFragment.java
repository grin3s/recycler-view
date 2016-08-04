package ru.yandex.yamblz.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindString;
import butterknife.BindView;
import ru.yandex.yamblz.R;
import ru.yandex.yamblz.ui.adapters.ContentItemTouchHelperCallback;

public class ContentFragment extends BaseFragment {

    private static final String DEBUG_TAG = "ContentFragment";
    @BindView(R.id.rv)
    RecyclerView rv;

    @BindString(R.string.columns_count_pref)
    String str;

    GridLayoutManager mLayoutManager;
    ContentAdapter mAdapter;

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

        ItemTouchHelper.Callback callback =
                new ContentItemTouchHelperCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rv);
    }
}
