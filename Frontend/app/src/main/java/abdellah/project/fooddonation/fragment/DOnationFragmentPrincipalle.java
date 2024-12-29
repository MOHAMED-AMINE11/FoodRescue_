package abdellah.project.fooddonation.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import java.util.List;
import abdellah.project.fooddonation.R;
import abdellah.project.fooddonation.adapters.DonationAdapter;
import abdellah.project.fooddonation.entity.Donations;
import abdellah.project.fooddonation.mvvm.DonationsViewModel;

public class DOnationFragmentPrincipalle extends Fragment implements DonationAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private DonationAdapter adapter;
    private DonationsViewModel donationsViewModel;
    private List<Donations> donations = new ArrayList<>();
    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialisation du ViewModel
        donationsViewModel = new ViewModelProvider(this).get(DonationsViewModel.class);

        // Observer les données des donations

        // Lancer la récupération des données
        donationsViewModel.fetchDonations();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate le layout du fragment
        rootView = inflater.inflate(R.layout.fragment_d_onation_principalle, container, false);

        // Initialisation du RecyclerView
        recyclerView = rootView.findViewById(R.id.secondRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialisation de l'adapter avec une connexion à l'interface
        adapter = new DonationAdapter(getContext(), donations, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);  // Optimisation des performances
        observeDonations();
        donationsViewModel.fetchDonations();
        return rootView;
    }

    @Override
    public void onItemClick(Donations donation) {
        Toast.makeText(getContext(), "Item clicked: " + donation.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onButtonClick(Donations donation) {
        if (rootView == null || donation == null) return; // Vérification que la vue et la donation existent

        // Préparation des données pour la navigation
        Bundle bundle = new Bundle();

        bundle.putSerializable("donation", donation);
        bundle.putInt("id_donner",donation.getIdDonner());
        bundle.putString("image", donation.getFoodImagePreview());
        bundle.putLong("donationId", donation.getIdDonation());
        bundle.putString("donationTitle", donation.getTitle());
        bundle.putString("donationDescription", donation.getDescription());
        bundle.putString("donationQuantity", donation.getQuantity() + donation.getUniter());
        bundle.putString("date", donation.getDate().toString());
        bundle.putString("donationQuality", donation.getQualityDropdown());
        bundle.putDouble("latitude", donation.getLatitude());
        bundle.putDouble("longitude", donation.getLongitude());
        bundle.putBoolean("allMap", true);



        // Navigation
        Navigation.findNavController(rootView).navigate(R.id.action_DOnationFragmentPrincipalle_to_infoDonation_,bundle);
    }
    private void observeDonations() {
        // Observe donation data from ViewModel
        donationsViewModel.getDonations().observe(getViewLifecycleOwner(), donationList -> {
            if (donationList == null || donationList.isEmpty()) {
                Log.d("HomePageFragment", "Donation list is empty or null.");
            } else {
                donations.clear();
                donations.addAll(donationList);
                adapter.notifyDataSetChanged();
                Log.d("HomePageFragment", "Donations received: " + donations.size() + " items.");
            }
        });

        // Observe errors from ViewModel
        donationsViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
                Log.d("HomePageFragment", "Error: " + errorMessage);
            }
        });
    }

}
