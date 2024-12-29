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
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import abdellah.project.fooddonation.R;
import abdellah.project.fooddonation.adapters.AssociationAdapter;
import abdellah.project.fooddonation.adapters.DonationAdapter;
import abdellah.project.fooddonation.entity.Association;
import abdellah.project.fooddonation.entity.Donations;
import abdellah.project.fooddonation.mvvm.AssociationViewModel;
import abdellah.project.fooddonation.mvvm.DonationsViewModel;

public class HomePageFragment extends Fragment implements DonationAdapter.OnItemClickListener, AssociationAdapter.OnItemClick {

    private RecyclerView recyclerViewAssociations;
    private RecyclerView recyclerViewDonations;
    private AssociationAdapter associationAdapter;
    private DonationAdapter donationAdapter;
    private List<Association> associationList = new ArrayList<>();
    private List<Donations> donationsList = new ArrayList<>();
    private Button helpButton;
    private DonationsViewModel donationsViewModel;
    private View rootView;
    private AssociationViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize mock data for associations

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home_page, container, false);

        // Initialize UI components
        helpButton = rootView.findViewById(R.id.helpButton);
        recyclerViewAssociations = rootView.findViewById(R.id.firstRecyclerView);
        recyclerViewDonations = rootView.findViewById(R.id.secondRecyclerView);

        // Set up "Help" button navigation
        helpButton.setOnClickListener(v ->
                Navigation.findNavController(rootView).navigate(R.id.action_homePageFragment_to_choiseFragement)
        );

        // Set up associations RecyclerView
        recyclerViewAssociations.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        associationAdapter = new AssociationAdapter(getContext(), associationList,this);

        recyclerViewAssociations.setAdapter(associationAdapter);

        // Set up donations RecyclerView
        recyclerViewDonations.setLayoutManager(new LinearLayoutManager(getContext()));
        donationAdapter = new DonationAdapter(getContext(), donationsList,this);
        recyclerViewDonations.setAdapter(donationAdapter);

        // Initialize ViewModel and observe LiveData
        donationsViewModel = new ViewModelProvider(this).get(DonationsViewModel.class);
        viewModel=new ViewModelProvider(this).get(AssociationViewModel.class);
        observeDonations();
        donationsViewModel.fetchDonations();
        observationAssociation();
        viewModel.fetchAssociation();


        return rootView;
    }

    private void observeDonations() {
        // Observe donation data from ViewModel
        donationsViewModel.getDonations().observe(getViewLifecycleOwner(), donationList -> {
            if (donationList == null || donationList.isEmpty()) {
                Log.d("HomePageFragment", "Donation list is empty or null.");
            } else {
                donationsList.clear();
                donationsList.addAll(donationList);
                donationAdapter.notifyDataSetChanged();
                Log.d("HomePageFragment", "Donations received: " + donationsList.size() + " items.");
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
    private void observationAssociation(){
        viewModel.getAssociations().observe(getViewLifecycleOwner(),associationList1->{
            if (associationList1 == null || associationList1.isEmpty()) {
                Log.d("HomePageFragment", "Donation list is empty or null.");
            } else {
                associationList.clear();
                associationList.addAll(associationList1);
                associationAdapter.notifyDataSetChanged();
                Log.d("HomePageFragment", "Donations received: " + donationsList.size() + " items.");
            }
        });
    }

    // Mock data for associations


    @Override
    public void onItemClick(Donations donation) {

    }

    @Override
    public void onButtonClick(Donations donation) {

        if (rootView == null) return;

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
        // Navigation vers l'autre fragment avec les arguments
        Navigation.findNavController(rootView).navigate(R.id.action_homePageFragment_to_infoDonation_,bundle);
    }


    @Override
    public void ItemClick(Association association) {
        Bundle bundle =new Bundle();

        bundle.putLong("association_id", association.getIdAssociation());
        Navigation.findNavController(rootView).navigate(R.id.donFormFragment,bundle);
    }
}


