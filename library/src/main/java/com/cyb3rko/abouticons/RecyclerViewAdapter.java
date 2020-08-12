package com.cyb3rko.abouticons;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;
import java.util.ArrayList;

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<IconModel> modelList;
    private ArrayList<String> drawableNames = new ArrayList<>();
    private ArrayList<Drawable> usedDrawables;
    private boolean allowModificationAnnotation;
    private Context appContext;
    private OnItemClickListener onItemClickListener;

    public RecyclerViewAdapter(Context appContext, ArrayList<IconModel> modelList, Class<?> drawableClass, boolean allowModificationAnnotation) {
        this.appContext = appContext;
        this.modelList = modelList;
        this.allowModificationAnnotation = allowModificationAnnotation;

        getUsedDrawables(appContext, drawableClass);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_list, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final IconModel model = getItem(position);
        ViewHolder genericViewHolder = (ViewHolder) holder;

        if (allowModificationAnnotation && model.getModified()) {
            genericViewHolder.itemRelLayout.setBackgroundColor(ContextCompat.getColor(appContext, R.color.colorModified));
        }
        genericViewHolder.imgUser.setImageDrawable(usedDrawables.get(position));
        if (!model.getIconLicense().equals("")) {
            genericViewHolder.itemLicense.setText(model.getIconLicense());
        } else {
            genericViewHolder.itemLicense.setVisibility(View.GONE);
            ViewGroup.LayoutParams layoutParams = genericViewHolder.itemRelLayout.getLayoutParams();
            layoutParams.height = layoutParams.height - 75;
            genericViewHolder.itemRelLayout.setLayoutParams(layoutParams);
        }
        genericViewHolder.itemTxtTitle.setText(model.getTitle());
        genericViewHolder.itemTxtMessage.setText(model.getMessage());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    private void getUsedDrawables(Context appContext, Class<?> drawableClass) {
        Resources resources = appContext.getResources();
        Field[] allDrawables = drawableClass.getFields();
        usedDrawables = new ArrayList<>();

        for (Field field : allDrawables) {
            try {
                if (resources.getResourceEntryName(field.getInt(null)).startsWith("_")) {
                    usedDrawables.add(resources.getDrawable(field.getInt(null), appContext.getTheme()));
                    drawableNames.add(resources.getResourceEntryName(field.getInt(null)));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private IconModel getItem(int position) {
        return modelList.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, IconModel model);
    }

    public int getIconListSize() {
        return usedDrawables.size();
    }

    public String getDrawableName(int index) {
        return drawableNames.get(index).substring(1);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout itemRelLayout;
        private ImageView imgUser;
        private TextView itemLicense;
        private TextView itemTxtTitle;
        private TextView itemTxtMessage;

        public ViewHolder(final View itemView) {
            super(itemView);

            itemRelLayout = itemView.findViewById(R.id.relativeLayout);
            imgUser = itemView.findViewById(R.id.img_user);
            itemLicense = itemView.findViewById(R.id.item_license);
            itemTxtTitle = itemView.findViewById(R.id.item_txt_title);
            itemTxtMessage = itemView.findViewById(R.id.item_txt_message);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));
                }
            });
        }
    }
}
