package afniramadania.tech.submission5finalapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TvResponse {

    @SerializedName("page")
    private Long mPage;
    @SerializedName("results")
    private List<Tvshow> mResults;
    @SerializedName("total_pages")
    private Long mTotalPages;
    @SerializedName("total_results")
    private Long mTotalResults;

    public List<Tvshow> getTvs() {
        return mResults;
    }




}
