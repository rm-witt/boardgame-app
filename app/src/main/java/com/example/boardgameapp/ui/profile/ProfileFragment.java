package com.example.boardgameapp.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.boardgameapp.R;
import com.example.boardgameapp.databinding.FragmentProfileBinding;
import com.example.boardgameapp.databinding.ItemRatingBinding;
import com.example.boardgameapp.model.Rating;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.databinding.BindableItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProfileFragment extends Fragment {
    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;

    private final GroupAdapter<GroupieViewHolder> ratingsAdapter = new GroupAdapter<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        assert getArguments() != null;
        int userID = getArguments().getInt("userID");
        String username = getArguments().getString("username");

        ArrayList<Rating> ratings = profileViewModel.getRatingsFor(userID);
        ratingsAdapter.clear();
        binding.ratingsRecyclerView.setAdapter(ratingsAdapter);
        displayRatings(ratings);

        binding.profileName.setText(username);

        List<Double> averages = profileViewModel.getAverages(ratings);
        String averageHost = String.format(Locale.ENGLISH, "%.1f", averages.get(0));
        binding.averageHost.setText(averageHost);
        String averageFood = String.format(Locale.ENGLISH, "%.1f", averages.get(1));
        binding.averageFood.setText(averageFood);
        String averageEvening = String.format(Locale.ENGLISH, "%.1f", averages.get(2));
        binding.averageEvening.setText(averageEvening);

        binding.ratingButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("userID", userID);
            Navigation.findNavController(requireView()).navigate(R.id.nav_ratings, bundle);
        });

        return root;
    }

    private void displayRating(Rating rating) {
        RatingItem item = new RatingItem(rating);
        ratingsAdapter.add(item);
    }

    private void displayRatings(ArrayList<Rating> ratings) {
        for (Rating rating : ratings) {
            displayRating(rating);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

class RatingItem extends BindableItem<ItemRatingBinding> {
    private final Rating rating;

    public RatingItem(Rating rating) {
        this.rating = rating;
    }

    @Override
    public void bind(@NonNull ItemRatingBinding viewBinding, int position) {
        viewBinding.setRating(rating);
    }

    @Override
    public int getLayout() {
        return R.layout.item_rating;
    }
}

