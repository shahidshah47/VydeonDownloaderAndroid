package com.appdev360.jobsitesentry.ui.dashboard;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.data.DataManager;
import com.appdev360.jobsitesentry.data.model.DashboardMenu;
import com.appdev360.jobsitesentry.injection.ApplicationContext;
import com.appdev360.jobsitesentry.widget.TextViewCustom;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by abubaker on 4/4/18.
 */
public class DashBoardAdapter extends
        RecyclerView.Adapter<DashBoardAdapter.ViewHolder> {

    private static final String TAG = DashBoardAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<DashboardMenu> mDataSet;
    private OnItemClickListener onItemClickListener;
    DataManager dataManager;


    @Inject
    public DashBoardAdapter(@ApplicationContext Context context) {
        this.context = context;
        mDataSet = new ArrayList<>();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // Todo Butterknife bindings

        @BindView(R.id.menu_icon)
        ImageView menuIcon;
        @BindView(R.id.menu_option)
        TextViewCustom menuOption;

        @BindView(R.id.background_layout)
        ConstraintLayout backgroundLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final DashboardMenu model, final OnItemClickListener listener) {

            menuOption.setText(model.getName());
            Picasso.get().load(model.getIconName()).into(menuIcon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v,getLayoutPosition());

                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.dashboard_item_layout, parent, false);
        ButterKnife.bind(this, view);




        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DashboardMenu item = mDataSet.get(position);

        if (position == (mDataSet.size() - 1))
        {
            holder.backgroundLayout.setBackground(ContextCompat.getDrawable(context,R.drawable.sentry_location_background));
        }else
        {
            holder.backgroundLayout.setBackground(ContextCompat.getDrawable(context,R.drawable.background_selector));
        }


        //Todo: Setup viewholder for item 
        holder.bind(item, onItemClickListener);
    }

    public void setSiteItems(ArrayList<DashboardMenu> items) {
        mDataSet = items;
    }

    public void addItems(ArrayList<DashboardMenu> items) {
        mDataSet.addAll(items);

    }

    public void clear() {
        mDataSet.clear();
        notifyDataSetChanged();
    }

    public ArrayList<DashboardMenu> getDataSet() {
        return mDataSet;
    }


    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View v ,int position);
    }

    public void setClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}