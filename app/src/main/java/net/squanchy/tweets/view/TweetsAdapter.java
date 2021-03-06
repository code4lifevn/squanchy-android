package net.squanchy.tweets.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.squanchy.R;
import net.squanchy.tweets.domain.view.TweetViewModel;

public class TweetsAdapter extends RecyclerView.Adapter<TweetViewHolder> {

    private final Context context;

    private List<TweetViewModel> tweets;
    private TweetItemView.OnTweetClickedListener listener;

    public TweetsAdapter(Context context) {
        this.context = context;
        this.tweets = new ArrayList<>();
    }

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new TweetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TweetViewHolder holder, int position) {
        holder.updateWith(tweets.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public boolean isEmpty() {
        return tweets.isEmpty();
    }

    public void updateWith(List<TweetViewModel> tweets, TweetItemView.OnTweetClickedListener listener) {
        this.tweets = Collections.unmodifiableList(tweets);
        this.listener = listener;
        notifyDataSetChanged();
    }
}
