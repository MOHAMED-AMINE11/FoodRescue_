package abdellah.project.fooddonation.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import abdellah.project.fooddonation.databinding.FragmentEditProfileBinding;
import abdellah.project.fooddonation.entity.User;
import abdellah.project.fooddonation.repositories.RepositoryCallback;
import abdellah.project.fooddonation.repositories.UserRepository;
import abdellah.project.fooddonation.repositories.UserRepository1;

public class EditProfileFragment extends Fragment {
    private FragmentEditProfileBinding binding;  // Utiliser FragmentEditProfileBinding
    private UserRepository1 userRepository;
    private User currentUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = UserRepository1.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);  // Lier le XML avec le Binding correct
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle=getArguments();
        String mail= bundle.getString("Email");
        loadCurrentUser(mail);
        setupClickListeners();
    }

    private void loadCurrentUser(String email) {
        showLoading(true);
        userRepository.getUserByEmail(email, new RepositoryCallback<User>() {
            @Override
            public void onSuccess(User user) {
                currentUser = user;
                populateFields(user);
                showLoading(false);
            }

            @Override
            public void onError(String error) {
                showError(error);
                showLoading(false);
            }
        });
    }

    private void populateFields(User user) {
        binding.nameInput.setText(user.getNom());
        binding.emailInput.setText(user.getEmail());
        binding.phoneInput.setText(user.getNumero());
    }

    private void setupClickListeners() {
        binding.saveButton.setOnClickListener(v -> validateAndUpdateProfile());

    }

    private void validateAndUpdateProfile() {
        String name = binding.nameInput.getText().toString().trim();
        String email = binding.emailInput.getText().toString().trim();
        String phone = binding.phoneInput.getText().toString().trim();

        if (validateFields(name, email, phone)) {
            updateProfile(name, email, phone);
        }
    }

    private boolean validateFields(String name, String email, String phone) {
        if (name.isEmpty()) {
            binding.nameInput.setError("Name required");
            return false;
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailInput.setError("Valid email required");
            return false;
        }
        if (phone.isEmpty() || phone.length() < 10) {
            binding.phoneInput.setError("Valid phone number required");
            return false;
        }
        return true;
    }

    private void updateProfile(String name, String email, String phone) {
        showLoading(true);

        User updatedUser = new User();
        updatedUser.setId(currentUser.getId());
        updatedUser.setNom(name);
        updatedUser.setEmail(email);
        updatedUser.setNumero(phone);
        updatedUser.setHashedPassword(currentUser.getHashedPassword());

        userRepository.updateUser(currentUser.getId(), updatedUser, new RepositoryCallback<User>() {
            @Override
            public void onSuccess(User user) {
                showLoading(false);
                showSuccess("Profile updated successfully");
                Navigation.findNavController(getView()).navigateUp();
            }

            @Override
            public void onError(String error) {
                showLoading(false);
                showError(error);
            }
        });
    }

    private void showLoading(boolean isLoading) {
        binding.loadingIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.saveButton.setEnabled(!isLoading);
    }

    private void showError(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }

    private void showSuccess(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
