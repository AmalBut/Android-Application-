package com.example.advancepizza.ui.pizzamenu;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.advancepizza.DataBaseHelperCustomer;
import com.example.advancepizza.HomeActivity;
import com.example.advancepizza.Pizza;
import com.example.advancepizza.PizzaAdapter;
import com.example.advancepizza.PizzaJasonParser;
import com.example.advancepizza.R;
import com.example.advancepizza.User;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PizzaMenuFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private PizzaMenuModel mViewModel;
    public static FragmentActivity activity;

    public static TextView textView_favourite_alert;
    private List<Button> categoryButtons = new ArrayList<>();
    private List<Pizza> filteredPizzas = new ArrayList<>();
    private DataBaseHelperCustomer dataBaseHelper;
    private ProgressBar progressBar;
    private Button button_all;
    private Button button_chicken;
    private Button button_beef;
    private Button button_seafood;
    private Button button_cheese;
    private Button button_speciality;
    private Button button_veggies;
    private EditText searchField;
    private String click;
    private ImageButton OpenBottomSheet;
    private List<Pizza> filteredPizza = new ArrayList<>();
    private boolean filterApplied = false;

    private RecyclerView recyclerView;
    private PizzaAdapter adapter;
    public Button lastButtonPressed;
    int counter = 0;
    String[] Categories = {
            "Choose Category","Beef","Cheese","Chicken","Seafood","Specialty","Veggies"
    };

    public static PizzaMenuFragment newInstance() {
        return new PizzaMenuFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PizzaMenuModel.class);
        // TODO: Use the ViewModel
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_pizzamenu, container, false);



        return view;
    }

    public static void makeFavouriteAlertAnimation(String message){
        // transtion animation for textview_favourite_alert
        textView_favourite_alert.setText(message);
        textView_favourite_alert.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.favourite_alert_animation));

    }

    @Override
    public void onResume(){
        super.onResume();
        // initialize the layout
        initializeLayout();

        showProgressBar(true);

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogTheme);

        lastButtonPressed = button_all;

        // set the listener for the factory buttons
        setListenerForButtons();

        // set the listener for the filter button
        setListenerForFilterButton(bottomSheetDialog);

        // set the listener for the search field
        setListenerForSearchField();


        new Thread(() -> {

            // transition animation for textview_favourite_alert
            textView_favourite_alert.startAnimation(AnimationUtils .loadAnimation(getActivity(),R.anim.favourite_alert_initial));
            activity = getActivity();

            // get all cars from the database
            getAllPizza();

            // Update UI on the main thread
            getActivity().runOnUiThread(() -> {
                recyclerView = getActivity().findViewById(R.id.pizza_menu);
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                checkFavoritePizzasList();
               // checkFavoriteCarsList();

                adapter = new PizzaAdapter(getActivity(),HomeActivity.allPizzas);
                recyclerView.setAdapter(adapter);

                showProgressBar(false); // Hide progress bar
            });
        }).start();




    }

    public void checkFavoritePizzasList(){
        for (Pizza pizza : HomeActivity.allPizzas) {
            if (dataBaseHelper.isFavorite(User.currentUser.getString(0), pizza.getId())) {
                pizza.setImgFavButton(R.drawable.ic_favorite);
            } else {
                pizza.setImgFavButton(R.drawable.ic_favorite_border);
            }
        }

    }

    public void getAllPizza(){
        HomeActivity.allPizzas.clear();
        HomeActivity.Chicken.clear();
        HomeActivity.Cheese.clear();
        HomeActivity.Beef.clear();
        HomeActivity.Specialty.clear();
        HomeActivity.Seafood.clear();
        HomeActivity.Veggies.clear();


        // get all cars not reserved and not on special offer.
        Cursor cursor = dataBaseHelper.getAllPizzas();
        while (cursor.moveToNext()) {
            Pizza pizza = new Pizza();
            pizza.setId(cursor.getInt(0));
            pizza.setName(cursor.getString(1));
            pizza.setPrice(PizzaJasonParser.PIZZAS.get(pizza.getId()).getPrice());
            pizza.setType(cursor.getString(2));
            pizza.setImgPizza(cursor.getInt(3));
            pizza.setImgFavButton(R.drawable.ic_favorite_border);


           HomeActivity.allPizzas.add(pizza);

            if (pizza.getType().equals("Cheese")){
                HomeActivity.Cheese.add(pizza);
            } else if (pizza.getType().equals("Chicken")){
                HomeActivity.Chicken.add(pizza);
            } else if (pizza.getType().equals("Seafood")){
                HomeActivity.Seafood.add(pizza);
            }else if (pizza.getType().equals("Specialty")){
                HomeActivity.Specialty.add(pizza);
            }else if (pizza.getType().equals("Beef")){
                HomeActivity.Beef.add(pizza);
            }else if (pizza.getType().equals("Veggies")){
                HomeActivity.Veggies.add(pizza);
            }
        }
    }

    private void setListenerForSearchField(){

        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                List<Pizza> pizzas;
                if (filterApplied){
                    // if the user applied a filter (search in the filtered cars)
                    pizzas = searchForPizzaByType(searchField.getText().toString(), filteredPizzas);
                } else {
                    // if the user didn't apply a filter (search in the current array selected)
                    pizzas = searchForPizzaByType(searchField.getText().toString(), findCurrentArray(lastButtonPressed));
                }
                adapter = new PizzaAdapter(getActivity(), pizzas);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }

        });

    }

    public List<Pizza> findCurrentArray(Button lastButtonPressed){
        List<Pizza> currentArray = new ArrayList<>();
        if (lastButtonPressed.getText().toString().equals("All")){
            currentArray = HomeActivity.allPizzas;
        } else if (lastButtonPressed.getText().toString().equals("Cheese")){
            currentArray = HomeActivity.Cheese;
        } else if (lastButtonPressed.getText().toString().equals("Beef")){
            currentArray = HomeActivity.Beef;
        } else if (lastButtonPressed.getText().toString().equals("Chicken")){
            currentArray = HomeActivity.Chicken;
        } else if (lastButtonPressed.getText().toString().equals("Veggies")){
            currentArray = HomeActivity.Veggies;
        } else if (lastButtonPressed.getText().toString().equals("Specialty")){
            currentArray = HomeActivity.Specialty;
        } else if (lastButtonPressed.getText().toString().equals("Seafood")){
            currentArray = HomeActivity.Seafood;
        }
        return currentArray;
    }


    public List<Pizza> searchForPizzaByType(String key, List<Pizza> pizzas){
        List<Pizza> searchedPizzas = new ArrayList<>();
        for (Pizza pizza : pizzas) {
            String pizzaType = pizza.getName().toLowerCase().trim() + pizza.getType().toLowerCase().trim();
            String pizzaType2 = pizza.getType().toLowerCase().trim() + pizza.getName().toLowerCase().trim();
            pizzaType = pizzaType.replaceAll(" ", "");
            pizzaType2 = pizzaType2.replaceAll(" ", "");
            String filteredKey = key.toLowerCase().trim().replaceAll(" ", "");
            if (pizzaType.contains(filteredKey) || pizzaType2.contains(filteredKey)) {
                searchedPizzas.add(pizza);
            }
        }
        return searchedPizzas;
    }

      private void setListenerForFilterButton(BottomSheetDialog bottomSheetDialog){
        // set the listener for the filter button
        OpenBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View bottomSheetView = LayoutInflater.from(getActivity())
                        .inflate(R.layout.modal_bottom_sheet,
                                (LinearLayout) getActivity().findViewById(R.id.modalBottomSheetContainer));
                List<Button> clickedButtons = new ArrayList<>();

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
                Button small = bottomSheetView.findViewById(R.id.btn_small);
                Button medium = bottomSheetView.findViewById(R.id.btn_medium);

                Button large = bottomSheetView.findViewById(R.id.btn_large);

                Button btn_apply = bottomSheetView.findViewById(R.id.btn_apply);
                Button btn_cancel = bottomSheetView.findViewById(R.id.btn_cancel);

                EditText priceFrom = bottomSheetView.findViewById(R.id.editTextNumber_priceFrom);
                EditText priceTo = bottomSheetView.findViewById(R.id.editTextNumber_priceTo);

                Spinner spin = bottomSheetView.findViewById(R.id.categorySpin);
                spin.setOnItemSelectedListener(PizzaMenuFragment.this);


                ArrayAdapter ad = new ArrayAdapter(getActivity(),
                        android.R.layout.simple_spinner_item, Categories );

                ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin.setAdapter(ad);

                btn_apply.setOnClickListener(v1 -> {
                    ProgressBar pb = (ProgressBar) bottomSheetView.findViewById(R.id.progressBar);
                    prog(pb);

                    // check the validity of the price range
                    if (!checkPriceRangeValidity(priceFrom, priceTo)){
                        return;
                    }
                    // find the current array based on the last button pressed
                    List<Pizza> currentArray = findCurrentArray(lastButtonPressed);
                    filteredPizzas.clear();

                //ADDED BY AMAL FROM HERE --->
                    small.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                          click="small";
                         // clickedButtons.add(small);
                        }
                    });
                    medium.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            click="medium";
                           // clickedButtons.add(medium);
                        }
                    });
                    large.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            click="large";
                         //   clickedButtons.add(large);
                        }
                    });
                     //////////////////TILL HERE

                        //adapter = new PizzaAdapter(getActivity(),HomeActivity.Beef);

                    filteredPizzas = applyFilter(currentArray, clickedButtons, spin, priceFrom, priceTo,click);   //click ADDED BY AMAL
                    filterApplied = true;
                    searchField.setHint("Search in filtered pizzas");

                    //adapter = new PizzaAdapter(getActivity(), HomeActivity.Beef);
                    recyclerView.setAdapter(adapter);

                    bottomSheetDialog.dismiss();
                });

               /* for (Button button : fuelTypeButtons) {
                    button.setOnClickListener(v12 -> {
                        if(lastFuelTypeButtonPressed!=null && !lastFuelTypeButtonPressed.equals(button)){
                            checkClickedButtons(clickedButtons,lastFuelTypeButtonPressed);
                        }
                        checkClickedButtons(clickedButtons,button);
                        lastFuelTypeButtonPressed = button;
                    });
                }*/



                btn_cancel.setOnClickListener(v18 -> {
                    bottomSheetDialog.cancel();
                    clickedButtons.clear();
                });
            }
        });
    }

    List<Pizza> applyFilter(List<Pizza> currentArray, List<Button> clickedButtons, Spinner spin, EditText priceFrom, EditText priceTo,String c) {
        List<Pizza> filteredPizzas = new ArrayList<>();
        List<String> filter = new ArrayList<>();

        for (int i = 0; i < clickedButtons.size(); i++) {
            filter.add(clickedButtons.get(i).getText().toString().toLowerCase().trim());
        }

            if (!spin.getSelectedItem().toString().equals("Choose Category")) {

                if (spin.getSelectedItem().toString().equals("Beef")) {
                    adapter = new PizzaAdapter(getActivity(), HomeActivity.Beef);
                    recyclerView.setAdapter(adapter);


                } else if (spin.getSelectedItem().toString().equals("Chicken")) {
                    adapter = new PizzaAdapter(getActivity(), HomeActivity.Chicken);
                    recyclerView.setAdapter(adapter);


                } else if (spin.getSelectedItem().toString().equals("Cheese")) {
                    adapter = new PizzaAdapter(getActivity(), HomeActivity.Cheese);
                    recyclerView.setAdapter(adapter);

                } else if (spin.getSelectedItem().toString().equals("Seafood")) {
                    adapter = new PizzaAdapter(getActivity(), HomeActivity.Seafood);
                    recyclerView.setAdapter(adapter);


                } else if (spin.getSelectedItem().toString().equals("Specialty")) {
                    adapter = new PizzaAdapter(getActivity(), HomeActivity.Specialty);
                    recyclerView.setAdapter(adapter);

                } else if (spin.getSelectedItem().toString().equals("Veggies")) {
                    adapter = new PizzaAdapter(getActivity(), HomeActivity.Veggies);
                    recyclerView.setAdapter(adapter);

                }
                filter.add("Category");
            }

        /*for (Pizza pizza : currentArray) {
        //    String pizzaSize=pizza.getSize();
            if (!filter.contains("automatic") && !filter.contains("manual")){ // if the user didn't choose any transmission type
                pizzaTransmission = "a";
                filter.add("a");
            }
            if (!filter.contains("diesel") && !filter.contains("petrol") && !filter.contains("electric") && !filter.contains("hybrid")){ // if the user didn't choose any fuel type
                pizzaFuelType = "b";
                filter.add("b");
            }


            if (filter.contains(carTransmission) && filter.contains(carFuelType)) {
                if (filter.contains("mileAge")) {
                    if (carMileage >= minMileage && carMileage <= maxMileage) {
                        filteredPizzas.add(pizza);
                    }
                } else {
                    filteredPizzas.add(pizza);
                }
            }
        }*/

        // if the user didn't choose any filter
        if(filter.size()==0){
            // make a copy of the all cars list
            filteredPizzas = new ArrayList<>(currentArray);
        }

        if (!priceFrom.getText().toString().replace(" ", "").equals("") || !priceTo.getText().toString().replace(" ", "").equals("")) {
            // filter the cars based on the price range
            filteredPizzas = new ArrayList<>(currentArray);
            filteredPizzas = filterPizzasBasedOnPriceRange(filteredPizzas, priceFrom, priceTo,c); //c ADDED BY AMAL
            adapter = new PizzaAdapter(getActivity(),filteredPizzas);
            recyclerView.setAdapter(adapter);
        }

        return filteredPizzas;
    }

    public List<Pizza> filterPizzasBasedOnPriceRange(List<Pizza> filteredPizzas, EditText priceFrom, EditText priceTo,String c){
        List<Pizza> filteredPizzasBasedOnPriceRange = new ArrayList<>();
        for (Pizza pizza : filteredPizzas) {
            int i=0;
            // remove the $ sign from the price
            int[] pizzaPrice = pizza.getPrice();

/////////CONDITIONS MODIFIED BY AMAL
                if (!priceFrom.getText().toString().replace(" ", "").equals("") || !priceTo.getText().toString().replace(" ", "").equals("") && c.equals("small")) {
                    if (pizzaPrice[0] >= Integer.parseInt(priceFrom.getText().toString()) && pizzaPrice[0] <= Integer.parseInt(priceTo.getText().toString())) {
                        filteredPizzasBasedOnPriceRange.add(pizza);
                    }
                } else if (!priceFrom.getText().toString().replace(" ", "").equals("") || !priceTo.getText().toString().replace(" ", "").equals("") && c.equals("medium")) {
                    if (pizzaPrice[1] >= Integer.parseInt(priceFrom.getText().toString()) && pizzaPrice[1] <= Integer.parseInt(priceTo.getText().toString())) {
                        filteredPizzasBasedOnPriceRange.add(pizza);
                    }
                }  else if (!priceFrom.getText().toString().replace(" ", "").equals("") || !priceTo.getText().toString().replace(" ", "").equals("") && c.equals("large")) {
                    if (pizzaPrice[2] >= Integer.parseInt(priceFrom.getText().toString()) && pizzaPrice[2] <= Integer.parseInt(priceTo.getText().toString())) {
                        filteredPizzasBasedOnPriceRange.add(pizza);
                    }
                }
                /* else if (!priceFrom.getText().toString().replace(" ", "").equals("")) {
                    if (pizzaPrice[i] >= Integer.parseInt(priceFrom.getText().toString())) {
                        filteredPizzasBasedOnPriceRange.add(pizza);
                    }
                } else if (!priceTo.getText().toString().replace(" ", "").equals("")) {
                    if (pizzaPrice[i] <= Integer.parseInt(priceTo.getText().toString())) {
                        filteredPizzasBasedOnPriceRange.add(pizza);
                    }
                }*/

        }
        return filteredPizzasBasedOnPriceRange;
    }

    private void showProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
    public void initializeLayout(){
        button_all =(Button) getActivity().findViewById(R.id.button_all);
        button_chicken =(Button) getActivity().findViewById(R.id.button_Chicken);
        button_beef =(Button) getActivity().findViewById(R.id.button_Beef);
        button_seafood =(Button) getActivity().findViewById(R.id.button_Seafood);
        button_cheese =(Button) getActivity().findViewById(R.id.button_Cheese);
        button_speciality =(Button) getActivity().findViewById(R.id.button_Specialty);
        button_veggies =(Button) getActivity().findViewById(R.id.button_Veggies);
        searchField = getActivity().findViewById(R.id.editText_search);
        OpenBottomSheet =(ImageButton) getActivity().findViewById(R.id.button_filter);
        textView_favourite_alert = getActivity().findViewById(R.id.textView_favouriteAlert);
        progressBar = getActivity().findViewById(R.id.progressBar_pizzaMenu);
        progressBar.setProgressTintList(ColorStateList.valueOf(Color.BLACK));


        textView_favourite_alert.setText("Welcome to Pizza Menu");

        categoryButtons.add(button_all);
        categoryButtons.add(button_chicken);
        categoryButtons.add(button_beef);
        categoryButtons.add(button_seafood);
        categoryButtons.add(button_cheese);
        categoryButtons.add(button_speciality);
        categoryButtons.add(button_veggies);

        dataBaseHelper =  HomeActivity.getDatabaseHelper();

    }

    private void setListenerForButtons(){

        for (Button button : categoryButtons) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showProgressBar(true);
                    if (button.getText().toString().equals("All")){
                        adapter = new PizzaAdapter(getActivity(), HomeActivity.allPizzas);
                        searchField.setHint("Search in all pizzas");
                    } else if (button.getText().toString().equals("Cheese")){
                        adapter = new PizzaAdapter(getActivity(),HomeActivity.Cheese);
                        searchField.setHint("Search in Cheese-based Pizza");
                    } else if (button.getText().toString().equals("Beef")){
                        adapter = new PizzaAdapter(getActivity(),HomeActivity.Beef);
                        searchField.setHint("Search in Beef-based Pizza");
                    } else if (button.getText().toString().equals("Chicken")){
                        adapter = new PizzaAdapter(getActivity(),HomeActivity.Chicken);
                        searchField.setHint("Search in Chicken-based Pizza");
                    } else if (button.getText().toString().equals("Veggies")){
                        adapter = new PizzaAdapter(getActivity(),HomeActivity.Veggies);
                        searchField.setHint("Search in Veggies Pizza");
                    } else if (button.getText().toString().equals("Specialty")){
                        adapter = new PizzaAdapter(getActivity(),HomeActivity.Specialty);
                        searchField.setHint("Search in Specialty");
                    } else if (button.getText().toString().equals("Seafood")){
                        adapter = new PizzaAdapter(getActivity(),HomeActivity.Seafood);
                        searchField.setHint("Search in Seafood Pizza");
                    }

                    new Thread(() -> {
                        // Update UI on the main thread
                        getActivity().runOnUiThread(() -> {
                            recyclerView.setAdapter(adapter);
                            filterApplied = false;
                            showProgressBar(false);
                        });
                    }).start();
                    if(lastButtonPressed.equals(button)){
                        return;
                    }
                    makeTextWhiteAndMakeBackgroundBlack(button);
                    makeTextBlackMakeBackgroundWhite(lastButtonPressed);
                    lastButtonPressed=button;
                }
            });
        }
    }

    public void makeTextWhiteAndMakeBackgroundBlack(Button button){
        button.setTextColor(Color.WHITE);

    }

    public void makeTextBlackMakeBackgroundWhite(Button button){
        button.setTextColor(Color.BLACK);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void prog(ProgressBar pb) {
        final Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                counter++;
                pb.setProgress(counter);

                if (counter == 100)
                    t.cancel();

            }
        };
        t.schedule(tt, 0, 50);
    }

    private boolean checkPriceRangeValidity(EditText priceFrom, EditText priceTo){
        if (!priceFrom.getText().toString().replace(" ", "").equals("")) {
            if (!priceFrom.getText().toString().matches("[0-9]+")){
                priceFrom.setError("Please enter a numeric value");
                return false;
            }

            // check if the price is too high for integer
            if (priceFrom.getText().toString().length() > 10){
                priceFrom.setError("Invalid price");
                return false;
            }
        }

        if (!priceTo.getText().toString().replace(" ", "").equals("")) {
            if (!priceTo.getText().toString().matches("[0-9]+")){
                priceTo.setError("Please enter a numeric value");
                return false;
            }

            // check if the price is too high for integer
            if (priceTo.getText().toString().length() > 10){
                priceTo.setError("Invalid price");
                return false;
            }
        }


        // if both price from and price to are not empty
        if (!priceFrom.getText().toString().replace(" ", "").equals("") && !priceTo.getText().toString().replace(" ", "").equals("")) {
            // check if price from is greater than price to
            if (Integer.parseInt(priceFrom.getText().toString()) > Integer.parseInt(priceTo.getText().toString())) {
                priceFrom.setError("Price from must be less than price to");
                return false;
            }
        }
        return true;
    }
}