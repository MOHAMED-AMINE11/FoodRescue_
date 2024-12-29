package abdellah.project.fooddonation.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import abdellah.project.fooddonation.R;
import abdellah.project.fooddonation.entity.Don;


public class ConfirmationFragment extends Fragment {
    private Button btnRetourAccueil;
    private Button btnVoirMesDons;
    private Don donEffectue;
    private TextView tvTypeAliment;
    private TextView tvQuantite;
    private TextView tvDateDon;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            donEffectue = (Don) getArguments().getSerializable("don_effectue");
            Log.d("ConfirmationFragment", donEffectue != null ?
                    "Don reçu: " + donEffectue.getTypeAliment() :
                    "donEffectue est null");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirmation, container, false);
        initializeViews(view);
        setupListeners();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        afficherDetailsDon();
    }

    private void initializeViews(View view) {
        btnRetourAccueil = view.findViewById(R.id.btnRetourAccueil);
        btnVoirMesDons = view.findViewById(R.id.btnVoirMesDons);
        tvTypeAliment = view.findViewById(R.id.tvTypeAliment);
        tvQuantite = view.findViewById(R.id.tvQuantite);
        tvDateDon = view.findViewById(R.id.tvDateDon);
    }

    private void setupListeners() {
        btnRetourAccueil.setOnClickListener(v -> {
            Navigation.findNavController(v)
                   .navigate(R.id.action_confirmationFragment_to_associationListFragment);
        });

        btnVoirMesDons.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_confirmationFragment_to_associationListFragment);
        });
    }

    private void afficherDetailsDon() {
        try {
            if (donEffectue != null && tvTypeAliment != null &&
                    tvQuantite != null && tvDateDon != null) {

                tvTypeAliment.setText(donEffectue.getTypeAliment());
                tvQuantite.setText(String.format("%s %s",
                        donEffectue.getQuantite().toString(),
                        donEffectue.getUnite()));

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm",
                        Locale.getDefault());
                tvDateDon.setText(sdf.format(new Date()));
            } else {
                Log.e("ConfirmationFragment", "Une ou plusieurs vues sont null");
            }
        } catch (Exception e) {
            Log.e("ConfirmationFragment", "Erreur lors de l'affichage des détails du don", e);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(false);
            }
        }
    }
}