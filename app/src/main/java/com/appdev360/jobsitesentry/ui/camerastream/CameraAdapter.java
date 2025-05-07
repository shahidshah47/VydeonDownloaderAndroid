package com.appdev360.jobsitesentry.ui.camerastream;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.data.DataManager;
import com.appdev360.jobsitesentry.constant.GeneralConstants;
import com.appdev360.jobsitesentry.data.model.Camera;
import com.appdev360.jobsitesentry.injection.ApplicationContext;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by abubaker on 3/15/18.
 */
public class CameraAdapter extends RecyclerView.Adapter<CameraAdapter.ViewHolder> {

    private static final String TAG = CameraAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<Camera> mDataSet;
    private OnItemClickListener onItemClickListener;
    private SparseBooleanArray selectedItems;
    int previous = 0;
    private DataManager dataManager;

    @Inject
    public CameraAdapter(@ApplicationContext Context context ,DataManager dataManager) {
        this.context = context;
        this.dataManager = dataManager;
        mDataSet = new ArrayList<>();
        selectedItems = new SparseBooleanArray();

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // Todo Butterknife bindings

        @BindView(R.id.site_name)
        TextView siteName;
        @BindView(R.id.background_layout)
        ConstraintLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void bind(final Camera model,int position, final OnItemClickListener listener) {
            siteName.setText(model.getCameraName());

            if (position == previous){
                layout.setBackgroundColor(context.getResources().getColor(android.R.color.holo_red_light));
            }else {

                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    layout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.background_selector) );
                } else {
                    layout.setBackground(ContextCompat.getDrawable(context, R.drawable.background_selector));
                }
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!GeneralConstants.IS_PROCESSING){
                        previous = position;
                        notifyDataSetChanged();
                        listener.onItemClick(getLayoutPosition());
                    }



                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.unit_item_layout, parent, false);
        ButterKnife.bind(this, view);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Camera item = mDataSet.get(position);


        //Todo: Setup viewholder for item 
        holder.bind(item, position,onItemClickListener);
    }


    public void addItems(ArrayList<Camera> items) {
        mDataSet.addAll(items);

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
        void onItemClick(int position);
    }

    public void setClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}