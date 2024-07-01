package com.example.advancepizza.ui.favorite;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.advancepizza.DataBaseHelperCustomer;
import com.example.advancepizza.HomeActivity;
import com.example.advancepizza.Pizza;
import com.example.advancepizza.PizzaAdapter;
import com.example.advancepizza.R;
import com.example.advancepizza.SpecialOffer;
import com.example.advancepizza.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private PizzaAdapter adapter;
    private ProgressBar progressBar;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoriteCarsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoriteFragment newInstance(String param1, String param2) {
        FavoriteFragment fragment = new FavoriteFragment();
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
 /*       @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = getActivity().findViewById(R.id.recycler_favorite_pizzas);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

//
       adapter = new PizzaAdapter(getActivity(),HomeActivity.favPizzas);
        recyclerView.setAdapter(adapter);
    }*/
    @Override
    public void onResume(){
        super.onResume();
        progressBar = getActivity().findViewById(R.id.progressBar_favorite);
        recyclerView = getActivity().findViewById(R.id.recycler_favorite_pizzas);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        progressBar.setVisibility(View.VISIBLE);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                getFavoritePizzas();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new PizzaAdapter(getActivity(),HomeActivity.favPizzas);
                        recyclerView.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
        thread.start();
    }

    // function to get the favorite cars from the database.
    public void getFavoritePizzas(){
        HomeActivity.favPizzas.clear();
        DataBaseHelperCustomer dataBaseHelper = ((HomeActivity)getActivity()).getDatabaseHelper();
        String userEmail = User.currentUser.getString(0);   //////check

        // get the favorite cars from the database
        Cursor cursor = dataBaseHelper.getFavoritesWithPizzaInfoByEmail(userEmail);

        while (cursor.moveToNext()) {
            Pizza pizza = new Pizza();
            pizza.setId(cursor.getInt(2));
            pizza.setType(cursor.getString(4));
           // pizza.setPrice(cursor.getString(6));

            Cursor offer = dataBaseHelper.getSpecialOfferForPizza(pizza.getId());
            if (offer.getCount() > 0) {
                SpecialOffer specialOffer = new SpecialOffer();
                offer.moveToNext();
               // specialOffer.setDiscount(offer.getString(4));
               // pizza.setPrice("$" + getNewPrice(pizza, specialOffer));
            }

            pizza.setImgPizza(cursor.getInt(6));
            pizza.setImgFavButton(R.drawable.ic_favorite);


            HomeActivity.favPizzas.add(pizza);



        }

    }

  /*  public String getNewPrice(Pizza currentPizza, SpecialOffer specialOffer){
        // remove $ from price
        String price = currentPizza.getPrice();
        price = price.substring(1, price.length());
        int priceInt = Integer.parseInt(price);
        // remove % from discount
        String discount = specialOffer.getDiscount();
        discount = discount.substring(0, discount.length() - 1);
        int discountInt = Integer.parseInt(discount);
        // calculate new price
        int newPriceInt = priceInt - (priceInt * discountInt / 100);
        String newPrice = Integer.toString(newPriceInt);
        return newPrice;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }
}