package abdellah.project.fooddonation.service;

import static abdellah.project.fooddonation.client.RetrofitClient.getApi;

import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import abdellah.project.fooddonation.entity.Donations;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DonationsService {

    private static final String TAG = "DonationsService"; // Ajout du tag pour les logs



    public void createDonation(Donations donation, File imageFile) {
        // Sérialiser l'objet Donations en JSON
        Gson gson = new Gson();
        String donationJsonString = gson.toJson(donation);

        // Créer le RequestBody pour l'objet JSON
        RequestBody donationRequestBody = RequestBody.create(
                MediaType.parse("application/json"), donationJsonString
        );

        // Créer le MultipartBody.Part pour le fichier image
        RequestBody imageRequestBody = RequestBody.create(
                MediaType.parse("image/*"), imageFile
        );
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData(
                "image", imageFile.getName(), imageRequestBody
        );

        // Appeler l'API
        Log.d("fofo","ana jay");
        Call<Donations> call = getApi().createDonation(donationRequestBody, imagePart);
        Log.d("ana fofo","ama jite");
        call.enqueue(new Callback<Donations>() {
            @Override
            public void onResponse(Call<Donations> call, Response<Donations> response) {
                if (response.isSuccessful()) {
                    Donations createdDonation = response.body();
                    Log.d("DonationsService", "Donation créée avec succès : " + createdDonation);
                } else {
                    Log.e("DonationsService", "Erreur : " + response.code());
                }

            }

            @Override
            public void onFailure(Call<Donations> call, Throwable t) {
                Log.e("DonationsService", "Échec de la requête : " + t.getMessage());
            }
        });
    }




}
