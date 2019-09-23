package com.meow.androidtrivia;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.meow.androidtrivia.databinding.FragmentGameOverBinding;

public class GameOverFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        // inflate the layout and store the binding variable
        final FragmentGameOverBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_game_over, container, false
        );
        // (7) retrieve the navigation on-click listener
        final NavDirections target =
                GameOverFragmentDirections.actionGameOverFragmentToGameFragment();
        final OnClickListener listener = Navigation.createNavigateOnClickListener(target);
        // (8) then feed the listener to the "Try again" button
        binding.tryAgainButton.setOnClickListener(listener);

        // (20) Same for the GameOverFragment
        final Bundle bundle = getArguments();
        if (bundle != null) {
            final GameWonFragmentArgs args = GameWonFragmentArgs.fromBundle(bundle);
            final Context context = getContext();
            // Make a toast
            Toast.makeText(context,
                    "Questions: " + args.getNumQuestions() + ", Correct: " + args.getNumCorrect(),
                    Toast.LENGTH_LONG
            ).show();
        }

        return binding.getRoot();
    }
}
