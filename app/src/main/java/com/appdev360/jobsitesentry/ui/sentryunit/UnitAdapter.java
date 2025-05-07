package com.appdev360.jobsitesentry.ui.sentryunit;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.data.model.unit.UnitData;
import com.appdev360.jobsitesentry.injection.ApplicationContext;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by abubaker on 3/14/18.
 */
public class UnitAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = UnitAdapter.class.getSimpleName();

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;


    private Context context;

    public void setmDataSet(ArrayList<UnitData> mDataSet) {
        this.mDataSet = mDataSet;
    }

    private ArrayList<UnitData> mDataSet;
    private OnItemClickListener onItemClickListener;

    @Inject
    public UnitAdapter(@ApplicationContext Context context) {
        this.context = context;
        mDataSet = new ArrayList<>();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Todo Butterknife bindings

        @BindView(R.id.site_name)
        TextView siteName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void bind(final UnitData model, final OnItemClickListener listener) {
            siteName.setText(model.getDeviceName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(getLayoutPosition());

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
    public int getItemViewType(int position) {
        return mDataSet.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.unit_item_layout, parent, false);
            ButterKnife.bind(this, view);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder) {
            populateRow((ViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }
    }

    public void populateRow(ViewHolder holder, int position) {
        UnitData item = mDataSet.get(position);
        //Todo: Setup viewholder for item 
        holder.bind(item, onItemClickListener);
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        viewHolder.progressBar.setVisibility(View.VISIBLE);
    }


    public void setItems(ArrayList<UnitData> items) {
        mDataSet = items;
    }

    public void addItems(ArrayList<UnitData> items) {
        mDataSet.addAll(items);
        notifyDataSetChanged();

    }

    public void clear() {
        mDataSet.clear();
        notifyDataSetChanged();
    }

    public ArrayList<UnitData> getDataSet() {
        return mDataSet;
    }


    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void setClickListener(OnItemClickListener onClickListener) {
        this.onItemClickListener = onClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}