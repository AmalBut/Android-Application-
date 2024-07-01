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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PizzaAdapterOrder extends RecyclerView.Adapter<PizzaAdapterOrder.PizzaViewHolder> {

    private List<Pizza> orderedPizza;
    private List<Order> orders;
    private LayoutInflater inflater;

        public PizzaAdapterOrder(Context context, List<Pizza> pizzaList, List<Order> order) {
        this.inflater = LayoutInflater.from(context);
        this.orderedPizza = pizzaList;
        this.orders = order;
    }

    //////////////////////for admin
    @Override
    public PizzaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.pizza_order_customer, parent, false);
        return new PizzaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PizzaViewHolder holder, int position) {

        DataBaseHelperCustomer dataBaseHelper = MainActivity.getDatabaseHelper();
        Cursor cursor = dataBaseHelper.getOrderWithPizzaInfoByEmail(User.currentUser.getString(0));

        Pizza currentPizza = orderedPizza.get(position);
        Pizza pizzaprice=Pizza.getPizzaByID(currentPizza.getId());
        Order currentOrder = orders.get(position);

        holder.userEmail.setText(currentOrder.getUserEmail());
        holder.pizzaImage.setImageResource(currentPizza.getImgPizza());
        holder.pizzaSize.setText(currentOrder.getPizzaSize());
        holder.pizzaName.setText(currentPizza.getName());
        /*int[] test=pizzaprice.getPrice();
        if(currentOrder.getPizzaSize().equals("Small")){
            holder.price.setText(String.valueOf(test[0])+" $");
        }
        else if(currentOrder.getPizzaSize().equals("Large")){
            holder.price.setText(String.valueOf(test[2])+" $");
        }
        else{
            holder.price.setText(String.valueOf(test[1])+" $");
        }*/
        holder.price.setText(String.valueOf(currentOrder.getPrice())+" $");

        //  holder.startDate.setText(currentSpecialOffer.getStartDate());
        holder.date.setText(currentOrder.getOrderDate());
holder.time.setText(currentOrder.getOrderTime());

    }

    @Override
    public int getItemCount() {
        return orderedPizza.size();
    }

    class PizzaViewHolder extends RecyclerView.ViewHolder {
        private ImageView pizzaImage;
        private TextView pizzaName;
        private TextView pizzaSize;

        private TextView userEmail;

        private Button order;
        private TextView time;
        private TextView date;
        private TextView price;


        public PizzaViewHolder(View itemView) {
            super(itemView);
            DataBaseHelperCustomer dataBaseHelper = MainActivity.getDatabaseHelper();
            Cursor cursor = dataBaseHelper.getOrderWithPizzaInfoByEmail(User.currentUser.getString(0));

            pizzaImage = itemView.findViewById(R.id.imgPizzaView);
            pizzaSize=  itemView.findViewById(R.id.pizzasize);
            pizzaName = itemView.findViewById(R.id.textView_name);
            date = itemView.findViewById(R.id.textView_date);
            time = itemView.findViewById(R.id.textView_time);
            price = itemView.findViewById(R.id.textView_price);
            userEmail = itemView.findViewById(R.id.userEmail);
            Button canceloredr= itemView.findViewById(R.id.button);

                    pizzaImage.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Pizza currentPizza = orderedPizza.get(position);
                    Pizza pizzaprice=Pizza.getPizzaByID(currentPizza.getId());

                    StringBuilder details = addDetails(pizzaprice, orders.get(position));

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Pizza Details")
                            .setMessage(details.toString())
                            .setNegativeButton("CANCEL", null)
                            .create()
                            .show();
                }
            });

            canceloredr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Pizza currentPizza = orderedPizza.get(position);
                       // if (currentPizza.getImgFavButton() == R.drawable.ic_favorite) {
                         //   imgFav.setImageResource(R.drawable.ic_favorite_border);
                            //   PizzaMenuFragment.makeFavouriteAlertAnimation("Removed from Favourites");
                       //     currentPizza.setImgFavButton(R.drawable.ic_favorite_border);
                            // remove the pizza from the favorite pizzas list in the database
                            removePizzaFromOrders(currentPizza.getId());


                      //  }
                    }
                }
            });

 }

        // function to add the pizza details to the alert dialog
        public StringBuilder addDetails(Pizza currentPizza, Order order) {
            StringBuilder details = new StringBuilder();
            details.append("ID:").append(currentPizza.getId()).append("\n");
            details.append("Category: ").append(currentPizza.getType()).append("\n");
            details.append("Name: ").append(currentPizza.getName()).append("\n");
            details.append("Price: ").append(order.getPrice()).append("$").append("\n");
            details.append("Date: ").append(order.getOrderDate()).append("\n");
            details.append("Time: ").append(order.getOrderTime()).append("\n");

            return details;
        }


    }

            public void removePizzaFromOrders(int pizzaID){

                DataBaseHelperCustomer dataBaseHelper = HomeActivity.getDatabaseHelper();
                // if (dataBaseHelper.isFavorite(User.currentUser.getString(0),pizzaID)){
                dataBaseHelper.deleteOrder(User.currentUser.getString(0), pizzaID);
                //  }

            }
}
