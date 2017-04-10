package com.example.harshil.charotarexplore;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by harshil on 30-01-2017.
 */

public class resultAdapter extends RecyclerView.Adapter<resultAdapter.ViewHolder> {
    location_details location_details = new location_details();
    private Context context;
    private List<resultData> datas;

    public resultAdapter(Context context, List<resultData> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.base, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final resultAdapter.ViewHolder holder, final int position) {
        Glide.with(context).load(datas.get(position).getImage()).into(holder.image);
        holder.title.setText(datas.get(position).getName());

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, details.class);
                intent.putExtra("id", datas.get(position).getId());
                intent.putExtra("catid", datas.get(position).getCatid());
                intent.putExtra("name", datas.get(position).getName());
                intent.putExtra("number", datas.get(position).getNumber());
                intent.putExtra("address", datas.get(position).getAddress());
                intent.putExtra("time", datas.get(position).getTime());
                intent.putExtra("lat", datas.get(position).getLatitude());
                intent.putExtra("lon", datas.get(position).getLongitude());
                intent.putExtra("image", datas.get(position).getImage());
                intent.putExtra("site", datas.get(position).getSite());
                context.startActivity(intent);
            }
        });

        final Dialog popup = new Dialog(context);
        popup.setContentView(R.layout.popup);
        popup.setCancelable(true);
        final TextView popname = (TextView) popup.findViewById(R.id.popname);
        final ImageView popimage = (ImageView) popup.findViewById(R.id.popimage);
        final TextView popadd = (TextView) popup.findViewById(R.id.popadd);
        holder.image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                popname.setText(datas.get(position).getName());
                Glide.with(context).load(datas.get(position).getImage()).into(popimage);
                popadd.setText(datas.get(position).getAddress());
                popup.show();
                return false;
            }
        });

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.image.performClick();
            }
        });

        Location startPoint = new Location("user_location");
        startPoint.setLatitude(Double.parseDouble(location_details.getLatitude()));
        startPoint.setLongitude(Double.parseDouble(location_details.getLongitude()));

        Location endPoint = new Location("destination_location");
        endPoint.setLatitude(Double.parseDouble(datas.get(position).getLatitude()));
        endPoint.setLongitude(Double.parseDouble(datas.get(position).getLongitude()));

        String distance = String.valueOf((int) startPoint.distanceTo(endPoint) / 1000) + " " + context.getResources().getString(R.string.away);
        holder.distance.setText(distance);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView title, distance;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            distance = (TextView) itemView.findViewById(R.id.distance);
        }
    }
}
