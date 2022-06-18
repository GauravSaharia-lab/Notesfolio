package com.saharia.notesell.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.saharia.notesell.MyApplication;
import com.saharia.notesell.R;
import com.saharia.notesell.databinding.ActivityPaymentBinding;
import com.saharia.notesell.model.Addmodel;
import com.saharia.notesell.model.User;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;

public class PaymentActivity extends AppCompatActivity  implements PaymentResultListener {
         ActivityPaymentBinding binding;
    FirebaseDatabase database;
    Intent intent;
    String priceID;
    String razorpay;
   // Double amount1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Checkout.preload(getApplicationContext());

        binding.payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment();
            }
        });

        intent=getIntent();

        database=FirebaseDatabase.getInstance();
       priceID=intent.getStringExtra("amountid");
       //razorpay=



        database.getReference().child("post")
                .child(priceID)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Addmodel addmodel= dataSnapshot.getValue(Addmodel.class);

                razorpay=addmodel.getPostPrice();
                binding.productPrice.setText(addmodel.getPostPrice());
                binding.product.setText(addmodel.getPostTitile());
                binding.totalAmt.setText(addmodel.getPostPrice());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






    }

    public void startPayment() {






        Checkout checkout = new Checkout();


        final Activity activity = PaymentActivity.this;

        try {



            JSONObject options = new JSONObject();
            options.put("name", "Notesfolio");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            Integer pp=Integer.parseInt(razorpay);
            options.put("amount",pp*100);
            JSONObject pre=new JSONObject();
            pre.put("email","notesfolio@gmail.com");
            pre.put("contact","9101505193");
            options.put("prefill",pre);


            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("bookId", "" + priceID);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(firebaseAuth.getUid()).child("Library").child(priceID)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(getApplicationContext(), "Added to your library list... ", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to add to library due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, ""+s, Toast.LENGTH_SHORT).show();
    }
}