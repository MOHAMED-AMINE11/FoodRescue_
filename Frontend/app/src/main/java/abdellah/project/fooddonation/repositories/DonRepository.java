package abdellah.project.fooddonation.repositories;

import java.util.List;

import abdellah.project.fooddonation.api.Api;

import abdellah.project.fooddonation.client.RetrofitClient;
import abdellah.project.fooddonation.entity.Don;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DonRepository {
    private final Api apiService;

    public interface DonCallback {
        void onSuccess(Don don);
        void onError(String message);
    }

    public interface DonsCallback {
        void onSuccess(List<Don> dons);
        void onError(String message);
    }

    public DonRepository() {
        apiService = RetrofitClient.getApi();
    }

    public void creerDon(Don don, DonCallback callback) {
        apiService.creerDon(don).enqueue(new Callback<Don>() {
            @Override
            public void onResponse(Call<Don> call, Response<Don> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Erreur lors de la création du don");
                }
            }

            @Override
            public void onFailure(Call<Don> call, Throwable t) {
                callback.onError("Erreur réseau: " + t.getMessage());
            }
        });
    }

    public void getDonsUtilisateur(Long userId, DonsCallback callback) {
        apiService.getDonsUtilisateur(userId).enqueue(new Callback<List<Don>>() {
            @Override
            public void onResponse(Call<List<Don>> call, Response<List<Don>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Erreur lors de la récupération des dons");
                }
            }

            @Override
            public void onFailure(Call<List<Don>> call, Throwable t) {
                callback.onError("Erreur réseau: " + t.getMessage());
            }
        });
    }
}
