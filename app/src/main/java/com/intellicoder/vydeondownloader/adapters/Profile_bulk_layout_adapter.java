package com.intellicoder.vydeondownloader.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.intellicoder.vydeondownloader.BulkDownloader_ProfileActivity;
import com.intellicoder.vydeondownloader.R;
import com.intellicoder.vydeondownloader.models.bulkdownloader.UserUser;

import java.util.List;

public class Profile_bulk_layout_adapter extends RecyclerView.Adapter<Profile_bulk_layout_adapter.Viewholder> {

    private List<UserUser> data;

    private Context context;

    public Profile_bulk_layout_adapter(List<UserUser> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public static class Viewholder extends RecyclerView.ViewHolder {

        public LinearLayout row_search_lay;
        public com.makeramen.roundedimageview.RoundedImageView row_search_imageview;
        public ImageView row_search_private_imageview;
        public TextView row_search_name_textview;
        public ImageView row_search_approved_imageview;
        public TextView row_search_detail_textview;


        public Viewholder(View itemView) {
            super(itemView);

            row_search_lay = (LinearLayout) itemView.findViewById(R.id.row_search_lay);
            row_search_imageview = (com.makeramen.roundedimageview.RoundedImageView) itemView.findViewById(R.id.row_search_imageview);
            row_search_private_imageview = (ImageView) itemView.findViewById(R.id.row_search_private_imageview);
            row_search_name_textview = (TextView) itemView.findViewById(R.id.row_search_name_textview);
            row_search_approved_imageview = (ImageView) itemView.findViewById(R.id.row_search_approved_imageview);
            row_search_detail_textview = (TextView) itemView.findViewById(R.id.row_search_detail_textview);

        }
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_row_bulk_downloader_layout, parent, false);

        return new Viewholder(itemView);
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {

        UserUser model = data.get(position);
        System.out.println("fhjsdfjks " + model.is_verified);
        Glide.with(context).load(model.profile_pic_url).into(holder.row_search_imageview);
        holder.row_search_name_textview.setText(model.fullName);
        holder.row_search_detail_textview.setText(model.username);

        if (!model.is_verified) {
            holder.row_search_approved_imageview.setVisibility(View.GONE);

        } else {
            holder.row_search_approved_imageview.setVisibility(View.VISIBLE);

        }

        if (!model.is_private) {
            holder.row_search_private_imageview.setVisibility(View.GONE);

        } else {
            holder.row_search_private_imageview.setVisibility(View.VISIBLE);

        }

        holder.row_search_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                context.startActivity(new Intent(context, BulkDownloader_ProfileActivity.class).putExtra("username", model.username).putExtra("pkid", model.pk));


            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
