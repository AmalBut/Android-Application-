package com.example.advancepizza.ui.specialoffer;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.advancepizza.DataBaseHelperCustomer;
import com.example.advancepizza.HomeActivity;
import com.example.advancepizza.PizzaAdapterSpecialOffers;
import com.example.advancepizza.R;
import com.example.advancepizza.User;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.advancepizza.Pizza;
import com.example.advancepizza.SpecialOffer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.//
 * Use the {@link SpecialOfferFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpecialOfferFragment extends Fragment {

    private ProgressBar progressBar;
    public static List<Pizza> allPizzas = new ArrayList<>();
    public static List<SpecialOffer> allSpecialOffers = new ArrayList<>();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String currentDate;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private PizzaAdapterSpecialOffers adapter;
    public SpecialOfferFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SpecialOffersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SpecialOfferFragment newInstance(String param1, String param2) {
        SpecialOfferFragment fragment = new SpecialOfferFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        progressBar = getActivity().findViewById(R.id.progressBar_specialOffers);
        recyclerView = getActivity().findViewById(R.id.recycler_special_offers);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                currentDate= dateFormat.format(calendar.getTime());


                getSpecialOffersFromDB();
                adapter = new PizzaAdapterSpecialOffers(getActivity(), allPizzas, allSpecialOffers);

                getActivity().runOnUiThread(() -> {
                    recyclerView.setAdapter(adapter);
                    progressBar.setVisibility(View.INVISIBLE);
                });
            }
        });
        thread.start();

    }

    // function to get all special offers from the database
    private void getSpecialOffersFromDB() {
        allPizzas.clear();
        allSpecialOffers.clear();

        DataBaseHelperCustomer dataBaseHelper = new DataBaseHelperCustomer(getActivity());
        Cursor cursor = dataBaseHelper.getAllSpecialOffersWithPizzaInfo();


        while (cursor.moveToNext()) {
            Pizza pizza = new Pizza();
            pizza.setId(cursor.getInt(6));
            pizza.setName(cursor.getString(7));
            pizza.setType(cursor.getString(8));

            pizza.setImgPizza(cursor.getInt(9));

            // check if the pizza in fav list
            boolean fav = dataBaseHelper.isFavorite(User.currentUser.getString(0), pizza.getId());
            if (fav)
                pizza.setImgFavButton(R.drawable.ic_favorite);
            else
                pizza.setImgFavButton(R.drawable.ic_favorite_border);


            allPizzas.add(pizza);

            // get special offer
            SpecialOffer specialOffer = new SpecialOffer();
            specialOffer.setId(cursor.getInt(0));
            specialOffer.setPizzaId(cursor.getInt(1));
            specialOffer.setPizzaSize(cursor.getString(2));
            specialOffer.setStartDate(cursor.getString(3));
            specialOffer.setEndDate(cursor.getString(4));
            specialOffer.setDiscount(cursor.getString(5));
                allSpecialOffers.add(specialOffer);


        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_special_offer, container, false);
}
}
