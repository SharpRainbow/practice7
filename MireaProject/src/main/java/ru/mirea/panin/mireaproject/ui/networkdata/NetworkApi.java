package ru.mirea.panin.mireaproject.ui.networkdata;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NetworkApi {
    @GET("json")
    Call<IpInfo> networkInfo();
}
