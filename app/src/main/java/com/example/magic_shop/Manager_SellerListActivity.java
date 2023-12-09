package com.example.magic_shop;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Manager_SellerListActivity extends AppCompatActivity {

    private UserManager userManager;

    public Context context;

    private SellerAdapter adapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_activity_seller_list);
        getWindow().setWindowAnimations(0);

        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        userManager = UserManager.getInstance(this);

        RecyclerView recyclerView = findViewById(R.id.seller_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new SellerAdapter(userManager.getproductList(), this);

        recyclerView.setAdapter(adapter);

        fetchDataFromServer();
    }

    private void fetchDataFromServer() {
        userManager.fetchSellerDataFromServer(new UserManager.OnDataReceivedListener() {
            @Override
            public void onDataReceived() {
                String str = Integer.toString(userManager.getproductList().size());
                Log.d("fetch", str);
                updateUI();
            }
        });
    }

    private void updateUI() {
        adapter.notifyDataSetChanged();
    }





    public class SellerAdapter extends RecyclerView.Adapter<SellerAdapter.SellerViewHolder> {
        private List<User> sellerList;
        private Context context;

        SellerAdapter(List<User> sellerList, Context context) {
            this.sellerList = sellerList;
            this.context = context;
        }

        @NonNull
        @Override
        public SellerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext(); // Context 설정
            View view = LayoutInflater.from(context).inflate(R.layout.manager_item_seller, parent, false);
            return new SellerViewHolder(view, context);
        }

        @Override
        public void onBindViewHolder(@NonNull SellerViewHolder holder, int position) {
            User user = sellerList.get(position);
            holder.bind(user);
        }

        @Override
        public int getItemCount() { return sellerList.size(); }

        public class SellerViewHolder extends RecyclerView.ViewHolder {
            private final TextView dateTextView;
            private final TextView sellerNameTextView;
            private final Context context;

            public SellerViewHolder(View itemView, Context context) {
                super(itemView);
                this.context = context;
                dateTextView = itemView.findViewById(R.id.date);
                sellerNameTextView = itemView.findViewById(R.id.sellerName);
            }

            void bind(User user) {
                dateTextView.setText("seller");
                sellerNameTextView.setText(user.getUserID());
            }
        }
    }
}


