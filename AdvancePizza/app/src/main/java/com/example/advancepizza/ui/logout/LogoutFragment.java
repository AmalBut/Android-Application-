package com.example.advancepizza.ui.logout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.advancepizza.LoginActivity;
import com.example.advancepizza.databinding.FragmentLogoutBinding;
import com.example.advancepizza.LoginRegistrationAct;

public class LogoutFragment extends Fragment {

    private FragmentLogoutBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogoutViewModel logoutViewModel = new ViewModelProvider(this).get(LogoutViewModel.class);
        binding = FragmentLogoutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        StringBuilder details = new StringBuilder();
        details.append("Are you sure you want to logout?");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm Logout")
                .setMessage(details.toString())
                .setNegativeButton("cancel",null)
                .setPositiveButton("sure", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent2 = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent2);
                        getActivity().finish();
                          }
                })
                .create()
                .show();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding=null;
}
}
