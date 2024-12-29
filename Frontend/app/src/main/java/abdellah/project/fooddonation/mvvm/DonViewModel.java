package abdellah.project.fooddonation.mvvm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import abdellah.project.fooddonation.entity.Don;
import abdellah.project.fooddonation.repositories.DonRepository;


// viewmodels/DonViewModel.java
public class DonViewModel extends ViewModel {
    private final MutableLiveData<Don> donLiveData = new MutableLiveData<>();
    private final DonRepository donRepository;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public DonViewModel() {
        donRepository = new DonRepository();
    }

    public LiveData<Don> getDonLiveData() {
        return donLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void creerDon(Don don) {
        isLoading.setValue(true);
        donRepository.creerDon(don, new DonRepository.DonCallback() {
            @Override
            public void onSuccess(Don nouveauDon) {
                isLoading.postValue(false);
                donLiveData.postValue(nouveauDon);
            }

            @Override
            public void onError(String message) {
                isLoading.postValue(false);
                error.postValue(message);
            }
        });
    }
}
