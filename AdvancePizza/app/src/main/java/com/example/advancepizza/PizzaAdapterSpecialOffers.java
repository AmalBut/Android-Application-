package com.example.advancepizza;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PizzaAdapterSpecialOffers extends RecyclerView.Adapter<PizzaAdapterSpecialOffers.PizzaViewHolder> {

    private List<Pizza> pizzaSpecialOffers;
    private List<SpecialOffer> specialOffers;
    private LayoutInflater inflater;
    private String size;

    private double price;
    String newPrice;

    public PizzaAdapterSpecialOffers(Context context, List<Pizza> pizzaList, List<SpecialOffer> specialOffers) {
        this.inflater = LayoutInflater.from(context);
        this.pizzaSpecialOffers = pizzaList;
        this.specialOffers = specialOffers;
    }

    //////////////////////for admin
    @Override
    public PizzaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.pizza_specialoffer_customer, parent, false);
        return new PizzaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PizzaViewHolder holder, int position) {

        DataBaseHelperCustomer dataBaseHelper = MainActivity.getDatabaseHelper();
        Cursor cursor = dataBaseHelper.getAllSpecialOffersWithPizzaInfo();
//        holder.pizzaImage.setImageResource(cursor.getInt(8));
        // holder.discount.setText(cursor.getString(3));

        Pizza currentPizza = pizzaSpecialOffers.get(position);
        Pizza pizzaprice=Pizza.getPizzaByID(currentPizza.getId());
        SpecialOffer currentSpecialOffer = specialOffers.get(position);

        holder.pizzaImage.setImageResource(currentPizza.getImgPizza());
        holder.imgFav.setImageResource(currentPizza.getImgFavButton());
        holder.pizzaSize.setText(currentSpecialOffer.getPizzaSize());
        holder.pizzaName.setText(currentPizza.getName());
        int[] test=pizzaprice.getPrice();
        if(currentSpecialOffer.getPizzaSize().equals("Small")){
            holder.oldPrice.setText(String.valueOf(test[0])+" $");
        }
        else if(currentSpecialOffer.getPizzaSize().equals("Large")){
            holder.oldPrice.setText(String.valueOf(test[2])+" $");
        }
        else{
            holder.oldPrice.setText(String.valueOf(test[1])+" $");
        }
        // set the old price and add strike through it
        holder.oldPrice.setPaintFlags(holder.oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        //  holder.startDate.setText(currentSpecialOffer.getStartDate());
        holder.endDate.setText(currentSpecialOffer.getEndDate());
        holder.discount.setText(currentSpecialOffer.getDiscount()+" %");




         newPrice = holder.getNewPrice(pizzaprice, currentSpecialOffer);

        holder.newPrice.setText(newPrice+" $");
price=Double.valueOf(newPrice);

    }

    @Override
    public int getItemCount() {
        return pizzaSpecialOffers.size();
    }

    class PizzaViewHolder extends RecyclerView.ViewHolder {
        private ImageView pizzaImage;
        private TextView pizzaName;
        private TextView pizzaSize;

        private ImageButton imgFav;
        private Button order;
        private final TextView discount;
        private TextView endDate;
        private TextView oldPrice;
        private TextView newPrice;
        private TextView startDate;



        public PizzaViewHolder(View itemView) {
            super(itemView);
            DataBaseHelperCustomer dataBaseHelper = MainActivity.getDatabaseHelper();
            Cursor cursor = dataBaseHelper.getAllSpecialOffersWithPizzaInfo();

            pizzaImage = itemView.findViewById(R.id.imgPizzaView);
            pizzaSize=  itemView.findViewById(R.id.pizzasize);
            pizzaName = itemView.findViewById(R.id.textView_name);
            imgFav = itemView.findViewById(R.id.imgFavView);
            order = itemView.findViewById(R.id.button_order);
            endDate = itemView.findViewById(R.id.textView_endDate);

            discount = itemView.findViewById(R.id.discount);

            oldPrice = itemView.findViewById(R.id.textView_oldPrice);
            newPrice = itemView.findViewById(R.id.text_newPrice);

            imgFav.setOnClickListener(view -> {
                // Handle the click event for imgFav
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Pizza currentPizza = pizzaSpecialOffers.get(position);
                    if (currentPizza.getImgFavButton() == R.drawable.ic_favorite) {
                        imgFav.setImageResource(R.drawable.ic_favorite_border);
                        currentPizza.setImgFavButton(R.drawable.ic_favorite_border);
                        // remove the pizza from the favorite pizzas list in the database
                        removePizzaFromFavorites(currentPizza.getId());

                    } else {
                        imgFav.setImageResource(R.drawable.ic_favorite);
                        currentPizza.setImgFavButton(R.drawable.ic_favorite);
                        // add the pizza to the favorite pizzas list in the database
                        addPizzaToFavorites(currentPizza.getId());

                    }
                }
                notifyDataSetChanged();
            });

            pizzaImage.setOnClickListener(view -> {
                // Handle the click event for imgFav
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Pizza currentPizza = pizzaSpecialOffers.get(position);
                    Pizza pizzaprice=Pizza.getPizzaByID(currentPizza.getId());
                    StringBuilder details = addDetails(pizzaprice, specialOffers.get(position));

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Pizza Details")
                            .setMessage(details.toString())
                            .setNegativeButton("CANCEL", null)
                            .create()
                            .show();


                }
            });


            order.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Pizza currentPizza = pizzaSpecialOffers.get(position);
                    Pizza pizzaprice=Pizza.getPizzaByID(currentPizza.getId());
                    StringBuilder details = addDetails(pizzaprice, specialOffers.get(position));

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Pizza Details")
                            .setMessage(details.toString())
                            .setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    Pizza currentPizza0 = new Pizza();

                                    int position = getAdapterPosition();
                                    if (position != RecyclerView.NO_POSITION) {
                                        SpecialOffer currentSpecialOffer = specialOffers.get(position);

                                        size = currentSpecialOffer.getPizzaSize();

                                    }


                                    currentPizza0.setVisibleOrderButton(View.INVISIBLE);
                                    currentPizza0.setId(currentPizza.getId());
                                    currentPizza0.setType(currentPizza.getType());
                                    currentPizza0.setType(currentPizza.getType() + " ");
                                    currentPizza0.setImgPizza(currentPizza.getImgPizza());
                                    //currentPizza0.set("$"+newPrice);
                                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                                    LocalDateTime now = LocalDateTime.now();
                                    currentPizza0.setVisibleDate(View.VISIBLE);
                                    currentPizza0.setDate(dtf.format(now));
                                    currentPizza0.setOnOffer(true);

                                    notifyDataSetChanged();

                                    LocalDate currentDate = LocalDate.now();
                                    String formattedDate = currentDate.toString(); // Format the date as desired
                                    LocalTime currentTime = LocalTime.now();
                                    String formattedTime = currentTime.toString(); // Format the time as desired
                                  /*  int[] test=currentPizza.getPrice();
                                    if(size.equals("Small")) {
                                      price=  getNewPrice2(test[0], currentSpecialOffer);//  price=test[0];
                                    }
                                    else if(size.equals("Large")){
                                      price=  getNewPrice2(test[2], currentSpecialOffer);

                                    }
                                    else if(size.equals("Medium")){
                                      price=  getNewPrice2(test[1], currentSpecialOffer);
                                    }*/

                                 //   Order order = new Order();

                                //    price=getNewPrice2(order.getPrice(), currentSpecialOffer);
                                    // add reservation to database
                                    addOrderToDatabase(currentPizza0, formattedDate, formattedTime, size, price);

                                    // add the pizza to the list of reserved pizzas
                                    HomeActivity.orderPizzas.add(currentPizza0);
                                    Toast.makeText(view.getContext(), "The pizza is ordered successfully", Toast.LENGTH_SHORT).show();

                                    // remove the pizza from the special offers list
                                    notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("CANCEL", null)
                            .create()
                            .show();

                }
                notifyDataSetChanged();

            });

        }

        // function to remove the pizza from the favorite pizzas list in the database
        public void removePizzaFromFavorites(int pizzaID) {
            DataBaseHelperCustomer dataBaseHelper = ((HomeActivity) inflater.getContext()).getDatabaseHelper();
            if (dataBaseHelper.isFavorite(User.currentUser.getString(0), pizzaID)) {
                dataBaseHelper.deleteFavorite(User.currentUser.getString(0), pizzaID);
            }

        }


        // function to add the pizza to the favorite pizzas list in the database.
        public void addPizzaToFavorites(int pizzaID) {

            DataBaseHelperCustomer dataBaseHelper = ((HomeActivity) inflater.getContext()).getDatabaseHelper();
            if (!dataBaseHelper.isFavorite(User.currentUser.getString(0), pizzaID)) {
                dataBaseHelper.insertFavorite(User.currentUser.getString(0), pizzaID);
            }

        }

        // function to add the reservation to the database
        public void addOrderToDatabase(Pizza pizza , String currentTime, String currentDate, String size , double price) {
            DataBaseHelperCustomer dataBaseHelper = ((HomeActivity) inflater.getContext()).getDatabaseHelper();
            Order order = new Order();
            order.setUserEmail(User.currentUser.getString(0));
            order.setPizzaID(pizza.getId());
            order.setPizzaSize(size);
            order.setOrderDate(currentDate);
            order.setOrderTime(currentTime);
            order.setPrice(price);
            dataBaseHelper.insertOrder(order);

            // set the price of the pizza in the database to the new price
            //////dataBaseHelper.updatePizzaPrice(pizza.getId(), pizza.getPrice());

        }

        // function to add the pizza details to the alert dialog
        public StringBuilder addDetails(Pizza currentPizza, SpecialOffer specialOffer) {
            StringBuilder details = new StringBuilder();
            details.append("ID:").append(currentPizza.getId()).append("\n");
            details.append("Category: ").append(currentPizza.getType()).append("\n");
            details.append("Name: ").append(currentPizza.getName()).append("\n");
            String newPrice = getNewPrice(currentPizza, specialOffer);

            details.append("Price: ").append("$").append(newPrice).append("\n");

            return details;
        }

        public String getNewPrice(Pizza currentPizza, SpecialOffer specialOffer){
            // remove $ from price
            int[] test=currentPizza.getPrice();
            int price;

            if(specialOffer.getPizzaSize().equals("Small")){
                price =test[0];

            }
            else if(specialOffer.getPizzaSize().equals("Large")){
                price =test[2];

            }
            else{
                price = test[1];

            }

            //price = price.substring(1, price.length());

            //remove % from discount
            String discount = specialOffer.getDiscount();
            // discount = discount.substring(1, discount.length());
            int discountInt = Integer.parseInt(discount);
            // calculate new price
            double newPriceInt = price - (price * discountInt / 100.0);
            String newPrice = String.format("%.2f", newPriceInt);
            // String newPrice = Double.toString(newPriceInt);
            return newPrice;
        }
}
}
