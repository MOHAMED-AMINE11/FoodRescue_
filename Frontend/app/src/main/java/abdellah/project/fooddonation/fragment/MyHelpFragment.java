package abdellah.project.fooddonation.fragment;

import static abdellah.project.fooddonation.client.RetrofitClient.getApi;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.ArrayList;
import abdellah.project.fooddonation.R;
import abdellah.project.fooddonation.SessionManager;
import abdellah.project.fooddonation.adapters.HelpAdapter;
import abdellah.project.fooddonation.entity.BoiteMail;
import abdellah.project.fooddonation.mvvm.BoiteMainViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyHelpFragment extends Fragment implements HelpAdapter.OnHelpClickListener {

    RecyclerView recyclerView;
    HelpAdapter helpAdapter;
    BoiteMainViewModel boiteMainViewModel;
    View rootViw;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boiteMainViewModel = new ViewModelProvider(this).get(BoiteMainViewModel.class);
        // Appel pour récupérer les données du bénéficiaire
        boiteMainViewModel.fetchBoiteMailBenificier(SessionManager.getInstance().getCurrentUser().getId());  // Exemple de fetch pour un utilisateur
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootViw = inflater.inflate(R.layout.fragment_my_help, container, false);

        // Initialisation du RecyclerView et de l'adaptateur
        recyclerView = rootViw.findViewById(R.id.myhelpview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Création d'un adaptateur avec une liste initiale vide
        helpAdapter = new HelpAdapter(new ArrayList<BoiteMail>(), this);
        recyclerView.setAdapter(helpAdapter);

        // Observer les données du ViewModel pour mettre à jour la liste
        observeViewModel();

        return rootViw;
    }

    private void observeViewModel() {
        // Observer les données de boîte mail
        boiteMainViewModel.getBoiteMailBinificier().observe(getViewLifecycleOwner(), boiteMails -> {
            if (boiteMails != null && !boiteMails.isEmpty()) {
                // Mise à jour de l'adaptateur avec les nouvelles données
                helpAdapter.updateList(boiteMails);
            } else {
                // Gérer le cas où la liste est vide ou nulle
                Toast.makeText(getContext(), "Aucune aide disponible pour le moment.", Toast.LENGTH_SHORT).show();
            }
        });

        // Observer les messages d'erreur
        boiteMainViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            // Gérer les erreurs (par exemple, afficher un Toast avec le message d'erreur)
            if (errorMessage != null) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onTakeKnowClick(BoiteMail boiteMail) {
        // Vérifier si boiteMail et sa Donation ne sont pas nuls
        if (boiteMail != null && boiteMail.getDonation() != null) {
            Bundle bundle = new Bundle();
            bundle.putDouble("latitude", boiteMail.getDonation().getLatitude());
            bundle.putDouble("longitude", boiteMail.getDonation().getLongitude());
            bundle.putBoolean("allMap", true);
            Navigation.findNavController(rootViw).navigate(R.id.mapsFragment2, bundle);
        } else {
            Toast.makeText(getContext(), "Information sur la donation manquante", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCellItem(BoiteMail boiteMail) {
        if (boiteMail != null) {
            getApi().cellConfDemande(boiteMail.getIdMail()).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful()) {
                        // Handle the successful response
                        Toast.makeText(getContext(), "Demande confirmée avec succès", Toast.LENGTH_LONG).show();
                    } else {
                        // Handle the error response
                        Toast.makeText(getContext(), "Erreur lors de la confirmation de la demande", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    // Handle failure (network error, etc.)
                    Toast.makeText(getContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "Erreur lors de la confirmation de la demande", Toast.LENGTH_SHORT).show();
        }
    }
}
