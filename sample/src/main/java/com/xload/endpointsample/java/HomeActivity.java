package com.xload.endpointsample.java;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xload.endpointsample.Constants;
import com.xload.endpointsample.R;
import com.xload.endpointsforandroid.api.models.LinkedWallet;
import com.xload.endpointsforandroid.api.models.WalletStatus;
import com.xload.endpointsforandroid.modules.XLD;
import com.xload.endpointsforandroid.utils.OnXLDListener;

import static java.sql.DriverManager.println;

/**
 * @author John Paul Cas
 * Copyright (c) 2020 XLD Tech Labs. All rights reserved.
 */
public class HomeActivity extends AppCompatActivity {

    private String xldUserId = null;

    private ProgressBar startLoading;
    private Button btnGetConversion;
    private Button btnGetWalletStatus;
    private Button btnLinkAccount;
    private LinearLayout linkWalletContainer;
    private LinearLayout llAppStatus;
    private TextView tvMessage;

    private EditText etAppId;
    private EditText etPartnerId;
    private EditText etWalletAddress;
    private EditText etOtp;
    private EditText etStatusUserId;
    private EditText etConversionAppId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        startLoading = findViewById(R.id.startLoading);
        btnGetConversion = findViewById(R.id.btnGetConversion);
        btnGetWalletStatus = findViewById(R.id.btnGetWalletStatus);
        btnLinkAccount = findViewById(R.id.btnLinkAccount);
        linkWalletContainer = findViewById(R.id.linkWalletContainer);
        llAppStatus = findViewById(R.id.llAppStatus);
        tvMessage = findViewById(R.id.tvMessage);

        etAppId = findViewById(R.id.etAppId);
        etStatusUserId = findViewById(R.id.etStatusUserId);
        etPartnerId = findViewById(R.id.etPartnerId);
        etWalletAddress = findViewById(R.id.etWalletAddress);
        etOtp = findViewById(R.id.etOtp);
        etConversionAppId = findViewById(R.id.etConversionAppId);

        setClickListener();
    }

    private void setClickListener() {
        // set sample data
        etAppId.setText(Constants.APP_ID);
        etPartnerId.setText(Constants.SAMPLE_PARTNER_ID);
        etWalletAddress.setText(Constants.SAMPLE_WALLET_ADDRESS);
        etOtp.setText(String.valueOf(Constants.SAMPLE_OTP));

        // Get conversion sample
        btnGetConversion.setOnClickListener(v -> {
            getConversion();
        });

        /**
         * Get wallet status
         */
        btnGetWalletStatus.setOnClickListener(v -> {
           getWalletStatus();
        });

        btnLinkAccount.setOnClickListener(v -> {
            linkedAccount();
        });
    }

    /**
     * Linked wallet account
     */
    private void linkedAccount() {
        String appId = etAppId.getText().toString();
        String walletAddress = etWalletAddress.getText().toString();
        int otp = Integer.parseInt(etOtp.getText().toString());
        String partnerId = etPartnerId.getText().toString();

        if (etAppId.getText().toString().isEmpty()) {
            etWalletAddress.setError("App ID cannot be empty");
        } else if (etWalletAddress.getText().toString().isEmpty()) {
            etWalletAddress.setError("Wallet address cannot be empty");
        } else if (etOtp.getText().toString().isEmpty()) {
            etOtp.setError("OTP cannot be empty");
        } else if (etPartnerId.getText().toString().isEmpty()) {
            etPartnerId.setError("Partner user id cannot be empty");
        } else {
            submitLinkedWallet();
            XLD.getInstance(
                    Constants.KEY,
                    Constants.SECRET,
                    true /*development mode*/
            ).linkWallet(
                    appId,
                    walletAddress,
                    otp,
                    partnerId,
                    new OnXLDListener<LinkedWallet>() {
                        @Override
                        public void loading(boolean isLoading) {
                        }

                        @Override
                        public void error(@org.jetbrains.annotations.Nullable String error) {
                            startLoading.setVisibility(View.GONE);
                            showMessage(error);
                        }

                        @Override
                        public void success(@org.jetbrains.annotations.Nullable LinkedWallet result) {
                            xldUserId = result.getXldUserId();

                            etStatusUserId.setText(result.getXldUserId());
                            etConversionAppId.setText(result.getAppId());

                            startLoading.setVisibility(View.GONE);
                            llAppStatus.setVisibility(View.VISIBLE);

                            String response = new StringBuilder("Wallet\n")
                                    .append("appId=").append(result.getAppId()).append("\n")
                                    .append("isLinked=").append(result.getLinked()).append("\n")
                                    .append("otp=").append(result.getOtp()).append("\n")
                                    .append("partnerId=").append(result.getPartnerUserId()).append("\n")
                                    .append("walletAddress=").append(result.getWalletAddress()).append("\n")
                                    .append("xldUserId=").append(result.getXldUserId()).append("\n")
                                    .toString();
                            showMessage(response);
                        }
                    }
            );
        }
    }

    /**
     * Get wallet status
     */
    private void getWalletStatus() {
        if (etStatusUserId.getText().toString().isEmpty()) {
            etStatusUserId.setError("XLD User ID cannot be empty.");
            return;
        } else {
            startLoading.setVisibility(View.VISIBLE);

            String userId = etStatusUserId.getText().toString();
            XLD.getInstance(
                    Constants.KEY,
                    Constants.SECRET,
                    true /*development mode*/
            ).getWalletStatus(
                    userId,
                    new OnXLDListener<WalletStatus>() {
                        @Override
                        public void loading(boolean isLoading) {

                        }

                        @Override
                        public void error(@org.jetbrains.annotations.Nullable String error) {
                            startLoading.setVisibility(View.GONE);
                            showMessage(error);
                        }

                        @Override
                        public void success(@org.jetbrains.annotations.Nullable WalletStatus result) {
                            startLoading.setVisibility(View.GONE);
                            String msg = new StringBuilder("Wallet status:\n")
                                    .append("isLinked=").append(result.getLinked()).append("\n")
                                    .append("partnerUserId=").append(result.getPartnerUserId()).append("\n")
                                    .append("xldUserId=").append(result.getXldUserId()).toString();
                            showMessage(msg);
                        }
                    }
            );
        }
    }

    private void getConversion() {
        if (etConversionAppId.getText().toString().isEmpty()) {
            etConversionAppId.setError("App ID cannot be empty.");
            return;
        } else {
            showGetConversionLoading();

            String appId = etConversionAppId.getText().toString();
            XLD.getInstance(
                    Constants.KEY,
                    Constants.SECRET,
                    true
            ).getConversion(appId, new OnXLDListener<Double>() {
                @Override
                public void loading(boolean isLoading) {

                }

                @Override
                public void error(@org.jetbrains.annotations.Nullable String error) {
                    startLoading.setVisibility(View.GONE);
                    showMessage(error);
                }

                @Override
                public void success(@org.jetbrains.annotations.Nullable Double result) {
                    startLoading.setVisibility(View.GONE);
                    String msg = new StringBuilder("Conversion\n")
                            .append("conversion=").append(result.toString())
                            .toString();
                    showMessage(msg);
                }
            });
        }
    }

    private void submitLinkedWallet() {
        llAppStatus.setVisibility(View.GONE);
        startLoading.setVisibility(View.VISIBLE);
    }

    private void showMessage(String msg) {
        tvMessage.setText(msg);
    }

    private void showGetConversionLoading() {
        startLoading.setVisibility(View.VISIBLE);
        if (etStatusUserId.getText().toString().isEmpty()) {
            llAppStatus.setVisibility(View.GONE);
        }
    }

}
