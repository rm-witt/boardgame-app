package com.example.boardgameapp.ui.members;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.boardgameapp.R;
import com.example.boardgameapp.databinding.FragmentMembersBinding;
import com.example.boardgameapp.databinding.ItemMembersBinding;
import com.example.boardgameapp.model.GroupInfos;
import com.example.boardgameapp.model.User;
import com.example.boardgameapp.ui.home.HomeViewModel;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.databinding.BindableItem;

import java.util.ArrayList;

public class MembersFragment extends Fragment {
    private MembersViewModel membersViewModel;
    private HomeViewModel homeViewModel;
    private FragmentMembersBinding binding;

    private final GroupAdapter<GroupieViewHolder> membersAdapter = new GroupAdapter<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        membersViewModel = new ViewModelProvider(this).get(MembersViewModel.class);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentMembersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        GroupInfos groupInfos = homeViewModel.getGroupInfos();
        membersAdapter.clear();
        binding.membersRecyclerView.setAdapter(membersAdapter);
        displayUsers(membersViewModel.getUsers(), groupInfos.getCreatedBy(), groupInfos.getNextHost());

        return root;
    }

    private void displayUser(User user, int admin, int host) {
        MemberItem item = new MemberItem(user, admin, host);
        membersAdapter.add(item);
    }

    private void displayUsers(ArrayList<User> users, int adminId, int hostId) {
        for (User user : users) {
            displayUser(user, adminId, hostId);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}

class MemberItem extends BindableItem<ItemMembersBinding> {
    private final User user;
    private final int adminId, hostId;

    public MemberItem(User user, int adminId, int hostId) {
        this.user = user;
        this.adminId = adminId;
        this.hostId = hostId;
    }

    @Override
    public void bind(@NonNull ItemMembersBinding viewBinding, int position) {
        if (user.getId() == adminId) {
            if (user.getId() == hostId) {
                viewBinding.isHostAdmin.setText(R.string.member_is_host_and_admin);
            } else {
                viewBinding.isHostAdmin.setText(R.string.member_is_admin);
            }
        } else if (user.getId() == hostId) {
            viewBinding.isHostAdmin.setText(R.string.member_is_host);
        } else {
            viewBinding.isHostAdmin.setVisibility(View.INVISIBLE);
        }

        viewBinding.setUser(user);

        viewBinding.membersItem.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("userID", user.getId());
            bundle.putString("username", user.getUsername());

            Navigation.findNavController(viewBinding.getRoot()).navigate(R.id.nav_profile, bundle);
        });
    }

    @Override
    public int getLayout() {
        return R.layout.item_members;
    }
}
