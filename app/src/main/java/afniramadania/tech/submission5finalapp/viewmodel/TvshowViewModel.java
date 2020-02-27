package afniramadania.tech.submission5finalapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Objects;

import afniramadania.tech.submission5finalapp.api.ApiInterface;
import afniramadania.tech.submission5finalapp.models.TvGenre;
import afniramadania.tech.submission5finalapp.models.TvGenresResponse;
import afniramadania.tech.submission5finalapp.models.TvResponse;
import afniramadania.tech.submission5finalapp.models.Tvshow;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static afniramadania.tech.submission5finalapp.BuildConfig.API_KEY;
import static afniramadania.tech.submission5finalapp.api.ApiUtils.getApi;

public class TvshowViewModel extends ViewModel {

    private static final ApiInterface apiInterface = getApi();
    private final MutableLiveData<ArrayList<Tvshow>> lisTvs = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<TvGenre>> listTvGenres = new MutableLiveData<>();
    private final MutableLiveData<Tvshow> tv = new MutableLiveData<>();

    public LiveData<ArrayList<Tvshow>> getTvs() {
        return lisTvs;
    }

    public void setTvs(final String language) {
        apiInterface.getTvs(API_KEY, language).enqueue(new Callback<TvResponse>() {
            @Override
            public void onResponse(Call<TvResponse> call, Response<TvResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<Tvshow> tvs = new ArrayList<>(Objects.requireNonNull(response.body()).getTvs());
                    lisTvs.postValue(tvs);
                    Log.d("MainActivity", "posts loaded from API");
                } else {
                    int statusCode = response.code();
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<TvResponse> call, Throwable t) {
                Log.i("MainActivity", "error", t);
            }

        });
    }

    public LiveData<ArrayList<TvGenre>> getGenres() {
        return listTvGenres;
    }

    public LiveData<Tvshow> getTv() {
        return tv;
    }

    public void searchTvs(final String language, String query) {
        apiInterface.searchTv(API_KEY, language, query).enqueue(new Callback<TvResponse>() {
            @Override
            public void onResponse(Call<TvResponse> call, Response<TvResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<Tvshow> tvs = new ArrayList<>(Objects.requireNonNull(response.body()).getTvs());
                    lisTvs.postValue(tvs);
                    Log.d("MainActivity", "posts loaded from API");
                } else {
                    int statusCode = response.code();
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<TvResponse> call, Throwable t) {
                Log.i("MainActivity", "error", t);
            }

        });
    }

    public void setTv(int tv_id, String language) {
        apiInterface.getTv(tv_id, API_KEY, language).enqueue(new Callback<Tvshow>() {
            @Override
            public void onResponse(Call<Tvshow> call, Response<Tvshow> response) {
                if (response.isSuccessful()) {
                    tv.postValue(response.body());
                    Log.d("MainActivity", "posts loaded from API");
                } else {
                    int statusCode = response.code();
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<Tvshow> call, Throwable t) {
                Log.d("MainActivity", "error loading from API");
            }

        });
    }

    public void setGenre(String language) {
        apiInterface.getTvGenres(API_KEY, language).enqueue(new Callback<TvGenresResponse>() {
            @Override
            public void onResponse(Call<TvGenresResponse> call, Response<TvGenresResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<TvGenre> tvGenres = new ArrayList<>(Objects.requireNonNull(response.body()).getGenres());
                    listTvGenres.postValue(tvGenres);
                    Log.d("MainActivity", "posts loaded from API");
                } else {
                    int statusCode = response.code();
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<TvGenresResponse> call, Throwable t) {
                Log.d("MainActivity", "error loading from API");

            }


        });
    }

}
