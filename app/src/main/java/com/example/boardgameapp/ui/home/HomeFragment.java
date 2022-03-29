package com.example.boardgameapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.boardgameapp.MainActivity;
import com.example.boardgameapp.R;
import com.example.boardgameapp.databinding.FragmentHomeBinding;
import com.example.boardgameapp.model.GroupInfos;
import com.example.boardgameapp.model.Message;
import com.example.boardgameapp.ui.chat.ChatViewModel;

public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private ChatViewModel chatViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        GroupInfos groupInfos = homeViewModel.getGroupInfos();

        MainActivity main = (MainActivity) requireActivity();
        int userId = main.user.getId();
        int hostId = groupInfos.getNextHost();

        String hostName = homeViewModel.getName(hostId);
        String groupName = groupInfos.getName();
        String groupDescription = groupInfos.getDescription();
        binding.currentHost.setText(hostName);
        binding.groupName.setText(groupName);
        binding.groupDescription.setText(groupDescription);

        int nextHost = hostId + 1;
        if (nextHost > homeViewModel.getNumberUsers()) {
            nextHost = 1;
        }

        String nextHostName = homeViewModel.getName(nextHost);
        binding.nextHost.setText(nextHostName);

        if (userId == hostId) {
            binding.editDateButton.setVisibility(View.VISIBLE);
        } else {
            binding.editDateButton.setVisibility(View.INVISIBLE);
        }

        String meeting = groupInfos.getNextMeeting();
        meeting = chatViewModel.convertDate(meeting);
        binding.editDate.setText(meeting);

        binding.editDateButton.setOnClickListener(v -> {
            String date = binding.editDate.getText().toString();
            homeViewModel.setNextMeeting(date);

            // Send chat message
            Message msg = new Message(String.format("Der Termin wurde geändert auf den %s", date), "", "");
            chatViewModel.sendMessage(msg, userId);
        });

        binding.navChatButton.setOnClickListener(v ->
                Navigation.findNavController(root).navigate(R.id.action_nav_home_to_nav_chat));
        binding.navMembersButton.setOnClickListener(v ->
                Navigation.findNavController(root).navigate(R.id.action_nav_home_to_nav_members));
        binding.navCollectionButton.setOnClickListener(v ->
                Navigation.findNavController(root).navigate(R.id.action_nav_home_to_nav_collection));
        binding.navPollButton.setOnClickListener(v ->
                Navigation.findNavController(root).navigate(R.id.action_nav_home_to_nav_poll));

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}