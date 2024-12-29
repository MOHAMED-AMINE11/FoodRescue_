package abdellah.project.fooddonation;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private NavHostFragment navHostFragment;
    private NavController navController;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.print (SessionManager.getInstance().getCurrentUser()+"");
        TextView badge = findViewById(R.id.notificationBadge);

// Exemple de condition pour vérifier l'importance
        boolean important = true; // Changez cette valeur selon votre logique

        if (important) {
            badge.setBackgroundResource(R.drawable.notification_badge_important);
        } else {
            badge.setBackgroundResource(R.drawable.notification_badge_background);
        }

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Récupérer le NavHostFragment
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView2);

        if (navHostFragment != null) {
            // Obtenir le NavController à partir du NavHostFragment
            navController = navHostFragment.getNavController();

            // Configurer le BottomNavigationView avec le NavController
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
        }

        // Gestion des clics dans le BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            if (id == R.id.profile) {
                navController.navigate(R.id.userProfileFragment);
            } else if (id == R.id.association) {
                navController.navigate(R.id.associationListFragment);
            } else if (id == R.id.donate) {
                navController.navigate(R.id.DOnationFragmentPrincipalle);
            } else if (id == R.id.home) {
                navController.navigate(R.id.homePageFragment);
            } else if (id == R.id.publication) {
                navController.navigate(R.id.choiseFragement);

            }
            return true;
        });

        // Initialiser l'icône de notification
        imageView = findViewById(R.id.notificationIcon);
        imageView.setOnClickListener(v -> {
            if (navController != null) {
                // Naviguer vers la page des notifications
                navController.navigate(R.id.notifFragment);
            }
        });
    }
}
