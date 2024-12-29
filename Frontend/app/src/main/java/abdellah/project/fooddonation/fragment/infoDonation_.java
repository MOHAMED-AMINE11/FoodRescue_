package abdellah.project.fooddonation.fragment;

import static abdellah.project.fooddonation.client.RetrofitClient.getApi;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import abdellah.project.fooddonation.R;
import abdellah.project.fooddonation.SessionManager;
import abdellah.project.fooddonation.adapters.DonationAdapter;
import abdellah.project.fooddonation.entity.BoiteMail;
import abdellah.project.fooddonation.entity.Donations;
import abdellah.project.fooddonation.entity.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class infoDonation_ extends Fragment {

   private TextView title;
   private TextView description;
   private TextView quantity;
   private TextView quality;
   private ImageView imageView;
   private TextView date;
   private int id_donner;
   private int id_resiever;
private Donations donation;

   private Button button;

    private double donationLongitude;
    private double donationLatitude;

    private FrameLayout frameLayout;
    View v;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      v=inflater.inflate(R.layout.fragment_info_donation_, container, false);
        title=v.findViewById(R.id.donaretitle);
        description=v.findViewById(R.id.description);
        quantity=v.findViewById(R.id.quantiter);
        quality=v.findViewById(R.id.qualiter);
        date=v.findViewById(R.id.date);
        imageView=v.findViewById(R.id.imageView2);
        button=v.findViewById(R.id.getHelpbtn);
        frameLayout=v.findViewById(R.id.containerFragment);



        Bundle arguments = getArguments();
        if (arguments != null) {
            id_donner=arguments.getInt("id_donner");
            donation= (Donations) arguments.getSerializable("donation");
            title.setText(arguments.getString("donationTitle", "Default Title"));
            description.setText(arguments.getString("donationDescription", "Default Description"));
            quantity.setText(arguments.getString("donationQuantity", "0"));
            quality.setText(arguments.getString("donationQuality", "Unknown"));
            date.setText(arguments.getString("date"));
            id_donner=arguments.getInt("id_donner");

            Glide.with(getContext())
                    .load(arguments.getString("image"))
                    .into(imageView);

            donationLatitude=arguments.getDouble("latitude");
            donationLongitude=arguments.getDouble("longitude");
            Log.d("fff",id_donner+"");
            Log.d("ffff",donationLatitude+"");
            Log.d("ffff",donationLongitude+"");
            addFragment();

        }
        button.setOnClickListener(v1 -> {
            showElegantDialog(getContext());
        });
        frameLayout.setOnClickListener(v1 -> {
               Bundle bundle=new Bundle();
               bundle.putDouble("latitude",donationLatitude);
               bundle.putDouble("longitude",donationLongitude);
               bundle.putBoolean("allMap",true);
               Navigation.findNavController(v).navigate(R.id.action_infoDonation__to_mapsFragment,bundle);
        });

     return v;
    }
    public void addFragment() {

        MapsFragment mapsFragment  = new MapsFragment();
        mapsFragment.setDonationLongitude(donationLongitude);
        mapsFragment.setDonationLongitude(donationLatitude);

        getChildFragmentManager().beginTransaction()
                .replace(R.id.containerFragment, mapsFragment)
                .commit();
    }


    public void showElegantDialog(Context context) {

        Dialog dialog = new Dialog(context);

        View view = LayoutInflater.from(context).inflate(R.layout.custom_alert_dialog, null);
        dialog.setContentView(view);

        Button btnYes = view.findViewById(R.id.btn_yes);
        Button btnNo = view.findViewById(R.id.btn_no);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoiteMail boiteMail=new BoiteMail();
                boiteMail.setDonation(donation);
                Log.d("d",donation.getIdDonner()+"");
                User user=new User();
                user.setId(SessionManager.getInstance().getCurrentUser().getId());

                boiteMail.setBeneficiary(user);
                User user1=new User();
                user1.setId(donation.getIdDonner());
                boiteMail.setDonor(user1);
                dialog.dismiss();
                getApi().createB(boiteMail).enqueue(new Callback<BoiteMail>() {
                    @Override
                    public void onResponse(Call<BoiteMail> call, Response<BoiteMail> response) {
                        if (response.isSuccessful()) {
                            // Handle success
                            Log.d("API", "BoiteMail created successfully!");
                        } else {
                            // Handle error response
                            Log.e("API", "Error: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<BoiteMail> call, Throwable t) {
                        // Handle failure (e.g., network error)
                        Log.e("API", "Failure: " + t.getMessage());
                    }
                });

            }

        });


        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Action pour Non
                dialog.dismiss();
            }
        });




        dialog.show();
    }

}