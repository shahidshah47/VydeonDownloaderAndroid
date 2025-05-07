package com.appdev360.jobsitesentry.ui.alarm.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.data.model.Alarm;
import com.appdev360.jobsitesentry.injection.ApplicationContext;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Updated By Hussain Saad on 27/01/22
 */

public class AlarmAdapter extends
        RecyclerView.Adapter<AlarmAdapter.ViewHolder> {

    private static final String TAG = AlarmAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<Alarm> mDataSet;
    private OnItemClickListener onItemClickListener;


    @Inject
    public AlarmAdapter(@ApplicationContext Context context) {
        this.context = context;
        mDataSet = new ArrayList<>();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

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

        public void bind(final Alarm model, final OnItemClickListener listener) {

          //  videoName.setText(model.getName()+"  "+model.getTime());



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
        Alarm item = mDataSet.get(position);
        holder.notificationName.setText(item.getName());
        holder.date.setText(item.getTime());
        holder.notificationRule.setText(item.getRule());
        holder.notificationRegion.setText(item.getRegion());
        holder.notificationActivity.setText(item.getActivity());

        //Todo: Setup viewholder for item 
        holder.bind(item, onItemClickListener);
    }

    public void setSiteItems(ArrayList<Alarm> items) {
        mDataSet = items;
    }

    public void addItems(ArrayList<Alarm> items) {
        mDataSet.addAll(items);

    }


    public void clear() {
        mDataSet.clear();
        notifyDataSetChanged();
    }

    public ArrayList<Alarm> getDataSet() {
        return mDataSet;
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Alarm object);
    }

    public void setClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}