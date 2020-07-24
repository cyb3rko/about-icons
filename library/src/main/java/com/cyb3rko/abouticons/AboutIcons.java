package com.cyb3rko.abouticons;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class AboutIcons {
    private ArrayList<IconModel> modelList = new ArrayList<>();
    private Class drawableClass;
    private Context appContext;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter mAdapter;
    private View view;

    private boolean allowModificationAnnotation = true;

    @SuppressLint("InflateParams")
    public AboutIcons(Context appContext, Class drawableClass) {
        this.appContext = appContext;
        this.drawableClass = drawableClass;

        Toasty.Config.getInstance().allowQueue(false).apply();
        view = LayoutInflater.from(appContext).inflate(R.layout.activity_icon_view, null);
        recyclerView = view.findViewById(R.id.recycler_view);
    }

    private void setAdapter() {
        mAdapter = new RecyclerViewAdapter(appContext, modelList, drawableClass, allowModificationAnnotation);

        for (int i = 0; i < mAdapter.getIconListSize(); i++) {
            addAttributes(i);
        }

        final GridLayoutManager layoutManager = new GridLayoutManager(appContext, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        onItemClick();
    }

    private void addAttributes(int index) {
        boolean modified = false;
        String iconAuthor = "[Missing]";
        String iconWebsite = "[Missing]";
        String iconLink = "";

        try {
            int arrayId = appContext.getResources().getIdentifier(mAdapter.getDrawableName(index), "array", appContext.getPackageName());
            String[] iconInformation = appContext.getResources().getStringArray(arrayId);
            modified = Boolean.parseBoolean(iconInformation[3]);
            iconAuthor = "by " + iconInformation[0];
            iconWebsite = iconInformation[1];
            iconLink = iconInformation[2];
        } catch (Exception ignored) {
        }

        modelList.add(new IconModel(modified, iconAuthor, iconWebsite, iconLink));
    }

    private void onItemClick() {
        mAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, IconModel model) {
                if (!model.getIconLink().equals("")) {
                    appContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(model.getIconLink())));
                } else {
                    Toasty.warning(appContext, "Link not set!", Toasty.LENGTH_SHORT).show();
                }
            }
        });
    }

    public AboutIcons hideTitle() {
        TextView titleView = view.findViewById(R.id.title_view);
        titleView.setVisibility(View.GONE);
        return this;
    }

    public AboutIcons setTitle(String customTitle) {
        TextView titleView = view.findViewById(R.id.title_view);
        titleView.setText(customTitle);
        return this;
    }

    public AboutIcons hideModificationAnnotation() {
        view.findViewById(R.id.modified_info).setVisibility(View.GONE);
        allowModificationAnnotation = false;
        return this;
    }

    public View get() {
        setAdapter();
        return view;
    }
}
