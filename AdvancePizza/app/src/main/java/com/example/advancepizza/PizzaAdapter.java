package com.example.advancepizza;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.advancepizza.ui.pizzamenu.PizzaMenuFragment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PizzaAdapter extends RecyclerView.Adapter<PizzaAdapter.PizzaViewHolder> {
    private List<Pizza> pizzaList;
    private List<User> userList;
    private LayoutInflater inflater;
    private final Context context;

    private double price=0;

    private String size;


    public PizzaAdapter(Context context, List<Pizza> pizzaList) {
        this.inflater = LayoutInflater.from(context);
        this.pizzaList = pizzaList;
        this.context = context;
    }

    public PizzaAdapter(Context context, List<Pizza> pizzaList, List<User> userList) {
        this.inflater = LayoutInflater.from(context);
        this.pizzaList = pizzaList;
        this.userList = userList;
        this.context = context;
    }


    @Override
    public PizzaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.pizza_item, parent, false);
        return new PizzaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PizzaViewHolder holder, int position) {
        Pizza currentPizza = pizzaList.get(position);
        Pizza pricepizza = Pizza.getPizzaByID(currentPizza.getId());

        holder.pizzaName.setText(pricepizza.getName());
        int[] test=pricepizza.getPrice();
        holder.pizzaPrice.setText("Price  "+String.valueOf(test[0])+"$  "+String.valueOf(test[1])+"$  "+String.valueOf(test[2])+"$");
        holder.pizzaSize.setText("Size "+"   S    M    L");
        holder.pizzaImage.setImageResource(pricepizza.getImgPizza());
        holder.imgFav.setImageResource(currentPizza.getImgFavButton());
        holder.order.setVisibility(currentPizza.getVisibleOrderButton());
        // holder.viewDate.setVisibility(pricepizza.getVisibleDate());

        if(context instanceof HomeAdminActivity){
            // remove the favorite button and space from the admin view
            holder.favLayout.removeView(holder.imgFav);
            holder.favLayout.removeView(holder.space_pizzaItem);

            // remove the reserve button from the admin view
            holder.mainLayout.removeView(holder.order);
            holder.mainLayout.removeView(holder.pizzaInfoLayout);

            User currentUser = userList.get(position);


            // split the date and time
            String[] dateTime = currentPizza.getDate().split("T");
            String date = dateTime[0];
            String time = dateTime[1];

            TextView textView = new TextView(holder.itemView.getContext());
            TextView textView2 = new TextView(holder.itemView.getContext());
            String text = currentPizza.getName()+" - "+currentPizza.getType();
            String text2 = "Reserved by:\n"+currentUser.getFirstName()+" "+currentUser.getLastName()+"\n"+currentUser.getEmail() +"\n"+date+"\n"+time;

            LinearLayout linearLayout = new LinearLayout(holder.itemView.getContext());
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setPadding(20, 20, 20, 20);


            textView.setTypeface(null, Typeface.BOLD);
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(15);
            textView.setText(text);

            textView2.setTextColor(Color.BLACK);
            textView2.setTextSize(15);
            textView2.setText(text2);

            linearLayout.addView(textView);
            linearLayout.addView(textView2);

            holder.favLayout.removeView(linearLayout);
            holder.favLayout.addView(linearLayout);

        } else{
            DataBaseHelperCustomer dataBaseHelper = ((HomeActivity)inflater.getContext()).getDatabaseHelper();

            holder.order.setText("Order");
            holder.pizzaInfoLayout.removeView(holder.viewDate);
            //change the high of the pizzaInfoLayout
            holder.pizzaInfoLayout.getLayoutParams().height = 158;
            // padding bottom for the pizzaInfoLayout
            holder.pizzaInfoLayout.setPadding(0,0,0,5);


        }



    }

    @Override
    public int getItemCount() {
        return pizzaList.size();
    }

    class PizzaViewHolder extends RecyclerView.ViewHolder {
        private ImageView pizzaImage;
        private TextView pizzaName;
        private TextView pizzaPrice;
        private TextView pizzaSize;
        private TextView viewDate;
        private ImageButton imgFav;
        private Button order;
        private LinearLayout mainLayout;
        private LinearLayout favLayout;
        private Space space_pizzaItem;
        private LinearLayout pizzaInfoLayout;

        private String typee;
        public PizzaViewHolder(View itemView) {
            super(itemView);
            pizzaImage = itemView.findViewById(R.id.imgPizza);
            pizzaName = itemView.findViewById(R.id.pizzaType);
            pizzaPrice = itemView.findViewById(R.id.pizzaPrice);
            pizzaSize=itemView.findViewById(R.id.size);
            imgFav = itemView.findViewById(R.id.imgFav);
            order=itemView.findViewById(R.id.button_reserve);
            mainLayout=itemView.findViewById(R.id.linear_pizzaItem);
            favLayout=itemView.findViewById(R.id.favLayout);
            space_pizzaItem=itemView.findViewById(R.id.space_pizzaItem);
            pizzaInfoLayout=itemView.findViewById(R.id.pizzaInfoLayout);

            imgFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Handle the click event for imgFav
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Pizza currentPizza = pizzaList.get(position);
                        if (currentPizza.getImgFavButton()==R.drawable.ic_favorite) {
                            imgFav.setImageResource(R.drawable.ic_favorite_border);
                         //   PizzaMenuFragment.makeFavouriteAlertAnimation("Removed from Favourites");
                            currentPizza.setImgFavButton(R.drawable.ic_favorite_border);
                            // remove the pizza from the favorite pizzas list in the database
                            removePizzaFromFavorites(currentPizza.getId());

                        }
                        else{
                            imgFav.setImageResource(R.drawable.ic_favorite);
                          //  PizzaMenuFragment.makeFavouriteAlertAnimation("Added to Favourites");
                            currentPizza.setImgFavButton(R.drawable.ic_favorite);
                            // add the pizza to the favorite pizzas list in the database
                            addPizzaToFavorites(currentPizza.getId());


                        }
                    }
                    notifyDataSetChanged();
                }
            });

            pizzaImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Handle the click event for imgFav
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Pizza currentPizza = pizzaList.get(position);

                        StringBuilder details = addDetails(currentPizza);

                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle("Pizza Details")
                                .setMessage(details.toString())
                                .setNegativeButton("CANCEL", null)
                                .create()
                                .show();


                    }
                }
            });


            order.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Pizza currentPizza = pizzaList.get(position);
                        StringBuilder details = addDetails(currentPizza);

                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        String[] items = {"S", "M", "L"};
                        int checkedItem = -1;  // Default selected item

                        // Create a main LinearLayout to hold the details, selected option, and the radio buttons
                        LinearLayout mainLayout = new LinearLayout(view.getContext());
                        mainLayout.setOrientation(LinearLayout.VERTICAL);
                        mainLayout.setPadding(16, 16, 16, 16);

                        // Create a TextView for the pizza details
                        TextView detailsTextView = new TextView(view.getContext());
                        detailsTextView.setText(details.toString());
                        detailsTextView.setTextSize(18);
                        detailsTextView.setPadding(0, 0, 0, 16);
                        mainLayout.addView(detailsTextView);

                        // Create a horizontal LinearLayout to hold the radio buttons
                        LinearLayout radioButtonLayout = new LinearLayout(view.getContext());
                        radioButtonLayout.setOrientation(LinearLayout.HORIZONTAL);

                        // Create the radio buttons and add them to the horizontal LinearLayout
                        RadioGroup radioGroup = new RadioGroup(view.getContext());
                        radioGroup.setOrientation(RadioGroup.HORIZONTAL);

                        for (int i = 0; i < items.length; i++) {
                            RadioButton radioButton = new RadioButton(view.getContext());
                            radioButton.setText(items[i]);
                            radioButton.setId(i);
                            radioGroup.addView(radioButton);
                            if (i == checkedItem) {
                                radioButton.setChecked(true);
                            }
                        }

                        radioButtonLayout.addView(radioGroup);
                        mainLayout.addView(radioButtonLayout);

                        // Create a TextView to display the selected option
                        TextView selectedOptionTextView = new TextView(view.getContext());
                        selectedOptionTextView.setTextSize(16);
                        selectedOptionTextView.setPadding(0, 16, 0, 0);
                        mainLayout.addView(selectedOptionTextView);

                        // Set a listener to update the selected option text view when a radio button is selected
                        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                RadioButton selectedRadioButton = group.findViewById(checkedId);
                                Pizza pricepizza = Pizza.getPizzaByID(currentPizza.getId());
                                int[] test = pricepizza.getPrice();
                                if (selectedRadioButton.getText().toString().equals("S")) {
                                    selectedOptionTextView.setText("Selected size: " + selectedRadioButton.getText().toString() + "\nThe Price is: " + String.valueOf(test[0])+"$");
                                    typee = "S";
                                } else if (selectedRadioButton.getText().toString().equals("M")) {
                                    selectedOptionTextView.setText("Selected size: " + selectedRadioButton.getText().toString() + "\nThe Price is: " + String.valueOf(test[1])+"$");
                                    typee = "M";
                                } else if (selectedRadioButton.getText().toString().equals("L")) {
                                    selectedOptionTextView.setText("Selected size: " + selectedRadioButton.getText().toString() + "\nThe Price is: " + String.valueOf(test[2])+"$");
                                    typee = "L";
                                }

                                ///////////////////////////////

                                if (selectedRadioButton.getText().toString().equals("S")) {
                                    size = "Small";

                                } else if (selectedRadioButton.getText().toString().equals("M")) {
                                    size = "Medium";

                                } else {
                                    size = "Large";

                                }

                                //////////////////////////////

                            }
                        });

                        builder.setTitle("Pizza Details")
                                .setView(mainLayout)
                                .setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Pizza currentPizza0 = new Pizza();
                                        Pizza pricepizza = Pizza.getPizzaByID(currentPizza.getId());
                                        currentPizza0.setVisibleOrderButton(View.VISIBLE);
                                        currentPizza0.setId(currentPizza.getId());
                                        currentPizza0.setType(typee + " ");
                                        currentPizza0.setImgPizza(currentPizza.getImgPizza());

                                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                                        LocalDateTime now = LocalDateTime.now();
                                        currentPizza0.setVisibleDate(View.VISIBLE);
                                        currentPizza0.setDate(dtf.format(now));
                                       // PizzaMenuFragment.makeFavouriteAlertAnimation("Pizza has been Ordered successfully");
                                        notifyDataSetChanged();
                                        // Add order to database

                                        LocalDate currentDate = LocalDate.now();
                                        String formattedDate = currentDate.toString(); // Format the date as desired
                                        LocalTime currentTime = LocalTime.now();
                                        String formattedTime = currentTime.toString(); // Format the time as desired
                                        int[] test = pricepizza.getPrice();
                                        if (size.equals("Small")) {
                                            price = test[0];
                                        } else if (size.equals("Large")) {
                                            price = test[2];

                                        } else {
                                            price = test[1];


                                        }

                                        addOrderToDatabase(currentPizza, formattedDate, formattedTime, size, price);

                                        // Remove the pizza from the list of favorite pizzas
                                       // HomeActivity.favPizzas.remove(currentPizza);
                                        // Add the pizza to the list of Ordered pizzas
                                        HomeActivity.orderPizzas.add(currentPizza);

                                        //  HomeAdminActivity.
                                        notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("CANCEL", null);

                        AlertDialog alert = builder.create();
                        alert.setCanceledOnTouchOutside(false);
                        alert.show();
                    }
                    notifyDataSetChanged();
                }


            });





        }

        // function to add the pizza details to the alert dialog
        public StringBuilder addDetails(Pizza currentPizza){
            StringBuilder details = new StringBuilder();
            details.append("ID:").append(currentPizza.getId()).append("\n");
            details.append("Name:").append(currentPizza.getName()).append("\n");
            details.append("Type: ").append(currentPizza.getType()).append("\n");
            return details;
        }


        public void addOrderToDatabase(Pizza pizza,String formattedDate, String formattedTime, String size, double price) {
            DataBaseHelperCustomer dataBaseHelper = HomeActivity.getDatabaseHelper();
            Order order = new Order();
            order.setUserEmail(User.currentUser.getString(0));
            order.setPizzaID(pizza.getId());
            order.setPizzaSize(size);
            order.setOrderDate(formattedDate);
            order.setOrderTime(formattedTime);
            order.setPrice(price);
            dataBaseHelper.insertOrder(order);
        }

        // function to add the pizza to the favorite pizzas list in the database
        public void addPizzaToFavorites(int pizzaID){

            DataBaseHelperCustomer dataBaseHelper = HomeActivity.getDatabaseHelper();
            if (!dataBaseHelper.isFavorite(User.currentUser.getString(0),pizzaID)){
                dataBaseHelper.insertFavorite(User.currentUser.getString(0), pizzaID);
            }
            //////////////////User.currentUser.getString(0)

        }

        // function to remove the pizza from the favorite pizzas list in the database
        public void removePizzaFromFavorites(int pizzaID){

            DataBaseHelperCustomer dataBaseHelper = HomeActivity.getDatabaseHelper();
            if (dataBaseHelper.isFavorite(User.currentUser.getString(0),pizzaID)){
                dataBaseHelper.deleteFavorite(User.currentUser.getString(0), pizzaID);
            }

        }




    }

}