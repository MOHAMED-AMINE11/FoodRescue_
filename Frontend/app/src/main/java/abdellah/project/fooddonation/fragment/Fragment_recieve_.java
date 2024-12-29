package abdellah.project.fooddonation.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import abdellah.project.fooddonation.R;
import abdellah.project.fooddonation.SessionManager;
import abdellah.project.fooddonation.adapters.AdapterBoiteMail;
import abdellah.project.fooddonation.entity.BoiteMail;
import abdellah.project.fooddonation.entity.Donations;
import abdellah.project.fooddonation.mvvm.BoiteMainViewModel;

public class Fragment_recieve_ extends Fragment implements AdapterBoiteMail.OnItemClickListener {

    private RecyclerView recyclerView;
    private AdapterBoiteMail adapter;
    private BoiteMainViewModel boiteMainViewModel;
    private View rootView;
    private Donations donation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialisation du ViewModel
        boiteMainViewModel = new ViewModelProvider(this).get(BoiteMainViewModel.class);

        // Vérification et récupération de l'objet "donation" depuis les arguments
        if (getArguments() != null) {
            donation = (Donations) getArguments().getSerializable("donnation");
        }

        // Charger les données
        if (donation != null) {
            boiteMainViewModel.fetchBoiteMail(SessionManager.getInstance().getCurrentUser().getId(), donation.getIdDonation()); // Id utilisateur et id donation
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate le layout
        rootView = inflater.inflate(R.layout.fragment_recieve_, container, false);

        // Initialisation du RecyclerView et de l'adaptateur
        recyclerView = rootView.findViewById(R.id.myRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Création d'un adaptateur avec une liste initiale vide
        adapter = new AdapterBoiteMail(new ArrayList<>(), this, getContext());
        recyclerView.setAdapter(adapter);

        // Observer les données du ViewModel
        observeViewModel();

        return rootView;
    }

    private void observeViewModel() {
        // Observer les données de BoiteMail
        boiteMainViewModel.getBoiteMails().observe(getViewLifecycleOwner(), boiteMails -> {
            if (boiteMails != null && !boiteMails.isEmpty()) {
                // Mise à jour des données de l'adaptateur
                adapter.setData(boiteMails);
                adapter.notifyDataSetChanged();
                Log.d("Fragment_recieve_", "Données reçues : " + boiteMails.size());
            } else {
                Toast.makeText(getContext(), "Aucune donnée à afficher", Toast.LENGTH_SHORT).show();
                Log.d("Fragment_recieve_", "La liste des données est vide ou null.");
            }
        });

        // Observer les erreurs
        boiteMainViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(getContext(), "Erreur : " + errorMessage, Toast.LENGTH_SHORT).show();
                Log.d("Fragment_recieve_", "Erreur : " + errorMessage);
            }
        });
    }

    @Override
    public void onConfirmClick(BoiteMail boiteMail) {
        // Action pour confirmer un élément
        boiteMainViewModel.confirme(boiteMail.getIdMail());
        boiteMainViewModel.fetchBoiteMail(SessionManager.getInstance().getCurrentUser().getId(), donation.getIdDonation());
        // Mise à jour de l'adaptateur après confirmation
        Toast.makeText(getContext(), "Confirmé : " + boiteMail.getDonation().getDescription(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancelClick(BoiteMail boiteMail) {
        // Action pour annuler un élément
        boiteMainViewModel.cell(boiteMail.getIdMail());
        boiteMainViewModel.fetchBoiteMail(SessionManager.getInstance().getCurrentUser().getId(), donation.getIdDonation());
        // Mise à jour de l'adaptateur après annulation
        adapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Annulé : " + boiteMail.getDonation().getDescription(), Toast.LENGTH_SHORT).show();
    }
}
