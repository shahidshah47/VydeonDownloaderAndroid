package com.appdev360.jobsitesentry.ui.sentryLocation;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.data.model.Site;
import com.appdev360.jobsitesentry.injection.ApplicationContext;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Abubaker on 07,May,2018
 * <p>
 * Updated By Hussain Saad on 27/01/22
 */

public class SentryLocationAdapter extends RecyclerView.Adapter<SentryLocationAdapter.ViewHolder> {

    private static final String TAG = SentryLocationAdapter.class.getSimpleName();
    private ArrayList<Site> mDataSet;
    private OnItemClickListener onClickListener;
    private Context mContext;

    @Inject
    public SentryLocationAdapter(@ApplicationContext Context context) {
        this.mContext = context;
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

        public void bind(final Site model, final OnItemClickListener listener) {
            siteName.setText(model.getSiteName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(getLayoutPosition());

                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.sentry_location_item_layout, parent, false);
        ButterKnife.bind(this, view);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Site item = mDataSet.get(position);

        //Todo: Setup viewholder for item 
        holder.bind(item, onClickListener);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void setSiteItems(ArrayList<Site> feeds) {
        mDataSet = feeds;
    }

    public void addItems(ArrayList<Site> items) {
        mDataSet.addAll(items);
        //notifyItemInserted(0);
    }

    public void clear() {
        mDataSet.clear();
        notifyDataSetChanged();
    }

    public ArrayList<Site> getDataSet() {
        return mDataSet;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setClickListener(OnItemClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}