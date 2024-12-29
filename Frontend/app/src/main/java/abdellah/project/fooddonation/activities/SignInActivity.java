package abdellah.project.fooddonation.activities;

import static android.content.ContentValues.TAG;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import abdellah.project.fooddonation.MainActivity;
import abdellah.project.fooddonation.R;
import abdellah.project.fooddonation.api.RetrofitClient;
import abdellah.project.fooddonation.entity.AuthRequest;
import abdellah.project.fooddonation.entity.AuthResponse;
import abdellah.project.fooddonation.entity.User;
import abdellah.project.fooddonation.mvvm.UserViewModel;
import abdellah.project.fooddonation.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {
    private TextInputLayout emailLayout, passwordLayout;
    private TextInputEditText emailEditText, passwordEditText;
    private MaterialButton signInButton;
    private View signUpPrompt;
    private User user1=new User();
    private AuthResponse resp;
    private UserViewModel authViewModel;
    private CircularProgressIndicator progressIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initializeViews();

        setupViewModel();
        setupClickListeners();


        Log.d("d",authViewModel.getUser().toString());

    }

    private void initializeViews() {
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signInButton = findViewById(R.id.signInButton);
        signUpPrompt = findViewById(R.id.signUpPrompt);
        progressIndicator = findViewById(R.id.progressBar);
    }

    private void setupViewModel() {
        authViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        authViewModel.getUser().observe(this,user -> {
            user1=user;
            if(user!=null){

                navigateToMain();
            }
        });
        authViewModel.getAuthState().observe(this, authResponse -> {
            hideLoading();
            if (authResponse != null) {
               System.out.println(user1);
                Log.d("nav",authResponse.toString());
                navigateToMain();
            }
        });

        authViewModel.getError().observe(this, error -> {
            hideLoading();
        });
    }

    private void setupClickListeners() {
        signInButton.setOnClickListener(v -> attemptSignIn());

        signUpPrompt.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    private void attemptSignIn() {
        emailLayout.setError(null);
        passwordLayout.setError(null);

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            passwordLayout.setError(getString(R.string.error_field_required));
            focusView = passwordEditText;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            passwordLayout.setError(getString(R.string.error_invalid_password));
            focusView = passwordEditText;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            emailLayout.setError(getString(R.string.error_field_required));
            focusView = emailEditText;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailLayout.setError(getString(R.string.error_invalid_email));
            focusView = emailEditText;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showLoading();
            Log.d("fcuk","fuck");

            login(new AuthRequest(email,password));

        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    private void showLoading() {
        if (progressIndicator != null) {
            progressIndicator.setVisibility(View.VISIBLE);
        }
        signInButton.setEnabled(false);
    }

    private void hideLoading() {
        if (progressIndicator != null) {
            progressIndicator.setVisibility(View.GONE);
        }
        signInButton.setEnabled(true);
    }

    private void navigateToMain() {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void login(AuthRequest res) {
        Log.d(TAG, "login: Tentative de connexion...");

        // Appel de l'API de connexion avec Retrofit
        RetrofitClient.getInstance()
                .getUserApiService()
                .login(res)
                .enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Connexion réussie
                            AuthResponse authResponse = response.body();
                            Log.d("user",authResponse.getUser().toString());
                            // Afficher un message à l'utilisateur
                            Toast.makeText(SignInActivity.this, "Connexion réussie, Bienvenue " + authResponse.getUser(), Toast.LENGTH_LONG).show();
                            SessionManager.getInstance().setCurrentUser(authResponse.getUser());

                            startActivity(new Intent(SignInActivity.this,MainActivity.class));

                        } else {
                            // Gestion des erreurs de l'API
                            Log.e(TAG, "login: Erreur de connexion - Code: " + response.code());
                            Toast.makeText(SignInActivity.this, "Erreur de connexion. Vérifiez vos identifiants.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        // Gestion des erreurs de réseau ou autres exceptions
                        Log.e(TAG, "login: Échec de la connexion - " + t.getMessage(), t);
                        Toast.makeText(SignInActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

}

