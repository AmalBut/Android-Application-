package com.example.advancepizza.ui.calculation;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

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

public class CalculationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static FragmentActivity activity;
    public static List<Order> allOrders = new ArrayList<>();
    public static List<Pizza> allPizzas = new ArrayList<>();


    public CalculationFragment() {
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
    public static CalculationFragment newInstance(String param1, String param2) {
        CalculationFragment fragment = new CalculationFragment();
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


    public void getOrderedPizzasDB(String type) {

        allPizzas.clear();
        allOrders.clear();

        DataBaseHelperCustomer dataBaseHelper = new DataBaseHelperCustomer(getActivity());

        Cursor cursor = dataBaseHelper.getAllOrdersWithPizzaInfoAndUserInfo();


        while (cursor.moveToNext()) {

            Pizza pizza = new Pizza();
            Order order = new Order();



            if (type.equals(cursor.getString(8))) {

                pizza.setId(cursor.getInt(7));
                pizza.setName(cursor.getString(8));
                pizza.setType(cursor.getString(9));
                pizza.setImgPizza(cursor.getInt(10));

                allPizzas.add(pizza);

                order.setOrderID(cursor.getInt(0));
                order.setUserEmail(cursor.getString(1));
                order.setPizzaID(cursor.getInt(2));
                order.setPizzaSize(cursor.getString(3));
                order.setOrderDate(cursor.getString(4));
                order.setOrderTime(cursor.getString(5));
                order.setPrice(cursor.getDouble(6));
                allOrders.add(order);
            }
            if (type.equals("All")) {

                pizza.setId(cursor.getInt(8));
                pizza.setName(cursor.getString(8));
                pizza.setType(cursor.getString(9));
                pizza.setImgPizza(cursor.getInt(10));

                allPizzas.add(pizza);

                order.setOrderID(cursor.getInt(0));
                order.setUserEmail(cursor.getString(1));
                order.setPizzaID(cursor.getInt(2));
                order.setPizzaSize(cursor.getString(3));
                order.setOrderDate(cursor.getString(4));
                order.setOrderTime(cursor.getString(5));
                order.setPrice(cursor.getDouble(6));
                allOrders.add(order);

            }

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_calculation, container, false);
        String[] options = {"Tandoori Chicken Pizza","BBQ Chicken Pizza","Pesto Chicken Pizza","Buffalo Chicken Pizza","Pepperoni","Vegetarian Pizza","Mushroom Truffle Pizza","Margarita","New York Style","Calzone","Seafood Pizza","Hawaiian","Neapolitan"};
        String[] options2 = {"All","Tandoori Chicken Pizza","BBQ Chicken Pizza","Pesto Chicken Pizza","Buffalo Chicken Pizza","Pepperoni","Vegetarian Pizza","Mushroom Truffle Pizza","Margarita","New York Style","Calzone","Seafood Pizza","Hawaiian","Neapolitan"};

        final Spinner pizzanameSpinner = view.findViewById(R.id.pizza_spinner);
        final Spinner pizzanameSpinner2 = view.findViewById(R.id.pizza_spinner2);

        ArrayAdapter<String> objPizzaArr = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, options);
        pizzanameSpinner.setAdapter(objPizzaArr);

        ArrayAdapter<String> objPizzaArr2 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, options2);
        pizzanameSpinner2.setAdapter(objPizzaArr2);


        Button calc= view.findViewById(R.id.btn_calc);
        Button calc1= view.findViewById(R.id.btn_calc1);
        TextView numOrders=view.findViewById(R.id.num_txt);
        TextView income=view.findViewById(R.id.income);

        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOrderedPizzasDB(pizzanameSpinner.getSelectedItem().toString());
                if(allOrders!=null) {
                    numOrders.setText(String.valueOf(allOrders.size()));
                }
                else{
                    numOrders.setText("0");
                }

            }
        });

        calc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOrderedPizzasDB(pizzanameSpinner2.getSelectedItem().toString());
                if(allOrders!=null) {
                    double total_income=0;

                    for (int i = 0; i <allOrders.size() ; i++) {
                       total_income+=allOrders.get(i).getPrice();
                    }
                    income.setText(String.valueOf(total_income)+"$");

                }
                else{
                    income.setText("0$");
                }

            }
        });






        return view;
    }
}