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
import abdellah.project.fooddonation.R;
import abdellah.project.fooddonation.SessionManager;
import abdellah.project.fooddonation.databinding.FragmentUserProfileBinding;
import abdellah.project.fooddonation.entity.User;
import abdellah.project.fooddonation.repositories.RepositoryCallback;
import abdellah.project.fooddonation.repositories.UserRepository1;


public class UserProfileFragment extends Fragment {

    private FragmentUserProfileBinding binding;
    private UserRepository1 userRepository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = UserRepository1.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String userEmail = SessionManager.getInstance().getCurrentUser().getEmail();

        loadUserData(userEmail);
        setupClickListeners();
    }
    private void loadUserData(String email) {
        if (email != null && !email.isEmpty()) {
            userRepository.getUserByEmail(email, new RepositoryCallback<User>() {
                @Override
                public void onSuccess(User user) {
                    updateUI(user);
                }

                @Override
                public void onError(String error) {
                    showError(error);
                }
            });
        } else {
            showError("Email is missing.");
        }
    }
    private void setupClickListeners() {
        binding.editProfileButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("Email",binding.userEmail.getText().toString());
            bundle.putString("name",binding.userName.getText().toString());
            bundle.putString("phone",binding.userPhone.getText().toString());
            // Naviguer vers le fragment EditProfileFragment
           Navigation.findNavController(v).navigate(R.id.action_userProfileFragment_to_editProfileFragment,bundle);
        });
    }
    private void updateUI(User user) {
        if (user != null) {
            binding.userName.setText(user.getNom());
            binding.userEmail.setText(user.getEmail());
            binding.userPhone.setText(user.getNumero());
        }
    }
    private void showError(String error) {
        if (error != null && !error.isEmpty()) {
            Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
