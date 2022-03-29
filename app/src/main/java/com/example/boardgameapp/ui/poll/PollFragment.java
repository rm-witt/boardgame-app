package com.example.boardgameapp.ui.poll;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.boardgameapp.MainActivity;
import com.example.boardgameapp.R;
import com.example.boardgameapp.databinding.FragmentPollBinding;
import com.example.boardgameapp.databinding.ItemPollBinding;
import com.example.boardgameapp.model.Game;
import com.example.boardgameapp.model.GroupInfos;
import com.example.boardgameapp.model.Message;
import com.example.boardgameapp.model.Poll;
import com.example.boardgameapp.ui.chat.ChatViewModel;
import com.example.boardgameapp.ui.collection.CollectionViewModel;
import com.example.boardgameapp.ui.home.HomeViewModel;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.databinding.BindableItem;

import java.util.ArrayList;
import java.util.Locale;

public class PollFragment extends Fragment {
    private int userID;

    private PollViewModel pollViewModel;
    private FragmentPollBinding binding;
    private ChatViewModel chatViewModel;
    private HomeViewModel homeViewModel;
    private CollectionViewModel collectionViewModel;

    private final GroupAdapter<GroupieViewHolder> pollAdapter = new GroupAdapter<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        collectionViewModel = new ViewModelProvider(this).get(CollectionViewModel.class);
        pollViewModel = new ViewModelProvider(this).get(PollViewModel.class);
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentPollBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        MainActivity main = (MainActivity) requireActivity();
        userID = main.user.getId();

        binding.pollRecyclerView.setAdapter(pollAdapter);

        ArrayList<Game> games = collectionViewModel.getGames();
        ArrayList<String> gameNames = new ArrayList<>();
        for (Game game : games) {
            gameNames.add(game.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, gameNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.gamesSpinner.setAdapter(adapter);

        binding.voteButton.setOnClickListener(v -> {
            String selectedGame = binding.gamesSpinner.getSelectedItem().toString();
            int gameId = 0;
            for (Game game : games) {
                if (game.getName().equals(selectedGame)) {
                    gameId = game.getId();
                    break;
                }
            }
            pollViewModel.voteForGame(gameId, userID);

            // Liste mit Abstimmungsergebnissen aktualisieren, sobald ein Nutzer abgestimmt hat & Benachrichtigung via Toast
            pollAdapter.clear();
            ArrayList<Poll> newPolls = pollViewModel.getPolls();
            displayPolls(newPolls);
            Toast.makeText(getActivity(), "Du hast abgestimmt", Toast.LENGTH_SHORT).show();
        });

        GroupInfos groupInfos = homeViewModel.getGroupInfos();
        Boolean pollStatus = groupInfos.getPollActive();
        if (pollStatus.equals(true)) {
            ArrayList<Poll> polls = pollViewModel.getPolls();
            displayPollInformation(polls);
        } else {
            hidePollInformation();
        }

        int hostId = groupInfos.getNextHost();
        if (userID == hostId) {
            binding.pollSwitch.setVisibility(View.VISIBLE);
        } else {
            binding.pollSwitch.setVisibility(View.INVISIBLE);
        }

        binding.pollSwitch.setOnClickListener(v -> {
            if (binding.pollSwitch.isChecked()) {
                pollViewModel.setPollActive();
                pollViewModel.resetVotes();

                binding.pollSwitch.setVisibility(View.VISIBLE);
                displayPollInformation(new ArrayList<>());

                // Chatnachricht sobald Abstimmung gestartet wurde
                Message msg = new Message("Die Abstimmung für den Abend bei mir wurde gestartet.", "", "");
                chatViewModel.sendMessage(msg, userID);
            } else {
                pollViewModel.setPollInactive();
                int nextHostId = hostId + 1;
                if (nextHostId > homeViewModel.getNumberUsers()) {
                    nextHostId = 1;
                }
                pollViewModel.setNextHost(nextHostId);

                binding.pollSwitch.setVisibility(View.INVISIBLE);
                hidePollInformation();
                Toast.makeText(getActivity(), "Die Abstimmung wurde beendet", Toast.LENGTH_SHORT).show();

                // Chatnachricht sobald Abstimmung beendet wurde
                Message msg = new Message("Die Abstimmung für meinen Abend wurde beendet.", "", "");
                chatViewModel.sendMessage(msg, userID);

                // Chatnachricht mit den Ergebnissen
                ArrayList<Poll> pollResults = pollViewModel.getPolls();
                StringBuilder resultStrings = new StringBuilder();
                for (Poll poll : pollResults) {
                    resultStrings.append(String.format(Locale.ENGLISH, "%d Stimmen für %s\n", poll.getGameVotes(), poll.getGameName()));
                }
                Message resultMsg = new Message(resultStrings.toString(), "", "");
                chatViewModel.sendMessage(resultMsg, userID);

                // Chatnachricht mit Information über den neuen Gastgeber
                Message nextHostMsg = new Message(String.format("Die nächste Veranstaltung findet bei %s statt.", homeViewModel.getName(nextHostId)), "", "");
                chatViewModel.sendMessage(nextHostMsg, userID);
            }
        });

        return root;
    }

    private void hidePollInformation() {
        binding.pollSwitch.setChecked(false);
        binding.pollSwitch.setText(R.string.poll_is_not_active);

        binding.pollNoticeInactive.setVisibility(View.VISIBLE);
        binding.pollResults.setVisibility(View.INVISIBLE);
        binding.voteButton.setVisibility(View.INVISIBLE);
        binding.gamesSpinner.setVisibility(View.INVISIBLE);

        pollAdapter.clear();
    }

    private void displayPollInformation(ArrayList<Poll> polls) {
        binding.pollSwitch.setChecked(true);
        binding.pollSwitch.setText(R.string.poll_is_active);

        binding.pollNoticeInactive.setVisibility(View.INVISIBLE);
        binding.pollResults.setVisibility(View.VISIBLE);
        binding.voteButton.setVisibility(View.VISIBLE);
        binding.gamesSpinner.setVisibility(View.VISIBLE);

        displayPolls(polls);
    }

    private void displayPoll(Poll poll) {
        PollItem item = new PollItem(poll);
        pollAdapter.add(item);
    }

    private void displayPolls(ArrayList<Poll> polls) {
        for (Poll poll : polls) {
            displayPoll(poll);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

class PollItem extends BindableItem<ItemPollBinding> {
    private final Poll poll;

    public PollItem(Poll poll) {
        this.poll = poll;
    }

    @Override
    public void bind(@NonNull ItemPollBinding viewBinding, int position) {
        viewBinding.setPoll(poll);
    }

    @Override
    public int getLayout() {
        return R.layout.item_poll;
    }
}

