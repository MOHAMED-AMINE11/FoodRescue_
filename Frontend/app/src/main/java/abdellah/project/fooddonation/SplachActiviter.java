package abdellah.project.fooddonation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import abdellah.project.fooddonation.activities.SignInActivity;
import abdellah.project.fooddonation.activities.designeActivity;

public class SplachActiviter extends AppCompatActivity {
    private static final int SPLASH_DELAY = 7000; // 2 secondes en millisecondes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splach_activiter);

        ImageView gifImageView = findViewById(R.id.gifImageView);

        // Chargez votre GIF avec Glide
        Glide.with(this)
                .asGif()
                .load(R.drawable.donation)
                .into(gifImageView);

        // Ajouter le délai de 2 secondes
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Remplacez MainActivity.class par votre activité de destination
                Intent intent = new Intent(SplachActiviter.this, designeActivity.class);
                startActivity(intent);
                finish(); // Ferme l'activité splash pour empêcher le retour arrière
            }
        }, SPLASH_DELAY);
    }
}