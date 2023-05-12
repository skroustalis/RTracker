package sokrous.rtracker.fragment;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import sokrous.rtracker.R;

public class ForgotPasswordFragment extends Fragment implements View.OnClickListener {

    private EditText editTextEmail;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private Button forgotPassword;
    private FragmentActivity fragmentActivity;
    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_forgot_password, container, false);

        fragmentActivity = requireActivity();
        fragmentManager = fragmentActivity.getSupportFragmentManager();

        editTextEmail = view.findViewById(R.id.editTextForgotPasswordEmail);
        mAuth = FirebaseAuth.getInstance();
        progressBar = view.findViewById(R.id.forgotPasswordFragment);

        forgotPassword = view.findViewById(R.id.resetPassBtn);

        forgotPassword.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == forgotPassword.getId()){
            resetPassword();
        }else{
            Toast.makeText(fragmentActivity, "Try again", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetPassword(){
        String txtEmail = editTextEmail.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail).matches()){
            editTextEmail.setError("please Enter Valid Email");
            editTextEmail.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(txtEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(fragmentActivity, "Please Check Your Email to Reset Password", Toast.LENGTH_SHORT).show();
                    fragmentManager.beginTransaction().replace(R.id.welcomeContainer, new SignInFragment()).commit();
                    progressBar.setVisibility(View.GONE);
                }
                else{
                    Toast.makeText(fragmentActivity, "Failed to Reset Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}