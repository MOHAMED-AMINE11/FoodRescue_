package abdellah.project.fooddonation.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import abdellah.project.fooddonation.R;
import abdellah.project.fooddonation.SessionManager;
import abdellah.project.fooddonation.entity.Association;
import abdellah.project.fooddonation.entity.Don;
import abdellah.project.fooddonation.mvvm.DonViewModel;


public class DonFormFragment extends Fragment {
    private DonViewModel viewModel;
    private Long associationId;
    private TextInputEditText etTypeAliment, etQuantite, etDatePeremption, etDescription;
    private AutoCompleteTextView spinnerUnite;
    private MaterialButton btnSoumettreDon, btnAddPhoto;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(DonViewModel.class);

        if (getArguments() != null) {
            associationId = getArguments().getLong("association_id");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_don_form, container, false);

        initializeViews(view);
        setupUniteDropdown();
        setupDatePicker();
        setupObservers();
        setupSubmitButton();
        setupPhotoButton();

        return view;
    }

    private void initializeViews(View view) {
        etTypeAliment = view.findViewById(R.id.etTypeAliment);
        etQuantite = view.findViewById(R.id.etQuantite);
        etDatePeremption = view.findViewById(R.id.etDatePeremption);
        etDescription = view.findViewById(R.id.etDescription);
        spinnerUnite = view.findViewById(R.id.spinnerUnite);
        btnSoumettreDon = view.findViewById(R.id.btnSoumettreDon);
        btnAddPhoto = view.findViewById(R.id.btnAddPhoto);
    }

    private void setupUniteDropdown() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.unites_mesure,
                R.layout.dropdown_menu_item
        );
        spinnerUnite.setAdapter(adapter);

        // Définir une valeur par défaut
        if (adapter.getCount() > 0) {
            spinnerUnite.setText(adapter.getItem(0).toString(), false);
        }
    }

    private void setupDatePicker() {
        etDatePeremption.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    R.style.MaterialCalendarTheme,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        showTimePicker(calendar); // Ajouter un sélecteur d'heure après avoir sélectionné la date
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });
    }

    private void showTimePicker(Calendar calendar) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, hourOfDay, minute1) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute1);
                    // Mise à jour du champ avec la date et l'heure sélectionnées
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
                    etDatePeremption.setText(sdf.format(calendar.getTime()));
                },
                hour,
                minute,
                false // Format 12 heures avec AM/PM
        );
        timePickerDialog.show();
    }

    private void setupPhotoButton() {
        btnAddPhoto.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Fonctionnalité à venir", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupObservers() {
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            btnSoumettreDon.setEnabled(!isLoading);
            if (isLoading) {
                btnSoumettreDon.setIcon(requireContext().getDrawable(R.drawable.ic_success));
            } else {
                btnSoumettreDon.setIcon(null);
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
            }
        });

        viewModel.getDonLiveData().observe(getViewLifecycleOwner(), don -> {
            if (don != null) {
                Bundle args = new Bundle();
                args.putSerializable("don_effectue", don);
             Navigation.findNavController(requireView())
                        .navigate(R.id.action_donFormFragment_to_confirmationFragment, args);
            }
        });
    }

    private void setupSubmitButton() {
        btnSoumettreDon.setOnClickListener(v -> {
            if (validateForm()) {
                Don don = createDonFromForm();
                viewModel.creerDon(don);
            }
        });
    }

    private boolean validateForm() {
        boolean isValid = true;

        if (etTypeAliment.getText().toString().trim().isEmpty()) {
            etTypeAliment.setError("Ce champ est requis");
            isValid = false;
        }

        if (etQuantite.getText().toString().trim().isEmpty()) {
            etQuantite.setError("Ce champ est requis");
            isValid = false;
        }

        if (etDatePeremption.getText().toString().trim().isEmpty()) {
            etDatePeremption.setError("Ce champ est requis");
            isValid = false;
        }

        if (spinnerUnite.getText().toString().trim().isEmpty()) {
            spinnerUnite.setError("Ce champ est requis");
            isValid = false;
        }

        return isValid;
    }

    private Don createDonFromForm() {
        Don don = new Don();
        Long id= (long) SessionManager.getInstance().getCurrentUser().getId();
        don.setIdUtilisateur(id);
        don.setStatut("en_attente");

        // Créer l'objet Association avec l'ID de l'association
        Association association = new Association();
        association.setIdAssociation(associationId.intValue());

        don.setAssociation(association); // Affecter l'objet Association au Don

        don.setTypeAliment(etTypeAliment.getText().toString().trim());
        don.setQuantite(Double.parseDouble(etQuantite.getText().toString().trim()));
        don.setUnite(spinnerUnite.getText().toString());
        don.setDescription(etDescription.getText().toString().trim());

        // Récupérer la date sous forme de chaîne et la formater avant de l'affecter
        don.setDatePeremption(etDatePeremption.getText().toString().trim());

        return don;
    }
}
