package abdellah.project.fooddonation.activities;

import static com.google.firebase.perf.network.FirebasePerfOkHttpClient.enqueue;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.button.MaterialButton;

import abdellah.project.fooddonation.MainActivity;
import abdellah.project.fooddonation.R;
import abdellah.project.fooddonation.SessionManager;
import abdellah.project.fooddonation.api.RetrofitClient;
import abdellah.project.fooddonation.entity.AuthRequest;
import abdellah.project.fooddonation.entity.AuthResponse;
import abdellah.project.fooddonation.entity.User;
import abdellah.project.fooddonation.mvvm.UserViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private TextInputLayout nameLayout, emailLayout, phoneLayout, passwordLayout;
    private TextInputEditText nameEditText, emailEditText, phoneEditText, passwordEditText;
    private MaterialButton signUpButton;
    private View signInPrompt;
    private CircularProgressIndicator progressIndicator;
    private UserViewModel userViewModel;
    private User user1;

    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Log.d(TAG, "onCreate: Initialisation de l'activité");

        initViews();

        setupListeners();
    }

    private void initViews() {
        Log.d(TAG, "initViews: Initialisation des vues");

        nameLayout = findViewById(R.id.nameLayout);
        emailLayout = findViewById(R.id.emailLayout);
        phoneLayout = findViewById(R.id.phoneLayout);
        passwordLayout = findViewById(R.id.passwordLayout);

        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        signUpButton = findViewById(R.id.signUpButton);
        signInPrompt = findViewById(R.id.signInPrompt);
        progressIndicator = findViewById(R.id.progressIndicator);
    }

    private void setupViewModel() {
        Log.d(TAG, "setupViewModel: Initialisation du ViewModel");

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.getUser().observe(this, user -> {
            hideLoading();
            if (user != null) {
                Log.d(TAG, "setupViewModel: Utilisateur enregistré, redirection immédiate vers la page principale");
                navigateToMain();
            }
        });

        userViewModel.getError().observe(this, error -> {
            hideLoading();
            if (error != null && !error.isEmpty()) {
                Log.e(TAG, "setupViewModel: Erreur lors de l'inscription - " + error);
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupListeners() {
        Log.d(TAG, "setupListeners: Initialisation des listeners");

        signUpButton.setOnClickListener(v -> attemptSignUp());
        signInPrompt.setOnClickListener(v -> {
            Log.d(TAG, "setupListeners: Redirection vers la page de connexion");
            finish();
        });
    }

    private void attemptSignUp() {
        Log.d(TAG, "attemptSignUp: Tentative d'inscription");

        // Reset des erreurs
        nameLayout.setError(null);
        emailLayout.setError(null);
        phoneLayout.setError(null);
        passwordLayout.setError(null);

        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim(); // Mot de passe statique

        boolean isValid = validateInputs(name, email, phone, password);

        if (isValid) {
            Log.d(TAG, "attemptSignUp: Formulaire valide, envoi à l'API");

            showLoading();

            User newUser = new User();
            newUser.setNom(name);
            newUser.setEmail(email);
            newUser.setNumero(phone);
            newUser.setHashedPassword(password);
            Log.d(TAG, "Mot de passe : " + newUser.getHashedPassword());
            AuthRequest authRequest=new AuthRequest();
            authRequest.setPassword(password);
            authRequest.setEmail(email);
            authRequest.setNom(name);
            authRequest.setNumero(phone);

            createUser(newUser);

        } else {
            Log.d(TAG, "attemptSignUp: Formulaire invalide");
        }
    }

    private boolean validateInputs(String name, String email, String phone, String password) {
        Log.d(TAG, "validateInputs: Validation des entrées");

        boolean isValid = true;

        if (TextUtils.isEmpty(name)) {
            Log.d(TAG, "validateInputs: Nom vide");
            nameLayout.setError("Le nom est requis");
            isValid = false;
        }

        if (TextUtils.isEmpty(email)) {
            Log.d(TAG, "validateInputs: Email vide");
            emailLayout.setError("L'email est requis");
            isValid = false;
        } else if (!email.contains("@")) {
            Log.d(TAG, "validateInputs: Email invalide");
            emailLayout.setError("Email invalide");
            isValid = false;
        }

        if (TextUtils.isEmpty(phone)) {
            Log.d(TAG, "validateInputs: Numéro de téléphone vide");
            phoneLayout.setError("Le numéro de téléphone est requis");
            isValid = false;
        } else if (phone.length() < 10) {
            Log.d(TAG, "validateInputs: Numéro de téléphone invalide");
            phoneLayout.setError("Numéro de téléphone invalide");
            isValid = false;
        }

        if (TextUtils.isEmpty(password)) {
            Log.d(TAG, "validateInputs: Mot de passe vide");
            passwordLayout.setError("Le mot de passe est requis");
            isValid = false;
        } else if (password.length() < 6) {
            Log.d(TAG, "validateInputs: Mot de passe trop court");
            passwordLayout.setError("Le mot de passe doit contenir au moins 6 caractères");
            isValid = false;
        }

        return isValid;
    }

    private void showLoading() {
        Log.d(TAG, "showLoading: Affichage de l'indicateur de progression");
        progressIndicator.setVisibility(View.VISIBLE);
        signUpButton.setEnabled(false);
    }

    private void hideLoading() {
        Log.d(TAG, "hideLoading: Masquage de l'indicateur de progression");
        progressIndicator.setVisibility(View.GONE);
        signUpButton.setEnabled(true);
    }

    private void navigateToMain() {
        Log.d(TAG, "navigateToMain: Navigation vers l'activité principale");

        // Créer et démarrer l'intent vers MainActivity
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();

    }

    public void createUser(User user) {
        RetrofitClient.getInstance()
                .getUserApiService()
                .register(user)
                .enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Réponse réussie : L'utilisateur est enregistré
                            AuthResponse authResponse = response.body();
                            Log.d("Register", "Utilisateur créé avec succès: " + authResponse.getUser().getNom());
                            SessionManager.getInstance().setCurrentUser(authResponse.getUser());
                            // Afficher un message à l'utilisateur
                            Toast.makeText(getApplicationContext(),
                                    "Inscription réussie, bienvenue " + authResponse.getUser().getNom(),
                                    Toast.LENGTH_LONG).show();
                                    user1=authResponse.getUser();
                                    startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
                            // Redirection vers une autre activité si nécessaire


                            finish();
                        } else {
                            // Gestion des erreurs côté serveur
                            Log.e("Register", "Erreur d'enregistrement: Code " + response.code());
                            Toast.makeText(getApplicationContext(),
                                    "Erreur lors de l'inscription. Veuillez réessayer.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        // Gestion des erreurs réseau ou exceptions
                        Log.e("Register", "Échec de la requête: " + t.getMessage(), t);
                        Toast.makeText(getApplicationContext(),
                                "Erreur réseau. Vérifiez votre connexion.",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}

