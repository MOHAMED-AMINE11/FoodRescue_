package abdellah.project.fooddonation.mvvm;

import static abdellah.project.fooddonation.client.RetrofitClient.getApi;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import abdellah.project.fooddonation.entity.BoiteMail;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoiteMainViewModel extends ViewModel {

    // LiveData pour contenir la liste des BoiteMail (données principales)
    private final MutableLiveData<List<BoiteMail>> boiteMails = new MutableLiveData<>();
    private final MutableLiveData<List<BoiteMail>> boiteMailBinificier = new MutableLiveData<>();
    // LiveData pour signaler les erreurs
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    // Exposer les LiveData sous forme immuable pour éviter toute modification externe
    public LiveData<List<BoiteMail>> getBoiteMails() {
        return boiteMails;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    // Méthode pour récupérer les données depuis l'API
    public void fetchBoiteMail(int idUser, int idDonation) {
        Call<List<BoiteMail>> call = getApi().getboiteMail(idUser, idDonation);



        call.enqueue(new Callback<List<BoiteMail>>() {
            @Override
            public void onResponse(Call<List<BoiteMail>> call, Response<List<BoiteMail>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Mise à jour des données en cas de succès
                    boiteMails.setValue(response.body());
                    Log.d("fff", "Données reçues : "+boiteMails.getValue().toString());

                } else {
                    // Gestion des erreurs côté serveur (ex : code HTTP 4xx ou 5xx)
                    String error = "Erreur du serveur: " + response.message();
                    Log.d("kkkk","ddddddd");
                    errorMessage.postValue(error);
                }
            }

            @Override
            public void onFailure(Call<List<BoiteMail>> call, Throwable t) {

                String error = "Échec de la connexion : " + t.getMessage();
                Log.d("ddddd","ddddddd");
                errorMessage.postValue(error);
            }
        });
    }


    public void confirme(int idMail) {
        Call<Boolean> call = getApi().confirme(idMail);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null && response.body()) {
                    System.out.println("L'opération de confirmation a réussi : " + response.body());

                    // Supprimer l'élément de la liste locale
                    List<BoiteMail> updatedList = boiteMails.getValue();
                    if (updatedList != null) {
                        updatedList.removeIf(mail -> mail.getIdMail() == idMail);
                        boiteMails.postValue(updatedList); // Mettre à jour les données observées
                    }
                } else {
                    System.err.println("Erreur de réponse du serveur : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                System.err.println("Échec de l'appel API : " + t.getMessage());
            }
        });
    }

    public void cell(int idMail) {
        Call<String> call = getApi().cellDemande(idMail);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    System.out.println("Réponse reçue : " + response.body());

                    // Supprimer l'élément de la liste locale
                    List<BoiteMail> updatedList = boiteMails.getValue();
                    if (updatedList != null) {
                        updatedList.removeIf(mail -> mail.getIdMail() == idMail);
                        boiteMails.postValue(updatedList); // Mettre à jour les données observées
                    }
                } else {
                    System.err.println("Erreur de réponse HTTP : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.err.println("Échec de l'appel : " + t.getMessage());
            }
        });
    }

    public void fetchBoiteMailBenificier(int idBinificier) {
        Call<List<BoiteMail>> call = getApi().getBenificierBoitMail(idBinificier);

        call.enqueue(new Callback<List<BoiteMail>>() {
            @Override
            public void onResponse(Call<List<BoiteMail>> call, Response<List<BoiteMail>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Mise à jour de la liste des boîtes mails des bénéficiaires
                    boiteMailBinificier.setValue(response.body());
                    Log.d("BoiteMail", "Données reçues pour le bénéficiaire : " + boiteMailBinificier.getValue().toString());
                } else {
                    // Gestion des erreurs côté serveur
                    String error = "Erreur du serveur: " + response.message();
                    Log.e("BoiteMail", error);
                    errorMessage.postValue(error);
                }
            }

            @Override
            public void onFailure(Call<List<BoiteMail>> call, Throwable t) {
                // Erreur en cas d'échec de la connexion
                String error = "Échec de la connexion : " + t.getMessage();
                Log.e("BoiteMail", error);
                errorMessage.postValue(error);
            }
        });
    }


    public LiveData<List<BoiteMail>> getBoiteMailBinificier() {
        return boiteMailBinificier;
    }


    public void confirmCellDemande(int idMail) {
        getApi().cellConfDemande(idMail).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {

                } else {
                    errorMessage.postValue("Erreur lors de la confirmation : " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                errorMessage.postValue("Échec de la requête : " + t.getMessage());
            }
        });
    }
}
