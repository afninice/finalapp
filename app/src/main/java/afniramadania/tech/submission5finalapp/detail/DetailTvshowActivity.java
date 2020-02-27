package afniramadania.tech.submission5finalapp.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Locale;

import afniramadania.tech.submission5finalapp.R;
import afniramadania.tech.submission5finalapp.database.FavoriteHelper;
import afniramadania.tech.submission5finalapp.models.Favorite;
import afniramadania.tech.submission5finalapp.models.Tvshow;
import afniramadania.tech.submission5finalapp.viewmodel.TvshowViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static afniramadania.tech.submission5finalapp.api.ApiUtils.IMAGE_URL;
import static afniramadania.tech.submission5finalapp.database.DatabaseContract.FavoriteColumns.BACKDROP;
import static afniramadania.tech.submission5finalapp.database.DatabaseContract.FavoriteColumns.CATEGORY;
import static afniramadania.tech.submission5finalapp.database.DatabaseContract.FavoriteColumns.CONTENT_URI;
import static afniramadania.tech.submission5finalapp.database.DatabaseContract.FavoriteColumns.ID;
import static afniramadania.tech.submission5finalapp.database.DatabaseContract.FavoriteColumns.OVERVIEW;
import static afniramadania.tech.submission5finalapp.database.DatabaseContract.FavoriteColumns.POSTER;
import static afniramadania.tech.submission5finalapp.database.DatabaseContract.FavoriteColumns.RATING;
import static afniramadania.tech.submission5finalapp.database.DatabaseContract.FavoriteColumns.RELEASE_DATE;
import static afniramadania.tech.submission5finalapp.database.DatabaseContract.FavoriteColumns.TITLE;
import static afniramadania.tech.submission5finalapp.widget.FavoriteWidget.sendRefreshBroadcast;

public class DetailTvshowActivity extends AppCompatActivity {

    public static final String TID = "tv_id";
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    @BindView(R.id.error_layout)
    LinearLayout errorLayout;
    @BindView(R.id.imgBackdrop)
    ImageView imgBackdrop;
    @BindView(R.id.imgPoster)
    ImageView imgPoster;
    @BindView(R.id.imgRating)
    ImageView imgRating;
    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.txtReleaseDate)
    TextView txtReleaseDate;
    @BindView(R.id.txtRating)
    TextView txtRating;
    @BindView(R.id.labelOverview)
    TextView labelOverview;
    @BindView(R.id.txtOverview)
    TextView txtOverview;
    @BindView(R.id.imgFavorite)
    ImageView imgFavorite;
    @BindView(R.id.content)
    ScrollView content;
    private TvshowViewModel tvViewModel;
    private int TV_ID;
    private int id, mId;
    private FavoriteHelper helper;
    private String backdropPath, title, releaseDate, overview, poster, rating, category;
    private final Observer<Tvshow> getTv = new Observer<Tvshow>() {

        @Override
        public void onChanged(Tvshow tv) {
            if (tv != null) {
                showLoading();
                imgBack.setVisibility(View.VISIBLE);
                imgFavorite.setVisibility(View.VISIBLE);
                imgBackdrop.setVisibility(View.VISIBLE);
                Glide.with(DetailTvshowActivity.this)
                        .load(IMAGE_URL + tv.getBackdropPath())
                        .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                        .into(imgBackdrop);

                imgPoster.setVisibility(View.VISIBLE);
                Glide.with(DetailTvshowActivity.this)
                        .load(IMAGE_URL + tv.getPosterPath())
                        .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                        .into(imgPoster);
                txtTitle.setText(tv.getOriginalName());
                txtReleaseDate.setVisibility(View.VISIBLE);
                txtReleaseDate.setText(tv.getFirstAirDate());
                imgRating.setVisibility(View.VISIBLE);
                txtRating.setText(tv.getRating().toString());
                labelOverview.setVisibility(View.VISIBLE);

                mId = tv.getId();
                title = tv.getOriginalName();
                poster = tv.getPosterPath();
                backdropPath = tv.getBackdropPath();
                releaseDate = tv.getFirstAirDate();
                rating = tv.getRating().toString();
                overview = tv.getOverview();
                checkFavorite();
                if (tv.getOverview().length() == 0) {
                    txtOverview.setText(getResources().getString(R.string.not_found));

                } else {
                    txtOverview.setText(tv.getOverview());

                }
            }
        }
    };
    private Favorite favorite = new Favorite();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tvshow);
        ButterKnife.bind(this);
        checkConnection();
       setupViewModeL();
        setupData();

    }

    private void checkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
        } else {
            showLoading();
            showError();
        }
    }

    private void setupData() {

        helper = new FavoriteHelper(getApplicationContext());
        helper.open();
        TV_ID = getIntent().getIntExtra(TID, TV_ID);
        String LANGUANGE = Locale.getDefault().toString();
        if (LANGUANGE.equals("in_ID")) {
            LANGUANGE = "id_ID";
        }
        tvViewModel.setTv(TV_ID, LANGUANGE);

    }

    private void setupViewModeL() {

        tvViewModel = ViewModelProviders.of(this).get(TvshowViewModel.class);
        tvViewModel.getTv().observe(this, getTv);
    }


    private void showLoading() {
        if (false) {
            avi.smoothToShow();
        } else {
            avi.smoothToHide();
        }
    }


    private void showError() {
        errorLayout.setVisibility(View.VISIBLE);
    }


    @OnClick(R.id.imgBack)
    public void onViewClicked() {
        finish();
    }


    @OnClick({R.id.imgFavorite})
    public void doFavorite(View view) {
        if (checkFavorite()) {
            Uri uri = Uri.parse(CONTENT_URI + "/" + id);
            getContentResolver().delete(uri, null, null);
            imgFavorite.setImageResource(R.drawable.ic_unfavorite);
            Toast.makeText(this, getString(R.string.unfavorite), Toast.LENGTH_SHORT).show();
        } else {
            favorite.setmId(mId);
            favorite.setTitle(title);
            favorite.setPoster(poster);
            favorite.setBackdrop(backdropPath);
            favorite.setRating(rating);
            favorite.setReleaseDate(releaseDate);
            favorite.setOverview(overview);
            favorite.setCategoty("movie");

            ContentValues values = new ContentValues();
            values.put(ID, mId);
            values.put(TITLE, title);
            values.put(POSTER, poster);
            values.put(BACKDROP, backdropPath);
            values.put(RATING, rating);
            values.put(RELEASE_DATE, releaseDate);
            values.put(OVERVIEW, overview);
            values.put(CATEGORY, "tv");

            if (getContentResolver().insert(CONTENT_URI, values) != null) {
                Toast.makeText(this, title + " " + getString(R.string.favorite), Toast.LENGTH_SHORT).show();
                imgFavorite.setImageResource(R.drawable.ic_favorite);
            } else {
                Toast.makeText(this, title + " " + getString(R.string.favorite_error), Toast.LENGTH_SHORT).show();
            }

        }
        sendRefreshBroadcast(getApplicationContext());

    }

    private boolean checkFavorite() {
        Uri uri = Uri.parse(CONTENT_URI + "");
        boolean favorite = false;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        int getmId;
        if (cursor.moveToFirst()) {
            do {
                getmId = cursor.getInt(1);
                if (getmId == mId) {
                    id = cursor.getInt(0);
                    imgFavorite.setImageResource(R.drawable.ic_favorite);
                    favorite = true;
                }
            } while (cursor.moveToNext());

        }

        return favorite;
    }

}
