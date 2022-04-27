package ru.mirea.panin.mireaproject.ui.networkdata;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.mirea.panin.mireaproject.databinding.FragmentNetworkDataBinding;

public class NetworkDataFragment extends Fragment {
    private FragmentNetworkDataBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNetworkDataBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://ipinfo.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NetworkApi networkApi = retrofit.create(NetworkApi.class);
        binding.buttonIP.setOnClickListener(view -> {
            Call<IpInfo> info = networkApi.networkInfo();
            info.enqueue(new Callback<IpInfo>() {
                @Override
                public void onResponse(Call<IpInfo> call, Response<IpInfo> response) {
                    binding.textViewIP.setText(response.body().toString());
                }

                @Override
                public void onFailure(Call<IpInfo> call, Throwable t) {
                    binding.textViewIP.setText("Failed to load.");
                }
            });
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}