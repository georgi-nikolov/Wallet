package com.example.xcomputers.wallet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {
    EditText amountTV;
    EditText descriptionTV;
    Button doneButton;
    RadioGroup group;
    RadioButton income;
    RadioButton expense;
    String amount;
    String description;
    final  static int EXPENSE_CODE = 1;
    final static int INCOME_CODE = 2;
    boolean isIncome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        amountTV = (EditText) findViewById(R.id.amountTV);
        descriptionTV =(EditText) findViewById(R.id.descriptionTV);
        doneButton = (Button) findViewById(R.id.addPageDoneButton);
        group = (RadioGroup) findViewById(R.id.radioGroup);
        income = (RadioButton) findViewById(R.id.incomeRadio);
        expense = (RadioButton) findViewById(R.id.expenseRadio);
        isIncome = true;

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = amountTV.getText().toString();
                description = descriptionTV.getText().toString();
                if(amount.isEmpty()){
                    Toast.makeText(AddActivity.this, "Please specify an amount", Toast.LENGTH_SHORT).show();
                    amountTV.requestFocus();
                    return;
                }
                else if(description.isEmpty()){
                    Toast.makeText(AddActivity.this, "Please specify description", Toast.LENGTH_SHORT).show();
                    descriptionTV.requestFocus();
                    return;
                }
                else{
                    Intent data = new Intent();
                    data.putExtra("amount", amount);
                    data.putExtra("description", description);
                    if(isIncome){
                        setResult(INCOME_CODE, data);
                        Log.e("TAG", "INCOME_CODE");
                        finish();
                    }
                    else{
                        setResult(EXPENSE_CODE, data);
                        Log.e("TAG", "EXPENSE_CODE");
                        finish();
                    }
                }
            }
        });

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.incomeRadio:
                if (checked)
                    Log.e("TAG","income_Radio_Checked");
                    isIncome = true;
                    break;
            case R.id.expenseRadio:
                if (checked)
                    isIncome = false;
                Log.e("TAG","expense_Radio_Checked");
                    break;
        }
    }
}
