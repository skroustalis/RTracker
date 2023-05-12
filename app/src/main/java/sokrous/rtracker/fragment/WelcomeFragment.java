package sokrous.rtracker.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import sokrous.rtracker.R;


public class WelcomeFragment extends Fragment implements View.OnClickListener {

    private FragmentManager fragmentManager;
    private FragmentActivity fragmentActivity;
    private Button signInBtn, signUpBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_welcome, container, false);

        fragmentActivity = requireActivity();
        fragmentManager = fragmentActivity.getSupportFragmentManager();

        signUpBtn = view.findViewById(R.id.signUpBtn);
        signInBtn = view.findViewById(R.id.signInBtn);

        signUpBtn.setOnClickListener(this);
        signInBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == signInBtn.getId()) {
            fragmentManager.beginTransaction().replace(R.id.welcomeContainer, new SignInFragment()).commit();
        } else {
            fragmentManager.beginTransaction().replace(R.id.welcomeContainer, new SignUpFragment()).commit();
        }
    }
}