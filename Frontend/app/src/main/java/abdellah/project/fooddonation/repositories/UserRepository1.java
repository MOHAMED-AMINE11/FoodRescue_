package abdellah.project.fooddonation.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import abdellah.project.fooddonation.api.Api;

import abdellah.project.fooddonation.client.RetrofitClient;
import abdellah.project.fooddonation.entity.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository1 {
    private static UserRepository1 instance;
    private final Api userApiService;
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    private UserRepository1() {
        userApiService = RetrofitClient.getApi();
    }

    public static synchronized UserRepository1 getInstance() {
        if (instance == null) {
            instance = new UserRepository1();
        }
        return instance;
    }

    public MutableLiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void getUserByEmail(String email, RepositoryCallback<User> callback) {
        userApiService.getUserByEmail(email).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error fetching user data");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
                Log.e("UserRepository", "Error fetching user", t);
            }
        });
    }

    public void updateUser(int id, User user, RepositoryCallback<User> callback) {
        userApiService.updateUser(id, user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error updating user data");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
                Log.e("UserRepository", "Error updating user", t);
            }
        });
    }
}
