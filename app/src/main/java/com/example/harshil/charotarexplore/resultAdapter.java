package com.example.harshil.charotarexplore;

import android.content.Context;
import android.content.Intent;
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
                intent.putExtra("name", datas.get(position).getName());
                intent.putExtra("number", datas.get(position).getNumber());
                intent.putExtra("address", datas.get(position).getAddress());
                intent.putExtra("time", datas.get(position).getTime());
                intent.putExtra("lat", datas.get(position).getLatitude());
                intent.putExtra("lon", datas.get(position).getLongitude());
                intent.putExtra("image", datas.get(position).getImage());
                context.startActivity(intent);
            }
        });

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.image.performClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}
