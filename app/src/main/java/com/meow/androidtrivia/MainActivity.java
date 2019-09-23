package com.meow.androidtrivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.meow.androidtrivia.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    // (31) Create a field to initialize the DrawerLayout
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        // initialize our drawerLayout
        drawerLayout = binding.drawerLayout;
        // (10) now, let's setup navigation buttons on action bar
        // first, find the current view's Navigation Controller
        final NavController navController = Navigation.findNavController(
                this, R.id.my_nav_host_fragment
        );
        // (11) then setup the action bar with navigation buttons
        NavigationUI.setupActionBarWithNavController(this, navController,
                // (32) the 3rd argument for this method is optional.
                // Include our drawer layout when setting up the action bar
                drawerLayout
        );

        // (35) A small fix to prevent pulling out the drawer during the quiz
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            final int destinationId = destination.getId();
            final int homeScreen = controller.getGraph().getStartDestination();
            final int lockMode;
            // if the current point is the home screen, unlock the drawer,
            // otherwise lock it
            if (homeScreen == destinationId) {
                lockMode = DrawerLayout.LOCK_MODE_UNLOCKED;
            } else {
                lockMode = DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
            }
            drawerLayout.setDrawerLockMode(lockMode);
        });

        // (33) Also, setup our drawer as the secondary navigation control
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    // (12) Define what to do when the "up" button is pressed - call
    // navigateUp() on my_nav_host_fragment
    @Override
    public boolean onSupportNavigateUp() {
        final NavController controller =
                Navigation.findNavController(this, R.id.my_nav_host_fragment);
        // return controller.navigateUp(); (deleted after 33)

        // (34) update the return value to include our drawer layout
        return NavigationUI.navigateUp(controller, drawerLayout);
    }
}
