package ru.mirea.panin.mireaproject.ui.player;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.concurrent.TimeUnit;

import ru.mirea.panin.mireaproject.databinding.FragmentPlayerBinding;

public class PlayerFragment extends Fragment {

    public static boolean indicator = true;
    private FragmentPlayerBinding binding;
    private SeekBar seekBar;
    private TextView startpos, endpos;
    private Button play;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPlayerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        seekBar = binding.seekBar;
        startpos = binding.textView3;
        endpos = binding.textView4;
        play = binding.buttonPlay;
        if(!indicator){
            play.setText("Stop");
        }
        else {
            play.setText("Play");
        }
        new Thread(() -> {
            seekBar.setMax(MyService.getDuration());
            while (true) {
                seekBar.setProgress(MyService.getPosition());
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        play.setOnClickListener((View v) -> {
            if(indicator){
                getActivity().startService(new Intent(getContext(), MyService.class));
                play.setText("Stop");
            }
            else {
                getActivity().stopService(new Intent(getActivity(), MyService.class));
                play.setText("Play");
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int time, min, sec;
                if(endpos.getText()=="") {
                    time = MyService.getDuration()/1000;
                    min = time/60;
                    sec = time - min*60;
                    endpos.setText(min + ":" + sec);
                }
                if(b)
                    MyService.setPosition(i);
                time = MyService.getPosition()/1000;
                min = time/60;
                sec = time - min*60;
                startpos.setText(sec < 10 ? min + ":0" + sec : min + ":" + sec);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}