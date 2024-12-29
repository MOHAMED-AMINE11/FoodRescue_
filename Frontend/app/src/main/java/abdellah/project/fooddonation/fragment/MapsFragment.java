package abdellah.project.fooddonation.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import abdellah.project.fooddonation.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapsFragment extends Fragment {

    private double donationLongitude;
    private double donationLatitude;
    private Boolean showMap = false;
    private FusedLocationProviderClient fusedLocationClient;

    public double getDonationLongitude() {
        return donationLongitude;
    }

    public void setDonationLongitude(double donationLongitude) {
        this.donationLongitude = donationLongitude;
    }

    public double getDonationLatitude() {
        return donationLatitude;
    }

    public void setDonationLatitude(double donationLatitude) {
        this.donationLatitude = donationLatitude;
    }

    public Boolean getShowMap() {
        return showMap;
    }

    public void setShowMap(Boolean showMap) {
        this.showMap = showMap;
    }

    public MapsFragment() {
        // Required empty public constructor
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            initialisation_donation_position();

            LatLng donationLocation = new LatLng(donationLatitude, donationLongitude);
            googleMap.addMarker(new MarkerOptions().position(donationLocation).title("Donation Location"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(donationLocation, 15));

            if (showMap) {
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                        if (location != null) {
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            googleMap.addMarker(new MarkerOptions().position(currentLocation).title("Your Location"));
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12));

                            // Tracé de l'itinéraire entre l'emplacement actuel et la donation
                            drawRoute(googleMap, currentLocation, donationLocation);
                        }
                    });
                } else {
                    // Demande d'autorisation si non accordée
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private void initialisation_donation_position() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            donationLatitude = arguments.getDouble("latitude", 0.0);
            donationLongitude = arguments.getDouble("longitude", 0.0);
            showMap = arguments.getBoolean("allMap", false);
        }
    }

    private void drawRoute(GoogleMap googleMap, LatLng start, LatLng end) {
        String apiKey = "AIzaSyBtAubRGSlTZanGLTPT3JrKWsRCFAXZzrE"; // Remplacez par votre clé API Google Maps
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" +
                start.latitude + "," + start.longitude + "&destination=" +
                end.latitude + "," + end.longitude + "&key=" + apiKey;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("MapsFragment", "Erreur lors de la récupération des directions", e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject json = new JSONObject(responseData);
                        JSONArray routes = json.getJSONArray("routes");
                        if (routes.length() > 0) {
                            JSONArray legs = routes.getJSONObject(0).getJSONArray("legs");
                            JSONArray steps = legs.getJSONObject(0).getJSONArray("steps");

                            List<LatLng> path = new ArrayList<>();
                            for (int i = 0; i < steps.length(); i++) {
                                JSONObject step = steps.getJSONObject(i);
                                String polyline = step.getJSONObject("polyline").getString("points");
                                path.addAll(decodePolyline(polyline));
                            }

                            requireActivity().runOnUiThread(() -> {
                                PolylineOptions polylineOptions = new PolylineOptions()
                                        .addAll(path)
                                        .width(10)
                                        .color(Color.BLUE);
                                googleMap.addPolyline(polylineOptions);
                            });
                        }
                    } catch (Exception e) {
                        Log.e("MapsFragment", "Erreur lors de l'analyse des directions", e);
                    }
                }
            }
        });
    }

    private List<LatLng> decodePolyline(String encoded) {
        List<LatLng> polyline = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            polyline.add(new LatLng((lat / 1E5), (lng / 1E5)));
        }
        return polyline;
    }
}
