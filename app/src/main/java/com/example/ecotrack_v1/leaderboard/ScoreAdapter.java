package com.example.ecotrack_v1.leaderboard;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecotrack_v1.FireBaseUtil;
import com.example.ecotrack_v1.R;
import com.example.ecotrack_v1.UserModel;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ScoreAdapter  extends RecyclerView.Adapter<ScoreAdapter.ScoreViewAdapter> {
    List<ScoreData> list;
    Context context;
    UserModel currentUser;

    public ScoreAdapter(List<ScoreData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ScoreViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.score_list_item,parent,false);
        return new ScoreViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewAdapter holder, int position) {
        ScoreData currentItem = list.get(position);
        AtomicBoolean gotUser = new AtomicBoolean(false);
        FireBaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            currentUser = task.getResult().toObject(UserModel.class);
            if(currentUser.getFullname() == currentItem.getFullname())
            {
                gotUser.set(true);
                holder.name.setText("YOU");
                holder.name.setTextColor(ContextCompat.getColor(context, R.color.appGreen));
            }
        });

        if(!gotUser.get()) {
            holder.name.setText(currentItem.getFullname());
        }

        holder.green_point.setText("Points:"+String.valueOf(currentItem.getGreenPoints()));
        holder.rank.setText(String.valueOf(position+1));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class ScoreViewAdapter extends RecyclerView.ViewHolder {
        //ImageView imageView;
        TextView name,green_point,rank;

        public ScoreViewAdapter(@NonNull View itemView) {
            super(itemView);
            //imageView = itemView.findViewById(R.id.score_user_img);
            name = itemView.findViewById(R.id.score_user_name);
            green_point = itemView.findViewById(R.id.score_user_green_point);
            rank = itemView.findViewById(R.id.score_user_rank);
        }
    }
}