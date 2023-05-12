package sokrous.rtracker.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import sokrous.rtracker.R;
import sokrous.rtracker.model.User;

public class SignUpFragment extends Fragment implements View.OnClickListener {

    EditText editTextUserName;
    EditText editTextPassword;
    EditText editTextMobileNo;
    EditText editTextEmail;
    Button signUpFinal;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;

    private FirebaseFirestore mFirestore ;

    private FragmentActivity fragmentActivity;

    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_sign_up, container, false);
        fragmentActivity = requireActivity();
        fragmentManager = fragmentActivity.getSupportFragmentManager();
        signUpFinal = view.findViewById(R.id.signUpFinal);
        editTextUserName = view.findViewById(R.id.editTextUserName);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        editTextMobileNo = view.findViewById(R.id.editTextMobileNo);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        progressBar = view.findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        signUpFinal.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        String txtUserName = editTextUserName.getText().toString().trim();
        String txtPassword = editTextPassword.getText().toString().trim();
        String txtMobileNo = editTextMobileNo.getText().toString().trim();
        String txtEmail = editTextEmail.getText().toString().trim();

        if (txtUserName.isEmpty()){
            editTextUserName.setError("Please Enter UserName");
            editTextUserName.requestFocus();
        }

        if (txtPassword.isEmpty() || txtPassword.length() < 6){
            editTextPassword.setError("Please Enter Password at least six characters");
            editTextPassword.requestFocus();
        }

        if (txtMobileNo.isEmpty()){
            editTextMobileNo.setError("Please Enter Mobile Number");
            editTextMobileNo.requestFocus();
        }

        if (txtEmail.isEmpty()){
            editTextEmail.setError("Please Enter Email");
            editTextEmail.requestFocus();
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(txtEmail, txtPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            User user = new User(txtUserName, txtPassword, txtMobileNo, txtEmail);

                            mFirestore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .set(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(fragmentActivity, "User Register successfully", Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);
                                                fragmentManager.beginTransaction().replace(R.id.welcomeContainer, new SignInFragment()).commit();
                                            } else {
                                                Toast.makeText(fragmentActivity, "User Failed to Register", Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        }
                        else {
                            Toast.makeText(fragmentActivity, "User Failed to Register", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });

    }
}