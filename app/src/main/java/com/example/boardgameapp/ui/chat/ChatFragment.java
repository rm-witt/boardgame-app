package com.example.boardgameapp.ui.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.boardgameapp.MainActivity;
import com.example.boardgameapp.R;
import com.example.boardgameapp.databinding.FragmentChatBinding;
import com.example.boardgameapp.databinding.ItemMessageReceiveBinding;
import com.example.boardgameapp.databinding.ItemMessageSendBinding;
import com.example.boardgameapp.model.Message;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.databinding.BindableItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatFragment extends Fragment {
    private int userID;
    private String username;

    private ChatViewModel chatViewModel;
    private FragmentChatBinding binding;

    private final GroupAdapter<GroupieViewHolder> messageAdapter = new GroupAdapter<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        binding = FragmentChatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        MainActivity main = (MainActivity) requireActivity();
        userID = main.user.getId();
        username = main.user.getUsername();

        binding.messageRecyclerView.setAdapter(messageAdapter);
        displayMessages(chatViewModel.getMessages());

        binding.sendMessageButton.setOnClickListener(v -> {
            String content = binding.messageInput.getText().toString();
            String timeSent = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH).format(new Date());
            Message message = new Message(content, username, timeSent);
            chatViewModel.sendMessage(message, userID);
            displayMessage(message);

            binding.messageInput.setText("");
            binding.messageRecyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
        });

        binding.messageRecyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);

        return root;
    }

    private void displayMessage(Message msg) {
        if (msg.getFrom().equals(username)) {
            SendMessageItem item = new SendMessageItem(msg);
            messageAdapter.add(item);
        } else {
            ReceiveMessageItem item = new ReceiveMessageItem(msg);
            messageAdapter.add(item);
        }
    }

    private void displayMessages(ArrayList<Message> msgs) {
        for (Message msg : msgs) {
            displayMessage(msg);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

class SendMessageItem extends BindableItem<ItemMessageSendBinding> {
    private final Message message;

    public SendMessageItem(Message message) {
        this.message = message;
    }

    @Override
    public int getLayout() {
        return R.layout.item_message_send;
    }

    @Override
    public void bind(@NonNull ItemMessageSendBinding viewBinding, int position) {
        viewBinding.setMessage(message);
    }
}

class ReceiveMessageItem extends BindableItem<ItemMessageReceiveBinding> {
    private final Message message;

    public ReceiveMessageItem(Message message) {
        this.message = message;
    }

    @Override
    public int getLayout() {
        return R.layout.item_message_receive;
    }

    @Override
    public void bind(@NonNull ItemMessageReceiveBinding viewBinding, int position) {
        viewBinding.setMessage(message);
    }
}