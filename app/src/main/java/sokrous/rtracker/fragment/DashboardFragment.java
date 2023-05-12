package sokrous.rtracker.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.io.Serializable;

import sokrous.rtracker.R;
import sokrous.rtracker.model.User;

public class DashboardFragment extends Fragment {

    TextView textUserName;
    User loggedInUser;


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_dashboard, container, false);


        Bundle bundle = this.getArguments();
        if (bundle != null){
            loggedInUser = (User) bundle.getSerializable("LoggedInUser");
        }

        System.out.println(loggedInUser.getUserName()+" - "+loggedInUser.getMobileNo());

        if (loggedInUser != null) {
            textUserName = view.findViewById(R.id.textUserName);
            textUserName.setText(loggedInUser.getUserName());
        }
        return view;
    }

}