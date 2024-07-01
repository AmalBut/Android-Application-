package com.example.advancepizza.ui.allorderes;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.advancepizza.MainActivity;
import com.example.advancepizza.Order;
import com.example.advancepizza.PizzaAdapterOrder;
import com.example.advancepizza.R;
import com.example.advancepizza.Pizza;
import com.example.advancepizza.PizzaAdapter;
import com.example.advancepizza.DataBaseHelperCustomer;
import com.example.advancepizza.SpecialOffer;
import com.example.advancepizza.User;
import com.example.advancepizza.HomeActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllOrdersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static FragmentActivity activity;

    private RecyclerView recyclerView;
    private PizzaAdapterOrder adapter;
    private ProgressBar progressBar;

    public static List<Pizza> allPizzas = new ArrayList<>();

    public static List<Order> allOrders = new ArrayList<>();

    List <User> allUsersOrdered = new ArrayList<>();



    public AllOrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderedPizzasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllOrdersFragment newInstance(String param1, String param2) {
        AllOrdersFragment fragment = new AllOrdersFragment();
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
        progressBar = getActivity().findViewById(R.id.progressBar_ordered);
        recyclerView = getActivity().findViewById(R.id.recycler_ordered_pizzas);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        //progressBar.setVisibility(View.VISIBLE);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                getOrderedPizzasDB();
                adapter = new PizzaAdapterOrder(getActivity(),allPizzas, allOrders);
                getActivity().runOnUiThread(() -> {
                    recyclerView.setAdapter(adapter);
                    progressBar.setVisibility(View.INVISIBLE);
                });
            }
        });
        thread.start();
    }

    // function to get the ordered pizzas from the database.
    public void getOrderedPizzasDB(){

        ////////////////////
        allPizzas.clear();
        allUsersOrdered.clear();
        allOrders.clear();

        DataBaseHelperCustomer dataBaseHelper = new DataBaseHelperCustomer(getActivity());

        Cursor cursor = dataBaseHelper.getAllOrdersWithPizzaInfoAndUserInfo();


        while (cursor.moveToNext()) {
            Pizza pizza = new Pizza();
            pizza.setId(cursor.getInt(7));
            pizza.setName(cursor.getString(8));
            pizza.setType(cursor.getString(9));
            pizza.setImgPizza(cursor.getInt(10));

            // check if the pizza in fav list

            allPizzas.add(pizza);

            // get special offer
            Order order = new Order();
            order.setOrderID(cursor.getInt(0));
            order.setUserEmail(cursor.getString(1));
            order.setPizzaID(cursor.getInt(2));
            order.setPizzaSize(cursor.getString(3));
            order.setOrderDate(cursor.getString(4));
            order.setOrderTime(cursor.getString(5));
            order.setPrice(cursor.getDouble(6));
            allOrders.add(order);

            User user = new User();
            user.setEmail(cursor.getString(11));
            user.setFirstName(cursor.getString(12));
            user.setLastName(cursor.getString(13));
            user.setGender(cursor.getString(14));
            user.setPhoneNumber(cursor.getString(15));
            allUsersOrdered.add(user);


        }

        ///////////////////
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container,false);
}
}
