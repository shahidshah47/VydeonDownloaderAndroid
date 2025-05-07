package com.appdev360.jobsitesentry.ui.alarm.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.data.model.AlarmNotification;
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

public class AlarmNotificationAdapter extends RecyclerView.Adapter<AlarmNotificationAdapter.ViewHolder> {

    private static final String TAG = AlarmNotificationAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<AlarmNotification> mDataSet;
    private OnItemClickListener onItemClickListener;

    @Inject
    public AlarmNotificationAdapter(@ApplicationContext Context context) {
        this.context = context;
        mDataSet = new ArrayList<>();

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Todo Butterknife bindings

        @BindView(R.id.notification_name)
        TextView notificationName;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.notification_rule)
        TextView notificationRule;
        @BindView(R.id.notification_region)
        TextView notificationRegion;
        @BindView(R.id.notification_activity)
        TextView notificationActivity;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void bind(final AlarmNotification model,
                         final OnItemClickListener listener) {
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

        View view = inflater.inflate(R.layout.notification_item_adapter, parent, false);
        ButterKnife.bind(this, view);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        AlarmNotification item = mDataSet.get(position);
        holder.notificationName.setText(item.getName());
        holder.date.setText(item.getCreatedAt());
        holder.notificationRule.setText(item.getRule());
        holder.notificationRegion.setText(item.getRegion());
        holder.notificationActivity.setText(item.getActivity());


        //Todo: Setup viewholder for item 
        holder.bind(item, onItemClickListener);
    }

    public void setAlarmItems(ArrayList<AlarmNotification> items) {
        mDataSet = items;
    }

    public void addItems(ArrayList<AlarmNotification> items) {
        mDataSet.addAll(items);

    }

    public void clear() {
        mDataSet.clear();
        notifyDataSetChanged();
    }

    public ArrayList<AlarmNotification> getDataSet() {
        return mDataSet;
    }


    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}