package com.fyp.mykarachi;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class TweetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final int MAX_WIDTH = 60;
    private static final int MAX_HEIGHT = 60;
    private View mView;
    private Context mContext;

    public TweetViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
    }

    public void bindTweet(Tweet tweet) {
        ImageView profilePicture = mView.findViewById(R.id.tweetProfilePicture);
        TextView displayName = mView.findViewById(R.id.tweetDisplayName);
        TextView userName = mView.findViewById(R.id.tweetUserName);
        TextView tweetText = mView.findViewById(R.id.tweetText);
        TextView timestamp = mView.findViewById(R.id.tweetTimestamp);

        Picasso.get()
                .load(tweet.getProfile_image_url())
                .placeholder(R.drawable.ic_round_account_circle_24px)
                .error(R.drawable.ic_round_account_circle_24px)
                .resize(MAX_WIDTH, MAX_HEIGHT)
                .centerCrop()
                .into(profilePicture);

        displayName.setText(tweet.getName());
        userName.setText("@" + tweet.getScreen_name());
        tweetText.setText(tweet.getUpdate());
        timestamp.setText(tweet.getTime_stamp());
    }

    @Override
    public void onClick(View view) {
        final ArrayList<Tweet> tweets = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Updates");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    tweets.add(snapshot.getValue(Tweet.class));
                }

//                int itemPosition = getLayoutPosition();

//                Intent intent = new Intent(mContext, RestaurantDetailActivity.class);
//                intent.putExtra("position", itemPosition + "");
//                intent.putExtra("restaurants", Parcels.wrap(restaurants));
//
//                mContext.startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(mContext, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
