package org.touchirc.adapter;

import java.util.LinkedList;

import org.touchirc.model.Conversation;
import org.touchirc.model.Message;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MessageAdapter extends BaseAdapter {
	
	private final LinkedList<TextView> messages;
    private final Context context;
    
    public MessageAdapter(Conversation conversation, Context context)
    {
        LinkedList<TextView> messages = new LinkedList<TextView>();
        
        LinkedList<Message> history = conversation.getHistory();

        for (Message m : history) {
            messages.add(m.getTextView(context));
        }

        this.messages = messages;
        this.context = context;
    }	

	@Override
	public int getCount() {
		return this.messages.size();
	}

	@Override
	public TextView getItem(int position) {
		return this.messages.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getItem(position);
	}
	
	public void addMessages(LinkedList<Message> messages)
    {
        LinkedList<TextView> msg = this.messages;

        for (Message m : messages) {
            msg.add(m.getTextView(this.context));
        }

        notifyDataSetChanged();
    }
	
}
