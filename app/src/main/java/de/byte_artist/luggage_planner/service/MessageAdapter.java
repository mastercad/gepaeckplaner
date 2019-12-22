package de.byte_artist.luggage_planner.service;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.byte_artist.luggage_planner.R;
import de.byte_artist.luggage_planner.entity.Message;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Message> messageList;

    private static final int SENDER = 0;
    private static final int RECIPIENT = 1;

    public MessageAdapter(List<Message> messages) {
        messageList = messages;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public ViewHolder(LinearLayout v) {
            super(v);
            mTextView = v.findViewById(R.id.text);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_purple, parent, false);
            return new ViewHolder((LinearLayout) v);
        }
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_green, parent, false);
        return new ViewHolder((LinearLayout) v);
    }

    public void remove(int pos) {
        messageList.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, messageList.size());

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTextView.setText(messageList.get(position).getMessage());
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);

        if (message.getSenderName().equals("Me")) {
            return SENDER;
        } else {
            return RECIPIENT;
        }
    }

}