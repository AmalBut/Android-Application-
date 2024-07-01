package com.example.advancepizza.ui.call;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.advancepizza.R;

public class CallFragment extends Fragment {

    private CallViewModel mViewModel;

    public static CallFragment newInstance() {
        return new CallFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CallViewModel.class);
        // TODO: Use the ViewModel
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_call, container, false);

        Button callButton = (Button) view.findViewById(R.id.call_bt);
        Button gmailButton = (Button) view.findViewById(R.id.gmail_bt);
        Button mapsButton = (Button) view.findViewById(R.id.map_bt);

       callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dialIntent =new Intent();
                dialIntent.setAction(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:0599000000"));
                startActivity(dialIntent);
            }

        });

        gmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gmailIntent =new Intent();
                gmailIntent.setAction(Intent.ACTION_SENDTO);
                gmailIntent.setType("message/rfc822");
                gmailIntent.setData(Uri.parse("mailto:"));
                gmailIntent.putExtra(Intent.EXTRA_EMAIL, new String ("AdvancePizza@Pizza.com"));
                gmailIntent.putExtra(Intent.EXTRA_SUBJECT,"My Subject");
                gmailIntent.putExtra(Intent.EXTRA_TEXT,"Content of the message");
                startActivity(gmailIntent);
            }
        });

        mapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapsIntent =new Intent();
                mapsIntent.setAction(Intent.ACTION_VIEW);
                mapsIntent.setData(Uri.parse("geo:31.961013,35.190483"));
                startActivity(mapsIntent);
            }
        });

        return view;
    }

}