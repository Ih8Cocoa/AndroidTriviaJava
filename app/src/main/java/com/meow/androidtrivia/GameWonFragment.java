package com.meow.androidtrivia;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ShareCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.meow.androidtrivia.databinding.FragmentGameWonBinding;

public class GameWonFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate the layout and store the binding variable
        final FragmentGameWonBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_game_won, container, false
        );

        // (9) do the same thing to GameWonFragment
        final NavDirections target =
                GameWonFragmentDirections.actionGameWonFragmentToGameFragment();
        final OnClickListener listener = Navigation.createNavigateOnClickListener(target);
        binding.nextMatchButton.setOnClickListener(listener);

        // (19) Try getting the arguments passed from GameFragment
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

        // (21) Since we've won, we need to enable the ability to share our result
        // Tell the fragment that it has an options menu
        setHasOptionsMenu(true);

        return binding.getRoot();
    }

    // (22) Define actions to do when options menu is created
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // (23) Inflate the winner's menu
        inflater.inflate(R.menu.winner_menu, menu);
        // (copy-paste) Define a fallback action in case our intent can't be resolved.
        // This prevents crashing
        final FragmentActivity activity = getActivity();
        final Intent shareIntent = getShareIntent();
        if (activity == null || shareIntent == null) {
            return;
        }
        final PackageManager pm = activity.getPackageManager();
        final ComponentName name = shareIntent.resolveActivity(pm);
        if (name == null) {
            // If this if statement is true, our activity failed to resolve for some reasons
            // so just hide the menu
            menu.findItem(R.id.share).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // (29) Override the default action when Options Item is selected
        final int id = item.getItemId();
        if (id == R.id.share) {
            // (30) if the intent ID is sharing, start sharing
            startSharing();
        }
        return super.onOptionsItemSelected(item);
    }

    // (24) Create our sharing intent
    @Nullable
    private Intent getShareIntent() {
        // (25) first off, check if our activity and the bundle is null
        // if either is null, return null
        final FragmentActivity activity = getActivity();
        final Bundle bundle = getArguments();
        if (activity == null || bundle == null) {
            return null;
        }

        // Try getting the argument passed into the GameWonFragment
        final GameWonFragmentArgs args = GameWonFragmentArgs.fromBundle(bundle);
        // (26) define the data to put into our intent (here, the data is plaintext)
        final String str = getString(R.string.share_success_text, args.getNumCorrect(), args.getNumQuestions());
        // (27) Then, create our intent from the intent builder, and return it to the caller
        return ShareCompat.IntentBuilder.from(activity).setText(str)
                .setType("text/plain")
                .getIntent();
    }

    // (28) Call this method to start the sharing intent
    private void startSharing() {
        final Intent sharingIntent = getShareIntent();
        startActivity(sharingIntent);
    }
}
