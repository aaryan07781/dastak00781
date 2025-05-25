package com.aaryan7.dastakmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.aaryan7.dastakmobile.analysis.ProfitAnalysisActivity;
import com.aaryan7.dastakmobile.analysis.SalesAnalysisActivity;
import com.aaryan7.dastakmobile.billing.CreateBillActivity;
import com.aaryan7.dastakmobile.product.AddProductActivity;
import com.aaryan7.dastakmobile.product.ProductListActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView cardAddProduct, cardMyProducts, cardCreateBill, cardSalesAnalysis, cardProfitAnalysis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        initViews();
        
        // Set click listeners
        setClickListeners();
    }

    private void initViews() {
        cardAddProduct = findViewById(R.id.cardAddProduct);
        cardMyProducts = findViewById(R.id.cardMyProducts);
        cardCreateBill = findViewById(R.id.cardCreateBill);
        cardSalesAnalysis = findViewById(R.id.cardSalesAnalysis);
        cardProfitAnalysis = findViewById(R.id.cardProfitAnalysis);
    }

    private void setClickListeners() {
        cardAddProduct.setOnClickListener(this);
        cardMyProducts.setOnClickListener(this);
        cardCreateBill.setOnClickListener(this);
        cardSalesAnalysis.setOnClickListener(this);
        cardProfitAnalysis.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        
        int id = view.getId();
        if (id == R.id.cardAddProduct) {
            intent = new Intent(this, AddProductActivity.class);
        } else if (id == R.id.cardMyProducts) {
            intent = new Intent(this, ProductListActivity.class);
        } else if (id == R.id.cardCreateBill) {
            intent = new Intent(this, CreateBillActivity.class);
        } else if (id == R.id.cardSalesAnalysis) {
            intent = new Intent(this, SalesAnalysisActivity.class);
        } else if (id == R.id.cardProfitAnalysis) {
            intent = new Intent(this, ProfitAnalysisActivity.class);
        }
        
        if (intent != null) {
            startActivity(intent);
        }
    }
}
