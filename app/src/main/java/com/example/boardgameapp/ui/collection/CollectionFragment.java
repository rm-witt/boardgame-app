package com.example.boardgameapp.ui.collection;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.boardgameapp.MainActivity;
import com.example.boardgameapp.R;
import com.example.boardgameapp.databinding.ItemCollectionBinding;
import com.example.boardgameapp.databinding.FragmentCollectionBinding;
import com.example.boardgameapp.model.Game;
import com.example.boardgameapp.model.Message;
import com.example.boardgameapp.ui.chat.ChatViewModel;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.databinding.BindableItem;

import java.util.ArrayList;

public class CollectionFragment extends Fragment {
    private int userID;

    private CollectionViewModel collectionViewModel;
    private FragmentCollectionBinding binding;
    private ChatViewModel chatViewModel;
    private final GroupAdapter<GroupieViewHolder> collectionAdapter = new GroupAdapter<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        collectionViewModel = new ViewModelProvider(this).get(CollectionViewModel.class);
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        binding = FragmentCollectionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        MainActivity main = (MainActivity) requireActivity();
        userID = main.user.getId();

        binding.collectionRecyclerView.setAdapter(collectionAdapter);
        displayCollection(collectionViewModel.getGames());

        binding.sendGameButton.setOnClickListener(v -> {
            String name = binding.gameInput.getText().toString();
            collectionViewModel.sendGame(name);

            Game game = new Game(0, name);

            displayGame(game);
            binding.gameInput.setText("");

            Message msg = new Message(String.format("Die Spielauswahl hat sich um %s erweitert.", name), "", "");
            chatViewModel.sendMessage(msg, userID);
        });

        return root;
    }

    private void displayGame(Game gm) {
        GameItem item = new GameItem(gm);
        collectionAdapter.add(item);
    }

    private void displayCollection(ArrayList<Game> gms) {
        for (Game gm : gms) {
            displayGame(gm);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

class GameItem extends BindableItem<ItemCollectionBinding> {
    private final Game game;

    public GameItem(Game game) {
        this.game = game;
    }

    @Override
    public void bind(@NonNull ItemCollectionBinding viewBinding, int position) {
        viewBinding.setGame(game);
    }

    @Override
    public int getLayout() {
        return R.layout.item_collection;
    }
}
