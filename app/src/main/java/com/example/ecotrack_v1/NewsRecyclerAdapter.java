package com.example.ecotrack_v1;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kwabenaberko.newsapilib.models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.NewsViewHolder> {

    List<Article> articleList;
    Context context;
    NewsRecyclerAdapter(List<Article> articleList)
    {
        this.articleList = articleList;
    }
    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_recycler_view, parent, false);

        return new NewsViewHolder(view);
    }
    public void getContext(Context context)
    {
        this.context = context;
    }
    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Article article = articleList.get(position);
        holder.title.setText(article.getTitle());
        holder.source.setText(article.getSource().getName());
        Picasso.get().load(article.getUrlToImage())
                .error(R.drawable.ic_hide_image)
                .placeholder(R.drawable.ic_placeholder_image)
                .into(holder.image);
        holder.itemView.setOnClickListener((v -> {
            Intent it = new Intent(v.getContext(), NewsFullActivity.class);
            it.putExtra("url", article.getUrl());
            v.getContext().startActivity(it);
        }));
    }
    public void updateData(List<Article> data)
    {
        articleList.clear();
        articleList.addAll(data);
    }
    @Override
    public int getItemCount() {
        return articleList.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder{

        TextView title, source;
        ImageView image;
        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.article_title);
            source = itemView.findViewById(R.id.article_source);
            image = itemView.findViewById(R.id.article_image_view);



        }
    }
}
