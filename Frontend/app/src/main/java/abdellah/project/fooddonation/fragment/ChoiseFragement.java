package abdellah.project.fooddonation.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavAction;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import abdellah.project.fooddonation.R;
public class ChoiseFragement extends Fragment {

    private CardView cardViewPub,cardViewAsso;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_choise_fragement, container, false);
         cardViewAsso=view.findViewById(R.id.cardView1);
         cardViewPub=view.findViewById(R.id.cardView2);
         cardViewPub.setOnClickListener(v -> {
             Navigation.findNavController(view).navigate(R.id.action_choiseFragement_to_addFoodRescue);
         });
        cardViewAsso.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.associationListFragment);
        });



        return view;
    }
}