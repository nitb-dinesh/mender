package com.app.handyman.mender.misc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.app.handyman.mender.R;

/**
 * Activity to accept payments. Currently for testing only!
 */

public class PaymentActivity extends AppCompatActivity {


//    Button btnPay;
//    CardInputWidget mCardInputWidget;
//    Card cardToSave;
//    Stripe stripe;
//    Token mToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

//        stripe = new Stripe(PaymentActivity.this, "pk_live_h2KFD4idG8pcGvjmBAbmpzZF");
//
//        btnPay = (Button) findViewById(R.id.btnPay);
//        mCardInputWidget = (CardInputWidget) findViewById(R.id.card_input_widget);
//
//        btnPay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                cardToSave = mCardInputWidget.getCard();
//
//                if (cardToSave == null) {
//                    Toast.makeText(PaymentActivity.this, "Invalid Card Data", Toast.LENGTH_LONG).show();
//                } else {
//                    validateCard();
//                }
//
//            }
//        });


    }

//    public void validateCard() {
//        if (cardToSave.validateCard()) {
//
//            stripe.createToken(
//                    cardToSave,
//                    new TokenCallback() {
//                        public void onSuccess(Token token) {
//                            System.out.println(token.getCard());
//                            mToken = token;
//
//                            try {
//
//                                AsyncHttpClient client = new AsyncHttpClient();
//                                RequestParams params = new RequestParams();
//                                // params.put("method", "charge");
//                                // params.put("description", "Test");
//                                // params.put("customerId", customerId);
//                                // params.put("amount", "100");
//
//                                params.put("method", "charge");
//                                params.put("description", "Test");
//                                params.put("source", mToken.getId());
//                                params.put("amount", "100");
//
//                                // params.put("currency", "usd");
//
//                                client.post("http://54.70.113.238:7002/payWithCustomer/", params, new AsyncHttpResponseHandler() {
//                                    @Override
//                                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
//                                        System.out.println(Arrays.toString(headers) + " :::: " + statusCode);
//                                        Toast.makeText(PaymentActivity.this, "Payment Successful ", Toast.LENGTH_LONG).show();
//                                        btnPay.setVisibility(View.GONE);
//                                    }
//
//                                    @Override
//                                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
//                                        System.out.println(Arrays.toString(headers) + " :::: " + statusCode);
//                                        Toast.makeText(PaymentActivity.this, "Payment Declined ", Toast.LENGTH_LONG).show();
//                                    }
//                                });
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//
//                        public void onError(Exception error) {
//                            // Show localized error message
//                            Toast.makeText(PaymentActivity.this,
//                                    "Error",
//                                    Toast.LENGTH_LONG
//                            ).show();
//                        }
//                    }
//            );
//
//        }
//    }

}
