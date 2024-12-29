package abdellah.project.fooddonation.fragment;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import abdellah.project.fooddonation.R;
import abdellah.project.fooddonation.adapters.AssociationAdapter1;
import abdellah.project.fooddonation.entity.Association;
import abdellah.project.fooddonation.mvvm.AssociationViewModel;


public class AssociationListFragment extends Fragment implements AssociationAdapter1.OnAssociationClickListener {
    private RecyclerView recyclerView;
    private AssociationAdapter1 adapter;
    private AssociationViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialisation du ViewModel
        viewModel = new ViewModelProvider(this).get(AssociationViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_association_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewAssociations);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new AssociationAdapter1(this);
        recyclerView.setAdapter(adapter);

        // Observer les changements dans la liste des associations
        viewModel.getAssociations().observe(getViewLifecycleOwner(), associations -> {
            if (associations != null) {
                adapter.setAssociations(associations);
            } else {
                Log.e("AssociationListFragment", "La liste des associations est vide ou n'a pas été chargée !");
            }
        });

        // Ajouter des observers pour loading et erreurs
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            // Gérer l'affichage du chargement
            // Par exemple, afficher/masquer un ProgressBar
        });

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                // Gérer l'affichage des erreurs
                Log.e("AssociationListFragment", "Erreur : " + error);
                // Vous pourriez afficher un message d'erreur à l'utilisateur
            }
        });

        return view;
    }

    @Override
    public void onAssociationClick(Association association) {
        // Navigation vers le formulaire de don
        Bundle bundle = new Bundle();
        bundle.putLong("association_id", association.getIdAssociation());
        Navigation.findNavController(getView())
                .navigate(R.id.action_associationListFragment_to_donFormFragment, bundle);
    }
}