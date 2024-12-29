package abdellah.project.fooddonation.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import abdellah.project.fooddonation.R;
import abdellah.project.fooddonation.SessionManager;
import abdellah.project.fooddonation.adapters.MydonationAdapter;
import abdellah.project.fooddonation.entity.Donations;
import abdellah.project.fooddonation.mvvm.DonationsViewModel;

public class DonateFragment extends Fragment implements MydonationAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private MydonationAdapter adapter;
    private List<Donations> donations = new ArrayList<>();
    private DonationsViewModel donationsViewModel;
    private  View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Optionnel : Ajoutez des arguments ou initialisez des données ici si nécessaire
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Charger le layout du fragment
        rootView = inflater.inflate(R.layout.fragment_donate, container, false);

        // Initialisation du RecyclerView
        recyclerView = rootView.findViewById(R.id.notifRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Remplir la liste des donations (données factices pour tester)
        observation();

        // Initialiser et attacher l'adaptateur
        adapter = new MydonationAdapter(getContext(), donations,this);
        recyclerView.setAdapter(adapter);

        return rootView;
    }


    public void observation(){
        donationsViewModel = new ViewModelProvider(this).get(DonationsViewModel.class);

        // Observer les données des donations
        donationsViewModel.getDonationsByUserId().observe(getViewLifecycleOwner(), donationList -> {
            if (donationList != null && !donationList.isEmpty()) {
                donations.clear();
                donations.addAll(donationList);
                adapter.notifyDataSetChanged();
                Log.d("DonationFragment", "Données des donations reçues : " + donations.size() + " items.");
            } else {
                Log.d("DonationFragment", "Liste des donations est vide ou null.");
                Toast.makeText(getContext(), "Aucune donation disponible.", Toast.LENGTH_SHORT).show();
            }
        });

        donationsViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(requireContext(), "Erreur: " + errorMessage, Toast.LENGTH_LONG).show();
                Log.d("DonationFragment", "Erreur : " + errorMessage);
            }
        });

        // Lancer la récupération des données
        donationsViewModel.fetchDonationsByUserId(SessionManager.getInstance().getCurrentUser().getId());
    }



    @Override
    public void onItemClick(Donations donation) {
        Bundle bundle=new Bundle();
        bundle.putSerializable("donnation",donation);
        Log.d("fffffffffffffffffffffff",donation.getIdDonation()+"");
        Navigation.findNavController(rootView).navigate(R.id.action_donateFragment_to_fragment_recieve_2,bundle);

    }
}
