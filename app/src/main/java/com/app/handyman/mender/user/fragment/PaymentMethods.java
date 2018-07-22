package com.app.handyman.mender.user.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.handyman.mender.R;

public class PaymentMethods extends AppCompatActivity {

    /*
    * https://github.com/anuj4994/stripeRestApiPython/blob/master/stripeRest.py
    * Method Names
    * 1. post - To Make a Payment
    * 2. saveCustomer
    * 3. payWithCustomer
    * 4. createConnectAccount
    * 5. connectCharge
    * 6. retrieveCustomer
    * 7. changeDefaultCard
    * 8. createCard
    * 9. retrieveCard
    * 10. deleteCard
    * 11. retrieveAllCards
    * */

    private Toolbar toolbar;
    private TextView toolbar_title;

    private Button mAddCard, mDeleteCard, mChangeDefaultCard;

    private RecyclerView mReclist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_methods_2);

        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setTypeface(myCustomFont);

        toolbar_title.setText("Payment Methods");

        mAddCard = (Button) findViewById(R.id.add_card);
        mDeleteCard = (Button) findViewById(R.id.delete_card);
        mChangeDefaultCard = (Button) findViewById(R.id.change_default_card);

        mReclist = (RecyclerView) findViewById(R.id.cards_list);


//        try {
//
//            AsyncHttpClient client = new AsyncHttpClient();
//            RequestParams params = new RequestParams();
//            // params.put("method", "charge");
//            // params.put("description", "Test");
//            // params.put("customerId", customerId);
//            // params.put("amount", "100");
//
//            params.put("method", "charge");
//            params.put("description", "Test");
//            params.put("source", mToken.getId());
//            params.put("amount", "100");
//
//            // params.put("currency", "usd");
//
//            client.post("http://54.70.113.238:7002/payWithCustomer/", params, new AsyncHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
//                    System.out.println(Arrays.toString(headers) + " :::: " + statusCode);
//                    Toast.makeText(PaymentActivity.this, "Payment Successful ", Toast.LENGTH_LONG).show();
//                    btnPay.setVisibility(View.GONE);
//                }
//
//                @Override
//                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
//                    System.out.println(Arrays.toString(headers) + " :::: " + statusCode);
//                    Toast.makeText(PaymentActivity.this, "Payment DeclinedActivity ", Toast.LENGTH_LONG).show();
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

        mAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                class createCard(Resource):
//                def post(self):
//                customerId = request.form["customerId"]
//                source = request.form["source"]
//                customer = stripe.Customer.retrieve(customerId)
//                card = customer.sources.create(source="tok_amex")
//                return card

            }
        });

        mDeleteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                class deleteCard(Resource):
//                def post(self):
//                customerId = request.form["customerId"]
//                cardId = request.form["cardId"]
//                customer = stripe.Customer.retrieve(customerId)
//                response = customer.sources.retrieve(cardId).delete()
//                return response

            }
        });

        mChangeDefaultCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                class changeDefaultCard(Resource):
//                def post(self):
//                customerId = request.form["customerId"]
//                source = request.form["source"]
//                cu = stripe.Customer.retrieve(customerId)
//                cu.default_source = source
//                cu.save()

            }
        });

        retriveAllCards();

    }

    private void retriveAllCards() {

//        class retriveAllCards(Resource):
//        def post(self):
//        customerId = request.form["customerId"]
//        index = request.form["index"]
//        if index is None:
//        index = 0
//        return stripe.Customer.retrieve(customerId).sources.all(limit=3, object='card',  starting_after = index)

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

//        Intent intent = new Intent(PaymentMethods.this, HomeActivity.class);
//        startActivity(intent);

    }
}
