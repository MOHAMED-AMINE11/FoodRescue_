package abdellah.project.fooddonation.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import abdellah.project.fooddonation.R;
import abdellah.project.fooddonation.SessionManager;
import abdellah.project.fooddonation.client.RetrofitClient;
import abdellah.project.fooddonation.entity.Donations;
import abdellah.project.fooddonation.service.DonationsService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddFoodRescue extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_GALLERY_IMAGE = 2;

    private TextInputEditText descriptionField, foodNameField, quantityField, notesField;
    private AutoCompleteTextView qualityDropdown, unite;
    private RadioGroup foodTypeGroup;
    private MaterialButton submitButton;
    private ImageView foodImagePreview;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private File imageFile;

    private double latitude = 0.0;
    private double longitude = 0.0;
    private View v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        addFragment();
         v= inflater.inflate(R.layout.fragment_add_food_rescue, container, false);
   return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        descriptionField = view.findViewById(R.id.description);
        foodNameField = view.findViewById(R.id.namefiel);
        quantityField = view.findViewById(R.id.quantiterFIeld);

        qualityDropdown = view.findViewById(R.id.quality_dropdown);
        foodTypeGroup = view.findViewById(R.id.food_type_group);
        foodImagePreview = view.findViewById(R.id.food_image_preview);
        submitButton = view.findViewById(R.id.submit_button);
        unite = view.findViewById(R.id.unit_dropdown);

        // Initialiser les qualités
        String[] qualities = {"Excellent", "Très bon", "Bon", "Acceptable"};
        ArrayAdapter<String> qualityAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, qualities);
        qualityDropdown.setAdapter(qualityAdapter);

        // Initialiser les unités
        String[] units = {"kg", "g", "litre", "ml", "m²", "cm²", "pcs", "boîte"};
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, units);
        unite.setAdapter(unitAdapter);

        // Forcer l'affichage des listes déroulantes
        qualityDropdown.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                qualityDropdown.showDropDown();
            }
        });

        unite.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                unite.showDropDown();
            }
        });

        fetchUserLocation();
        foodImagePreview.setOnClickListener(v -> showImagePickerDialog());
        submitButton.setOnClickListener(v -> {displayFormData();
            Navigation.findNavController(v).navigate(R.id.homePageFragment);
        });
    }

    private void fetchUserLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            return;
        }

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), location -> {
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Log.d("AddFoodRescue", "Position utilisateur : Latitude = " + latitude + ", Longitude = " + longitude);
                    } else {
                        Log.d("AddFoodRescue", "Impossible de récupérer la position.");
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchUserLocation();
            } else {
                Log.d("AddFoodRescue", "Permission de localisation refusée.");
            }
        }
    }

    private void showImagePickerDialog() {
        captureImageFromCamera();
    }

    private void captureImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                foodImagePreview.setImageBitmap(imageBitmap);
                imageFile = createTempFileFromBitmap(imageBitmap);
            }
        }
    }

    private String getTextInputValue(TextInputEditText editText) {
        return editText != null && editText.getText() != null ? editText.getText().toString().trim() : "";
    }

    private String getSelectedFoodType() {
        int selectedId = foodTypeGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            MaterialRadioButton selectedButton = getView().findViewById(selectedId);
            return selectedButton != null ? selectedButton.getText().toString() : "";
        }
        return "Non spécifié";
    }

    private void displayFormData() {
        String description = getTextInputValue(descriptionField);
        String foodName = getTextInputValue(foodNameField);
        String quantity = getTextInputValue(quantityField);
        String quality = qualityDropdown.getText().toString();
        String unit = unite.getText().toString();

        if (description.isEmpty() || foodName.isEmpty() || quantity.isEmpty() || quality.isEmpty() || unit.isEmpty()) {
            Toast.makeText(requireContext(), "Veuillez remplir tous les champs obligatoires.", Toast.LENGTH_SHORT).show();
            return;
        }

        String foodType = getSelectedFoodType();

        if (imageFile == null) {
            Toast.makeText(requireContext(), "Veuillez sélectionner une image avant d'envoyer.", Toast.LENGTH_SHORT).show();
            return;
        }

        DonationsService donation = new DonationsService();

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);

        Donations donations = new Donations(
                unit,
                SessionManager.getInstance().getCurrentUser().getId(),
                foodName,
                new Date(),
                foodType,
                quality,
                Float.parseFloat(quantity),
                "fjjsdjf",
                description,
                "abdellah",
                false,
                longitude,
                latitude
        );

        donation.createDonation(donations, imageFile);
        Toast.makeText(requireContext(), "Données envoyées avec succès !", Toast.LENGTH_SHORT).show();
    }

    public void addFragment() {
        MypositionFragment mypositionFragment = new MypositionFragment();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.map_container, mypositionFragment)
                .commit();
    }

    private File createTempFileFromBitmap(Bitmap bitmap) {
        try {
            File tempFile = File.createTempFile("image", ".jpg", requireActivity().getCacheDir());
            FileOutputStream fos = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
