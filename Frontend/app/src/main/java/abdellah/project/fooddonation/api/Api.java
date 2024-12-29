package abdellah.project.fooddonation.api;

import java.util.List;

import abdellah.project.fooddonation.entity.Association;
import abdellah.project.fooddonation.entity.AuthRequest;
import abdellah.project.fooddonation.entity.AuthResponse;
import abdellah.project.fooddonation.entity.BoiteMail;
import abdellah.project.fooddonation.entity.Don;
import abdellah.project.fooddonation.entity.Donations;
import abdellah.project.fooddonation.entity.User;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Multipart;
import retrofit2.http.Path;

public interface Api {

    // Méthode pour créer une donation avec une image (Multipart)
    @Multipart
    @POST("donations")
    Call<Donations> createDonation(
            @Part("donation") RequestBody donationJson, // JSON représentant les données de la donation
            @Part MultipartBody.Part image // Fichier image associé
    );


    @GET("donations")
    Call<List<Donations>> getAllDonations();


    @GET("associations")
    Call<List<Association>> getAssociations();

    @GET("associations/{id}")
    Call<Association> getAssociation(@Path("id") Long id);

    @POST("dons")
    Call<Don> creerDon(@Body Don don);

    @GET("dons/utilisateur/{userId}")
    Call<List<Don>> getDonsUtilisateur(@Path("userId") Long userId);
    @GET("donations/user/{userId}")
    Call<List<Donations>> getDonationByUser(@Path("userId") int userId);
     @GET("boite_mail/requests-by-donor/{donorId}/{donationId}")
     Call<List<BoiteMail>> getboiteMail(@Path("donorId") int donorId, @Path("donationId") int donationId);
    @POST("boite_mail/requests-by-donor/{idMail}")
    Call<Boolean> confirme(@Path("idMail") int idMail);
    @DELETE("boite_mail/delete/{idMail}")
    Call<String> cellDemande(@Path("idMail") int idMail);

    @GET("users/{id}")
    Call<User> getUserById(@Path("id") int id);

    @GET("users/email/{email}")
    Call<User> getUserByEmail(@Path("email") String email);


    @PUT("users/{id}")
    Call<User> updateUser(@Path("id") int id, @Body User user);

    @GET("boite_mail/beneficiary/active/{idBeneficiary}")
    Call<List<BoiteMail>> getBenificierBoitMail(@Path("idBeneficiary") int idBeneficiary );
    @PUT("boite_mail/cell/{idMail}")
    Call<Boolean> cellConfDemande(@Path("idMail") int idMail);

    @GET("associations")
    Call<List<Association>> getAllAssociation();

    @POST("boite_mail/create")
    Call<BoiteMail> createB(@Body BoiteMail boiteMail);
    @POST("api/auth/login")
    Call<AuthResponse> login(@Body AuthRequest request);

    @POST("api/auth/signup")
    Call<AuthResponse> register(@Body User user);

    @POST("api/auth/logout")
    Call<Void> logout(@Header("Authorization") String token);
}
