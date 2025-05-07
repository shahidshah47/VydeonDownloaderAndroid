package com.appdev360.jobsitesentry.ui.timelapse;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.data.model.timelapse.TimeLapse;
import com.appdev360.jobsitesentry.injection.ApplicationContext;
import com.appdev360.jobsitesentry.widget.TextViewCustom;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimeLapseAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = TimeLapseAdapter.class.getSimpleName();


    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private Context context;


    private ArrayList<TimeLapse> mDataSet;
    private OnItemClickListener onItemClickListener;
    //  private boolean imageFlag = false;

    @Inject
    public TimeLapseAdapter(@ApplicationContext Context context) {
        this.context = context;
        mDataSet = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        return mDataSet.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // Todo Butterknife bindings
        @BindView(R.id.thumbnail)
        ImageView thumbnail;
        @BindView(R.id.video_name)
        TextViewCustom videoName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final TimeLapse model, final OnItemClickListener listener) {
            videoName.setText(model.getName());
            if (model.getThumb() != null && model.getThumb().length() > 0)
                Picasso.get().load(model.getThumb()).placeholder(ContextCompat.getDrawable(context, R.drawable.app_logo)).into(thumbnail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener != null) {
                        listener.onItemClick(model);
                    }
                }
            });
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            Context context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.time_lapse_item_layout, parent, false);
            ButterKnife.bind(this, view);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder) {
            populateRow((ViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        viewHolder.progressBar.setVisibility(View.VISIBLE);
    }

    public void populateRow(ViewHolder holder, int position) {
        TimeLapse item = mDataSet.get(position);
        holder.bind(item, onItemClickListener);
    }

    public void setSiteItems(ArrayList<TimeLapse> items) {
        mDataSet = items;
    }

    public void addItems(ArrayList<TimeLapse> items) {
        mDataSet.addAll(items);
    }

//    public void setFlag(boolean imageFlag){
//        this.imageFlag = imageFlag;
//    }

    public void clear() {
        mDataSet.clear();
        notifyDataSetChanged();
    }

    public ArrayList<TimeLapse> getDataSet() {
        return mDataSet;
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public interface OnItemClickListener {
        void onItemClick(TimeLapse object);
    }

    public void setClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setmDataSet(ArrayList<TimeLapse> mDataSet) {
        this.mDataSet = mDataSet;
    }

}