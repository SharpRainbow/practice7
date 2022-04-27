package ru.mirea.panin.mireaproject.ui.calc;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.mirea.panin.mireaproject.R;
import ru.mirea.panin.mireaproject.databinding.FragmentCalcBinding;

public class CalcFragment extends Fragment {

    private TextView textView;
    private FragmentCalcBinding binding;

    public static CalcFragment newInstance() {
        return new CalcFragment();
    }

    private String Calculate(CharSequence input) {
        double val1, val2;
        String num1 = "", num2 = "";
        String sign;
        int i = 0;
        while (Character.isDigit(input.charAt(i)) || input.charAt(i) == '.' || i == 0) {
            num1 += input.charAt(i);
            i++;
        }
        val1 = Double.parseDouble(num1);
        sign = Character.toString(input.charAt(i++));
        while (i < input.length() && (Character.isDigit(input.charAt(i)) || input.charAt(i) == '.') || i == 0) {
            num2 += input.charAt(i);
            i++;
        }
        val2 = Double.parseDouble(num2);
        switch (sign) {
            case "+":
                return Double.toString(val1 + val2);
            case "-":
                return Double.toString(val1 - val2);
            case "÷":
                return Double.toString(val1 / val2);
            case "*":
                return Double.toString(val1 * val2);
            default:
                return "err";
        }
    }

    public boolean contSign(CharSequence inp) {
        for (int i = 0; i < inp.length(); i++) {
            if (inp.charAt(i) != '.' && !Character.isDigit(inp.charAt(i)) && i != 0)
                return true;
        }
        return false;
    }

    View.OnClickListener btnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            binding.textView2.setText("");
            switch (view.getId()) {
                case R.id.buttonOne:
                    textView.append("1");
                    break;
                case R.id.buttonTwo:
                    textView.append("2");
                    break;
                case R.id.buttonThree:
                    textView.append("3");
                    break;
                case R.id.buttonFour:
                    textView.append("4");
                    break;
                case R.id.buttonFive:
                    textView.append("5");
                    break;
                case R.id.buttonSix:
                    textView.append("6");
                    break;
                case R.id.buttonSeven:
                    textView.append("7");
                    break;
                case R.id.buttonEight:
                    textView.append("8");
                    break;
                case R.id.buttonNine:
                    textView.append("9");
                    break;
                case R.id.buttonZero:
                    textView.append("0");
                    break;
                case R.id.buttonDiv:
                    if (textView.getText().length() != 0 && !contSign(textView.getText()))
                        textView.append("÷");
                    break;
                case R.id.buttonMinus:
                    if (textView.getText().length() != 0 && !contSign(textView.getText()))
                        textView.append("-");
                    break;
                case R.id.buttonPlus:
                    if (textView.getText().length() != 0 && !contSign(textView.getText()))
                        textView.append("+");
                    break;
                case R.id.buttonEq:
                    try {
                        if (contSign(textView.getText())) {
                            textView.append("=");
                            binding.textView2.setText(textView.getText());
                            textView.setText(Calculate(textView.getText()));
                        }
                    } catch (NumberFormatException ignored) {
                    }
                    break;
                case R.id.buttonMultiplicate:
                    if (textView.getText().length() != 0 && !contSign(textView.getText()))
                        textView.append("*");
                    break;
                case R.id.buttonDel:
                    if (textView.getText().length() > 0)
                        textView.setText(textView.getText().subSequence(0, (int) (textView.getText().length() - 1)));
                    break;
                case R.id.buttonClr:
                    textView.setText("");
                    break;
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCalcBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textView = binding.textCalc;
        binding.buttonOne.setOnClickListener(btnClicked);
        binding.buttonTwo.setOnClickListener(btnClicked);
        binding.buttonThree.setOnClickListener(btnClicked);
        binding.buttonFour.setOnClickListener(btnClicked);
        binding.buttonFive.setOnClickListener(btnClicked);
        binding.buttonSix.setOnClickListener(btnClicked);
        binding.buttonSeven.setOnClickListener(btnClicked);
        binding.buttonEight.setOnClickListener(btnClicked);
        binding.buttonNine.setOnClickListener(btnClicked);
        binding.buttonZero.setOnClickListener(btnClicked);
        binding.buttonDel.setOnClickListener(btnClicked);
        binding.buttonDiv.setOnClickListener(btnClicked);
        binding.buttonMultiplicate.setOnClickListener(btnClicked);
        binding.buttonMinus.setOnClickListener(btnClicked);
        binding.buttonPlus.setOnClickListener(btnClicked);
        binding.buttonEq.setOnClickListener(btnClicked);
        binding.buttonClr.setOnClickListener(btnClicked);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}