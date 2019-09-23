package com.meow.androidtrivia;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.meow.androidtrivia.databinding.FragmentTitleBinding;

public class TitleFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        // (1) create the fragment binding
        final FragmentTitleBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_title, container, false
        );

        // (2) Use this method to create a navigation on-click listener,
        // which we can use to feed it to the button's on-click listener
        final NavDirections target = TitleFragmentDirections.actionTitleFragmentToGameFragment();
        final OnClickListener listener = Navigation.createNavigateOnClickListener(target);

        // (3) pass our listener object into the button's setOnClickListener method
        binding.playButton.setOnClickListener(listener);

        // (13) tell this fragment that it has options menu
        setHasOptionsMenu(true);

        return binding.getRoot();
    }

    // setup options menu
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // (14) inflate the menu
        inflater.inflate(R.menu.overflow_menu, menu);
    }

    // (15) execute when an option is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // (16) check if the current view is null, if it is then exit
        final View currentView = getView();
        if (currentView == null) {
            // (17) If the currentView is null, use the default behavior
            return super.onOptionsItemSelected(item);
        }
        // (18) otherwise, try overriding the default behavior,
        // but still falls back to default if things go wrong
        final NavController controller = Navigation.findNavController(currentView);
        return NavigationUI.onNavDestinationSelected(item, controller)
                || super.onOptionsItemSelected(item);

    }
}
