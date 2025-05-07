package com.appdev360.jobsitesentry.ui.timelapse.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.data.model.Camera;
import com.appdev360.jobsitesentry.injection.ApplicationContext;
import com.appdev360.jobsitesentry.ui.timelapse.TimeLapseAdapter;
import com.appdev360.jobsitesentry.widget.TextViewCustom;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimeLapseListAdapter extends
        RecyclerView.Adapter<TimeLapseListAdapter.ViewHolder> {

    private static final String TAG = TimeLapseAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<Camera> mDataSet;
    private OnItemClickListener onItemClickListener;
    private boolean imageFlag = false;

    @Inject
    public TimeLapseListAdapter(@ApplicationContext Context context) {
        this.context = context;
        mDataSet = new ArrayList<>();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.camera_name)
        TextViewCustom camName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void bind(final Camera model, final OnItemClickListener listener) {
            camName.setText(model.getCameraNameFull());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener != null){
                        listener.onItemClick(model);
                    }
                }
            });
        }
    }

    @Override
    public TimeLapseListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        
        View view = inflater.inflate(R.layout.item_card_timelap_layout, parent, false);
        ButterKnife.bind(this, view);
        TimeLapseListAdapter.ViewHolder viewHolder = new TimeLapseListAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(TimeLapseListAdapter.ViewHolder holder, int position) {
        Camera item = mDataSet.get(position);
        holder.bind(item, onItemClickListener);
    }

    public void setSiteItems(ArrayList<Camera> items) {
        mDataSet = items;
    }

    public void addItems(ArrayList<Camera> items) {
        mDataSet.addAll(items);

    }

    public void setFlag(boolean imageFlag){
        this.imageFlag = imageFlag;
    }

    public void clear() {
        mDataSet.clear();
        notifyDataSetChanged();
    }

    public ArrayList<Camera> getDataSet() {
        return mDataSet;
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Camera cam);
    }

    public void setClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}

