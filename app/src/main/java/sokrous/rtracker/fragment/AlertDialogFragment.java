package sokrous.rtracker.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

import sokrous.rtracker.R;

public class AlertDialogFragment extends DialogFragment implements View.OnClickListener {

    private Button saveBtn;
    private Button cancelBtn;

    private OnSaveClickListener onSaveClickListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build your AlertDialog here
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.alert_dialog,null);

        saveBtn = view.findViewById(R.id.saveBtn);
        cancelBtn = view.findViewById(R.id.cancelBtn);

        saveBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);


        return builder.create();
    }

    public void setOnSaveClickListener(OnSaveClickListener listener) {
        onSaveClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == saveBtn.getId()){
            if (onSaveClickListener != null) {
                onSaveClickListener.onSaveClick();
            }
        }else {
            System.out.println("Cancel!!!");
            dismiss();
        }
    }

    public interface OnSaveClickListener {
        void onSaveClick();
    }


}
