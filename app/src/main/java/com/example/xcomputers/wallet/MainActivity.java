package com.example.xcomputers.wallet;

import android.content.Intent;
import android.net.Uri;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements IncomeFragment.OnFragmentInteractionListener {
    Button addButton;
    Button clearAllButton;
    ScrollView scrollView;
    LinearLayout layout;
    TextView incomeTodayTV;
    TextView expenseTodayTV;
    TextView fundsInWalletTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clearAllButton = (Button) findViewById(R.id.clearAll);
        addButton = (Button) findViewById(R.id.addButton);
        scrollView = (ScrollView) findViewById(R.id.scrollView1);
        layout = (LinearLayout) findViewById(R.id.myLayout);
        incomeTodayTV = (TextView) findViewById(R.id.incomeTodayTV);
        expenseTodayTV = (TextView) findViewById(R.id.expenseTodayTV);
        fundsInWalletTV = (TextView) findViewById(R.id.fundsInWalletTV);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(intent, 10);
            }
        });

        clearAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.removeAllViews();
                incomeTodayTV.setText("0");
                expenseTodayTV.setText("0");
                fundsInWalletTV.setText("0");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Integer totalFunds = new Integer(fundsInWalletTV.getText().toString());


        switch (resultCode){
            case AddActivity.INCOME_CODE:
                IncomeFragment incomeFragment = IncomeFragment.newInstance(data.getStringExtra("amount"), data.getStringExtra("description"));
                ft.add(R.id.myLayout,incomeFragment);
                ft.commit();
                Log.e("TAG", "Income fragment");
                Integer currentIncome = new Integer(incomeTodayTV.getText().toString());
                Integer tempIncome = new Integer(data.getStringExtra("amount"));
                currentIncome += tempIncome;
                incomeTodayTV.setText(currentIncome.toString());
                totalFunds += tempIncome;
                fundsInWalletTV.setText(totalFunds.toString());
                break;
            case AddActivity.EXPENSE_CODE:
                ExpenseFragment expenseFragment = ExpenseFragment.newInstance(data.getStringExtra("amount"), data.getStringExtra("description"));
                ft.add(R.id.myLayout, expenseFragment);
                ft.commit();
                Log.e("TAG", "Expense fragment");
                Integer currentExpense = new Integer(expenseTodayTV.getText().toString());
                Integer tempExpense = new Integer(data.getStringExtra("amount"));
                currentExpense += tempExpense;
                expenseTodayTV.setText(currentExpense.toString());
                totalFunds -= tempExpense;
                fundsInWalletTV.setText(totalFunds.toString());
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onResume() {
        super.onResume();


    }
}
