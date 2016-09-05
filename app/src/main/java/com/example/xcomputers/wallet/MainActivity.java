package com.example.xcomputers.wallet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements IncomeFragment.OnFragmentInteractionListener {
    Button addButton;
    Button clearAllButton;
    ScrollView scrollView;
    LinearLayout layout;
    TextView incomeTodayTV;
    TextView expenseTodayTV;
    TextView fundsInWalletTV;
    SharedPreferences sharedPreferences;
    IncomeFragment incomeFragment;
    ExpenseFragment expenseFragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
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

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        if(savedInstanceState != null){
            Log.e("TAG", "onCreate restore called");
            incomeFragment = (IncomeFragment) fragmentManager.getFragment(savedInstanceState, "incomeFragment");
        }


        sharedPreferences = MainActivity.this.getSharedPreferences("Sharedprefs",Context.MODE_PRIVATE);
        incomeTodayTV.setText(sharedPreferences.getString("income", "0"));
        expenseTodayTV.setText(sharedPreferences.getString("expense", "0"));
        fundsInWalletTV.setText(sharedPreferences.getString("wallet", "0"));
        Log.e("TAG", sharedPreferences.getString("incomeFragment", "no income Fragment in shared prefs"));
        Log.e("TAG", sharedPreferences.getString("expenseFragment", "no expense Fragment in shared prefs"));
        if(sharedPreferences.contains("incomeFragment")){
            String jsonIncome = getSharedPreferences("Sharedprefs",Context.MODE_PRIVATE).getString("incomeFragment", "");

            try {
                JSONArray incomeArray = new JSONArray(jsonIncome);
                for (int i = 0; i < incomeArray.length(); i++) {
                    JSONObject incomeObj = incomeArray.getJSONObject(i);
                    incomeFragment = IncomeFragment.newInstance(incomeObj.getString("amount"), incomeObj.getString("description"));
                    fragmentTransaction.add(R.id.myLayout, incomeFragment);

                    Log.e("TAG", "income commit");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(sharedPreferences.contains("expenseFragment")){
        String jsonExpense = getSharedPreferences("Sharedprefs",Context.MODE_PRIVATE).getString("expenseFragment", "");
            try {
                    JSONArray expenseArray = new JSONArray(jsonExpense);
                    for(int i = 0; i< expenseArray.length(); i++){
                        JSONObject expenseObj = expenseArray.getJSONObject(i);
                        expenseFragment = ExpenseFragment.newInstance(expenseObj.getString("amount"), expenseObj.getString("description"));
                        fragmentTransaction.add(R.id.myLayout,expenseFragment);

                        Log.e("TAG", "expense commit");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                fragmentTransaction.commit();
            }



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
                sharedPreferences = MainActivity.this.getSharedPreferences("Sharedprefs",Context.MODE_PRIVATE);
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(incomeFragment);
                fragmentTransaction.remove(expenseFragment);
                fragmentTransaction.commit();
                sharedPreferences.edit().remove("incomeFragment").commit();
                sharedPreferences.edit().remove("expenseFragment").commit();
                incomeFragment = null;
                expenseFragment = null;
                incomeTodayTV.setText("0");
                expenseTodayTV.setText("0");
                fundsInWalletTV.setText("0");
            }
        });
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e("TAG", "onRestoreInstanceState called");
        if(savedInstanceState != null){
            fragmentManager = getFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            if(savedInstanceState.containsKey("incomeFragment")){
                incomeFragment = (IncomeFragment) fragmentManager.getFragment(savedInstanceState, "incomeFragment");
            }

        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("TAG", "onSaveInstanceState called");
        if(incomeFragment !=null){
            Log.e("TAG", "incomeFragment put in bundle");
            getFragmentManager().putFragment(outState, "incomeFragment", incomeFragment);
        }
        if(expenseFragment != null){
            getFragmentManager().putFragment(outState, "expenseFragment", expenseFragment);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        Integer totalFunds = new Integer(fundsInWalletTV.getText().toString());


        switch (resultCode){
            case AddActivity.INCOME_CODE:
                incomeFragment = IncomeFragment.newInstance(data.getStringExtra("amount"), data.getStringExtra("description"));
                fragmentTransaction.add(R.id.myLayout,incomeFragment);
                fragmentTransaction.commit();
                Log.e("TAG", "Income fragment");
                Integer currentIncome = new Integer(incomeTodayTV.getText().toString());
                Integer tempIncome = new Integer(data.getStringExtra("amount"));
                currentIncome += tempIncome;
                incomeTodayTV.setText(currentIncome.toString());
                totalFunds += tempIncome;
                fundsInWalletTV.setText(totalFunds.toString());
                break;
            case AddActivity.EXPENSE_CODE:
                expenseFragment = ExpenseFragment.newInstance(data.getStringExtra("amount"), data.getStringExtra("description"));
                fragmentTransaction.add(R.id.myLayout, expenseFragment);
                fragmentTransaction.commit();
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

    @Override
    protected void onPause() {
        super.onPause();
        sharedPreferences = MainActivity.this.getSharedPreferences("Sharedprefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String incomeKey = "incomeFragment";
        String expenseKey = "expenseFragment";
        String incomeValue = "";
        String expenseValue = "";
        JSONArray arrayIncome = new JSONArray();
        JSONArray arrayExpense = new JSONArray();
        JSONObject expenseObj = new JSONObject();
        JSONObject incomeOjb = new JSONObject();
        if(incomeFragment != null) {
            try {
                incomeOjb.put("amount", incomeFragment.amount.getText().toString());
                incomeOjb.put("description", incomeFragment.description.getText().toString());
                arrayIncome.put(incomeOjb);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(expenseFragment != null){
            try {
                expenseObj.put("amount", expenseFragment.amount.getText().toString());
                expenseObj.put("description", expenseFragment.description.getText().toString());
                arrayExpense.put(expenseObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        incomeValue = arrayIncome.toString();
        expenseValue = arrayExpense.toString();
        editor.putString(incomeKey, incomeValue);
        editor.putString(expenseKey, expenseValue);
        editor.putString("income", incomeTodayTV.getText().toString());
        editor.putString("expense", expenseTodayTV.getText().toString());
        editor.putString("wallet", fundsInWalletTV.getText().toString());
        editor.commit();
    }
}
