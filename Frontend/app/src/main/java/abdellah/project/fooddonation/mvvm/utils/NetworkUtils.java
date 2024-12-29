package abdellah.project.fooddonation.mvvm.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

// utils/NetworkUtils.java
public class NetworkUtils {
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    public static String handleApiError(Response<?> response) {
        try {
            if (response.errorBody() != null) {
                ResponseBody errorBody = response.errorBody();
                return errorBody.string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Une erreur s'est produite";
    }
}
