package com.appdev360.jobsitesentry.ui.drone;

import android.app.Activity;
import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.data.model.Video;
import com.appdev360.jobsitesentry.injection.ActivityContext;
import com.appdev360.jobsitesentry.util.GeneralUtils;
import com.appdev360.jobsitesentry.widget.TextViewCustom;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Abubaker on 09,April,2018
 */
public class VideoAdapter extends
        RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private static final String TAG = VideoAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<Video> mDataSet;
    private OnItemClickListener onItemClickListener;

    @Inject
    public VideoAdapter(@ActivityContext Context context) {
        this.context = context;
        mDataSet = new ArrayList<>();

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

        public void bind(final Video model, final VideoAdapter.OnItemClickListener listener) {

            videoName.setText(model.getName());

            int screenWidth = GeneralUtils.getScreenWidth((Activity) context);
            int screenHeight = GeneralUtils.getScreenHeight((Activity) context);

            thumbnail.getLayoutParams().width = screenWidth / 4;
            thumbnail.getLayoutParams().height = screenWidth / 4;


            Picasso.get().load(model.getThumb()).placeholder(ContextCompat.getDrawable(context, R.drawable
                    .app_logo)).centerCrop().resize(screenWidth / 4, screenWidth / 4).into
                    (thumbnail, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });



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

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.drone_video_item_adapter, parent, false);
        ButterKnife.bind(this, view);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Video item = mDataSet.get(position);

        //Todo: Setup viewholder for item 
        holder.bind(item, onItemClickListener);
    }

    public void setVideoItems(ArrayList<Video> items) {
        mDataSet = items;
    }

    public void addItems(ArrayList<Video> items) {
        mDataSet.addAll(items);

    }

    public void clear() {
        mDataSet.clear();
        notifyDataSetChanged();
    }

    public ArrayList<Video> getDataSet() {
        return mDataSet;
    }


    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Video model);
    }

    public void setClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}