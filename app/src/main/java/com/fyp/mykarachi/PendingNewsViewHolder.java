package com.fyp.mykarachi;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;

import androidx.recyclerview.widget.RecyclerView;

public class PendingNewsViewHolder extends RecyclerView.ViewHolder {

    public static final String TAG = PendingNewsViewHolder.class.getSimpleName();
    private View mView;
    private Context mContext;

    public PendingNewsViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
    }

    public void bindNews(News news) {
        final TextView displayName = mView.findViewById(R.id.pendingNewsDisplayName);
        final TextView newsText = mView.findViewById(R.id.pendingNewsText);
        final TextView location = mView.findViewById(R.id.pendingNewsLocation);
        final LikeButton thumbUp = mView.findViewById(R.id.pendingNewsThumbUp);
        final LikeButton thumbDown = mView.findViewById(R.id.pendingNewsThumbDown);

        displayName.setText(news.getAuthorName());
        newsText.setText(news.getDetails());
        location.setText(news.getLocation());

        thumbUp.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                thumbUp.setLiked(true);
                thumbDown.setLiked(false);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                thumbUp.setLiked(false);
            }
        });

        thumbDown.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                thumbDown.setLiked(true);
                thumbUp.setLiked(false);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                thumbDown.setLiked(false);
            }
        });

        FirebaseDatabase.getInstance().getReference().child("Users").child(news.getAuthor()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final int userCredibility = Integer.parseInt(dataSnapshot.child("Credibility").getValue().toString());
                for (int i = 1; i <= userCredibility; i++) {
                    int id = mContext.getResources().getIdentifier("pendingNewsStar" + Integer.toString(i), "id", mContext.getPackageName());
                    ((ImageView) mView.findViewById(id)).setImageResource(R.drawable.ic_round_star_24px);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
