package abdellah.project.fooddonation.repositories;

import java.util.List;

import abdellah.project.fooddonation.api.Api;
import abdellah.project.fooddonation.client.RetrofitClient;
import abdellah.project.fooddonation.entity.Association;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// repositories/AssociationRepository.java
public class AssociationRepository {
    private final Api apiService;

    public interface AssociationsCallback {
        void onSuccess(List<Association> associations);
        void onError(String message);
    }

    public AssociationRepository() {
        apiService = RetrofitClient.getApi();
    }

    public void getAssociations(AssociationsCallback callback) {
        apiService.getAssociations().enqueue(new Callback<List<Association>>() {
            @Override
            public void onResponse(Call<List<Association>> call, Response<List<Association>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Erreur lors de la récupération des associations");
                }
            }

            @Override
            public void onFailure(Call<List<Association>> call, Throwable t) {
                callback.onError("Erreur réseau: " + t.getMessage());
            }
        });
    }
}


