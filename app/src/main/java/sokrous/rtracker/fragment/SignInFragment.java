package sokrous.rtracker.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import sokrous.rtracker.R;
import sokrous.rtracker.activity.DrawerBaseActivity;
import sokrous.rtracker.model.User;

public class SignInFragment extends Fragment implements View.OnClickListener {
    private EditText editTextUserName, editTextPassword;
    private
    TextView textViewForgotPassword, textViewViewRegister;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FragmentActivity fragmentActivity;
    private FragmentManager fragmentManager;
    private Button signInButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_sign_in, container, false);

        db = FirebaseFirestore.getInstance();
        fragmentActivity = requireActivity();
        fragmentManager = fragmentActivity.getSupportFragmentManager();

        editTextUserName = view.findViewById(R.id.editTextSignInUserName);
        editTextPassword = view.findViewById(R.id.editTextSignInPassword);

        textViewForgotPassword = view.findViewById(R.id.txtSignInForgotPassword);
        textViewViewRegister = view.findViewById(R.id.txtSignInRegister);

        signInButton = view.findViewById(R.id.signInFinal);
        signInButton.setOnClickListener(this);

        textViewViewRegister.setOnClickListener(this);
        textViewForgotPassword.setOnClickListener(this);

        progressBar = view.findViewById(R.id.progressBarSingIn);

        mAuth = FirebaseAuth.getInstance();

        return view;
    }

    public void buttonSignInScrSignInClicked() {

        String userName = editTextUserName.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(userName).matches()) {
            editTextUserName.setError("Please Enter a Valid Email");
            editTextUserName.requestFocus();
        }

        if (editTextPassword.length() < 6) {
            editTextPassword.setError("Please Enter Password at least 6 characters");
            editTextPassword.requestFocus();
        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(userName, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(fragmentActivity, "User Has Successfully Singed In", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(fragmentActivity, DrawerBaseActivity.class);
                    db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot documentSnapshot = task.getResult();
                                        if (documentSnapshot.exists()) {
                                            User user = documentSnapshot.toObject(User.class);
                                            System.out.println("Users!!!!!!!!!!!!!!!!!!!");
                                            intent.putExtra("LoggedInUser", user);
                                            startActivity(intent);
                                            fragmentActivity.onBackPressed();
                                        }
                                    }

                                }
                            });
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(fragmentActivity, "User is Failed to Sign In", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtSignInForgotPassword:
                fragmentManager.beginTransaction().replace(R.id.welcomeContainer, new ForgotPasswordFragment()).commit();
                break;
            case R.id.txtSignInRegister:
                fragmentManager.beginTransaction().replace(R.id.welcomeContainer, new SignUpFragment()).commit();
                break;
            case R.id.signInFinal:
                buttonSignInScrSignInClicked();
                break;
            default:
                break;
        }
    }
}