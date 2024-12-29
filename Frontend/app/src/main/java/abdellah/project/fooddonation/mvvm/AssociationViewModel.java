package abdellah.project.fooddonation.mvvm;

import static abdellah.project.fooddonation.client.RetrofitClient.getApi;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import abdellah.project.fooddonation.entity.Association;
import abdellah.project.fooddonation.repositories.AssociationRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


// viewmodels/AssociationViewModel.java
public class AssociationViewModel extends ViewModel {
    private final MutableLiveData<List<Association>> associations = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final AssociationRepository repository;

    public AssociationViewModel() {
        repository = new AssociationRepository();
        chargerAssociations();
    }

    public LiveData<List<Association>> getAssociations() {
        return associations;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void chargerAssociations() {
        isLoading.setValue(true);
        repository.getAssociations(new AssociationRepository.AssociationsCallback() {
            @Override
            public void onSuccess(List<Association> associationsList) {
                isLoading.postValue(false);
                associations.postValue(associationsList);
            }

            @Override
            public void onError(String message) {
                isLoading.postValue(false);
                error.postValue(message);
            }
        });
    }
    public void fetchAssociation() {
        isLoading.setValue(true);
        Call<List<Association>> call = getApi().getAllAssociation();

        call.enqueue(new Callback<List<Association>>() {
            @Override
            public void onResponse(Call<List<Association>> call, Response<List<Association>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    associations.setValue(response.body());
                } else {

                }
            }



            @Override
            public void onFailure(Call<List<Association>> call, Throwable t) {
                isLoading.setValue(false);

            }
        });
    }
}
