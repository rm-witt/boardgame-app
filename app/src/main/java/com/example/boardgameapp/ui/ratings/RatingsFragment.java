package com.example.boardgameapp.ui.ratings;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.boardgameapp.MainActivity;
import com.example.boardgameapp.databinding.FragmentRatingsBinding;
import com.example.boardgameapp.model.Rating;

import java.util.Arrays;
import java.util.List;

public class RatingsFragment extends Fragment {
    private RatingsViewModel ratingsViewModel;
    private FragmentRatingsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ratingsViewModel = new ViewModelProvider(this).get(RatingsViewModel.class);

        binding = FragmentRatingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        assert getArguments() != null;
        int forUser = getArguments().getInt("userID");

        MainActivity main = (MainActivity) requireActivity();
        int userID = main.user.getId();

        List<Double> ratingOptions = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0);
        ArrayAdapter<Double> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, ratingOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.hostSpinner.setAdapter(adapter);
        binding.foodSpinner.setAdapter(adapter);
        binding.eveningSpinner.setAdapter(adapter);

        binding.rateButton.setOnClickListener(v -> {
            String comment = binding.commentText.getText().toString();
            Double hostRating = Double.valueOf(binding.hostSpinner.getSelectedItem().toString());
            Double foodRating = Double.valueOf(binding.foodSpinner.getSelectedItem().toString());
            Double eveningRating = Double.valueOf(binding.eveningSpinner.getSelectedItem().toString());

            Rating rating = new Rating(0, userID, forUser, comment, hostRating, foodRating, eveningRating, "");
            ratingsViewModel.sendRating(rating);

            Toast.makeText(requireActivity(), "Du hast die Bewertung erfolgreich abgegeben", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(requireView()).navigateUp();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}