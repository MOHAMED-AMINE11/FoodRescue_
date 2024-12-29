package abdellah.project.fooddonation.mvvm;

import static abdellah.project.fooddonation.client.RetrofitClient.getApi;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import abdellah.project.fooddonation.entity.Donations;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DonationsViewModel extends ViewModel {

    private final MutableLiveData<List<Donations>> donationsLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Donations>> donationsByUserIdLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>();

    public LiveData<List<Donations>> getDonations() {
        return donationsLiveData;
    }

    public LiveData<List<Donations>> getDonationsByUserId() {
        return donationsByUserIdLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessageLiveData;
    }

    // Fetcher tous les dons
    public void fetchDonations() {
        Call<List<Donations>> call = getApi().getAllDonations();
        call.enqueue(new Callback<List<Donations>>() {
            @Override
            public void onResponse(Call<List<Donations>> call, Response<List<Donations>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    donationsLiveData.setValue(response.body());
                } else {
                    String errorMessage = "Erreur: " + response.message();
                    errorMessageLiveData.setValue(errorMessage);
                    Log.d("erreur", errorMessage);
                }
            }

            @Override
            public void onFailure(Call<List<Donations>> call, Throwable t) {
                String errorMessage = "Échec: " + t.getMessage();
                errorMessageLiveData.setValue(errorMessage);
                Log.d("erreur", errorMessage);
            }
        });
    }

    // Fetcher les dons par ID utilisateur
    public void fetchDonationsByUserId(int userId) {
        Call<List<Donations>> call = getApi().getDonationByUser(userId); // Utilisez l'API que vous avez configurée
        call.enqueue(new Callback<List<Donations>>() {
            @Override
            public void onResponse(Call<List<Donations>> call, Response<List<Donations>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    donationsByUserIdLiveData.setValue(response.body()); // Mettre à jour les données des dons
                } else {
                    String errorMessage = "Erreur: " + response.message();
                    errorMessageLiveData.setValue(errorMessage); // Gérer les erreurs
                    Log.d("erreur", errorMessage);
                }
            }

            @Override
            public void onFailure(Call<List<Donations>> call, Throwable t) {
                String errorMessage = "Échec: " + t.getMessage();
                errorMessageLiveData.setValue(errorMessage); // Gérer l'échec
                Log.d("erreur", errorMessage);
            }
        });
    }
}
