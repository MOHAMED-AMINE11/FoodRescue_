package abdellah.project.fooddonation.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import abdellah.project.fooddonation.R;

public class NotifFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Gonfler la vue du fragment
        return inflater.inflate(R.layout.fragment_notif, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialisation des composants après que la vue soit prête
        bottomNavigationView = view.findViewById(R.id.bottom_navigation);

        // Récupérer le NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment)
                getChildFragmentManager().findFragmentById(R.id.fragmentContainerView3);

        if (navHostFragment != null) {
            // Obtenir le NavController
            navController = navHostFragment.getNavController();

            // Configurer le BottomNavigationView avec le NavController
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            if (id == R.id.nav_my_help) {
                navController.navigate(R.id.donateFragment);
            } else if (id == R.id.nav_donate) {
                navController.navigate(R.id.myHelpFragment2);
            }
            return true;
        });
    }
}
