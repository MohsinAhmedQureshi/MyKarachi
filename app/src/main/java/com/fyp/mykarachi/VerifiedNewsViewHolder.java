package com.fyp.mykarachi;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.recyclerview.widget.RecyclerView;

public class VerifiedNewsViewHolder extends RecyclerView.ViewHolder {

    public static final String TAG = VerifiedNewsViewHolder.class.getSimpleName();
    private View mView;
    private Context mContext;

    public VerifiedNewsViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
    }

    public void bindNews(News news) {
        final TextView displayName = mView.findViewById(R.id.verifiedNewsDisplayName);
        final TextView newsText = mView.findViewById(R.id.verifiedNewsText);
        final TextView location = mView.findViewById(R.id.verifiedNewsLocation);

        displayName.setText(news.getAuthorName());
        newsText.setText(news.getDetails());
        location.setText(news.getLocation());

        FirebaseDatabase.getInstance().getReference().child("Users").child(news.getAuthor()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final int userCredibility = Integer.parseInt(dataSnapshot.child("Credibility").getValue().toString());
                for (int i = 1; i <= userCredibility; i++) {
                    int id = mContext.getResources().getIdentifier("verifiedNewsStar" + Integer.toString(i), "id", mContext.getPackageName());
                    ((ImageView) mView.findViewById(id)).setImageResource(R.drawable.ic_round_star_24px);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
