package com.saharia.notesell.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.saharia.notesell.Fragments.HomeFragment;
import com.saharia.notesell.R;
import com.saharia.notesell.databinding.ActivityDocumentBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DocumentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
        ActivityDocumentBinding binding;
    FirebaseAuth auth;
    Uri pdfuri;
    String item;
    String price_item;
    FirebaseStorage storagee;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDocumentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        storagee = FirebaseStorage.getInstance();
        database=FirebaseDatabase.getInstance();

        binding.docimg.setVisibility(View.INVISIBLE);



        binding.pickPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 66);
            }
        });
        binding.addTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadfiles();
            }
        });





        binding.spinner.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<String>();
        categories.add("Guide");
        categories.add("Solved papper");
        categories.add("Class notes");
        categories.add("Programming");
        categories.add("Business");
        categories.add("Design");
        categories.add("Life");
        categories.add("other");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
     binding.spinner.setAdapter(dataAdapter);

                List<String> prices = new ArrayList<String>();
                prices.add("0");
                prices.add("25");
                prices.add("50");
                prices.add("75");
                prices.add("100");
                prices.add("200");
                prices.add("500");

            ArrayAdapter<String> priceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                prices);
        // Drop down layout style - list view with radio button
           priceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
            binding.spinner2.setAdapter(priceAdapter);
               binding.spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                   @Override
                   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                       price_item=parent.getItemAtPosition(position).toString();
                       Toast.makeText(parent.getContext(), "Price set to: " + price_item, Toast.LENGTH_SHORT).show();
                   }

                   @Override
                   public void onNothingSelected(AdapterView<?> parent) {

                   }
               });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        item=parent.getItemAtPosition(position).toString();
        //price_item=parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 66 && resultCode == RESULT_OK && data.getData() != null) {
            pdfuri = data.getData();
            binding.attach.setVisibility(View.INVISIBLE);
            binding.docimg.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), "File selected", Toast.LENGTH_SHORT).show();

        }
    }

    public void uploadfiles() {

        if (pdfuri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);

            progressDialog.setTitle("Uploading");
            progressDialog.show();

            final StorageReference reference = storagee.getReference().child("pdf files").child(FirebaseAuth.getInstance().getUid())

                    .child(new Date().getTime() + "");


            reference.putFile(pdfuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String pdfurl=uri.toString();

                            String currentuid = getIntent().getExtras().getString("currentuid");
                            DatabaseReference updateData = FirebaseDatabase.getInstance()
                                    .getReference("post")
                                    .child(currentuid);
                            updateData.child("pdf").setValue(pdfurl);
                            updateData.child("postCategory").setValue(item);
                            updateData.child("postPrice").setValue(price_item);


                            progressDialog.dismiss();

                            binding.addTo.setVisibility(View.GONE);

                            Intent intent = new Intent(DocumentActivity.this,HomeActivity.
                                    class);
                            //  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();





                        }
                    });

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progess = (100.0 * snapshot.getBytesTransferred()/ snapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + ((int) progess) + "%...");
                }
            });


        }else {

            Toast.makeText(this, "No file seleted to uploads", Toast.LENGTH_SHORT).show();
        }
    }


}