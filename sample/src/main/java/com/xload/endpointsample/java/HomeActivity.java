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
import com.xload.endpointsforandroid.utils.OnXLDStartListener;

import org.jetbrains.annotations.NotNull;

/**
 * @author John Paul Cas
 * Copyright (c) 2020 XLD Tech Labs. All rights reserved.
 */
public class HomeActivity extends AppCompatActivity {

    private String userKey = null;
    private String uniqueId;

    private ProgressBar startLoading;
    private Button btnRetryStart;
    private Button btnGetConversion;
    private Button btnGetWalletStatus;
    private Button btnLinkAccount;
    private LinearLayout linkWalletContainer;
    private TextView tvMessage;

    private EditText etPartnerUserId;
    private EditText etWalletAddress;
    private EditText etOtp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        startLoading = findViewById(R.id.startLoading);
        btnRetryStart = findViewById(R.id.btnRetryStart);
        btnGetConversion = findViewById(R.id.btnGetConversion);
        btnGetWalletStatus = findViewById(R.id.btnGetWalletStatus);
        btnLinkAccount = findViewById(R.id.btnLinkAccount);
        linkWalletContainer = findViewById(R.id.linkWalletContainer);
        tvMessage = findViewById(R.id.tvMessage);

        etPartnerUserId = findViewById(R.id.etPartnerUserId);
        etWalletAddress = findViewById(R.id.etWalletAddress);
        etOtp = findViewById(R.id.etOtp);

        setClickListener();

        uniqueId = getIntent().getStringExtra(Constants.UNIQUE_ID);
        startRequest(uniqueId);
    }

    // step 1
    private void startRequest(String uniqueId) {
        // get the userKey
        XLD.getInstance(
                Constants.KEY,
                Constants.SECRET,
                true /*development mode*/
        ).start(uniqueId, Constants.SUPER_SPIN_APP_ID, new OnXLDStartListener() {
            @Override
            public void loading(boolean isLoading) {
                showLoading();
            }

            @Override
            public void error(@org.jetbrains.annotations.Nullable String error) {
                showMessage(error);
            }

            @Override
            public void success(@NotNull String key) {
                userKey = key;
                showLinkedWalletContainer();
            }
        });
    }

    private void setClickListener() {
        // set sample data
        etPartnerUserId.setText(Constants.SAMPLE_PARTNER_ID);
        etWalletAddress.setText(Constants.SAMPLE_WALLET_ADDRESS);
        etOtp.setText(String.valueOf(Constants.SAMPLE_OTP));

        btnRetryStart.setOnClickListener(v -> {
            showLoading();
            startRequest(uniqueId);
        });

        // Get conversion sample
        btnGetConversion.setOnClickListener(v -> {
            XLD.getInstance(
                    Constants.KEY,
                    Constants.SECRET,
                    true
            ).getConversion(Constants.SUPER_SPIN_APP_ID, new OnXLDListener<Double>() {
                @Override
                public void loading(boolean isLoading) {
                    showLoading();
                }

                @Override
                public void error(@org.jetbrains.annotations.Nullable String error) {
                    showMessage(error);
                    btnGetConversion.setVisibility(View.VISIBLE);
                }

                @Override
                public void success(@org.jetbrains.annotations.Nullable Double result) {
                    showMessage("Conversion = " + result.toString());
                    showGetConversion();
                }
            });
        });

        /**
         * Get wallet status
         */
        btnGetWalletStatus.setOnClickListener(v -> {
            if (userKey != null) {
                XLD.getInstance(
                        Constants.KEY,
                        Constants.SECRET,
                        true /*development mode*/
                ).getWalletStatus(
                        userKey,
                        new OnXLDListener<WalletStatus>() {
                            @Override
                            public void loading(boolean isLoading) {
                                btnGetConversion.setVisibility(View.VISIBLE);
                                showLoading();
                            }

                            @Override
                            public void error(@org.jetbrains.annotations.Nullable String error) {
                                showMessage(error);
                                btnGetConversion.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void success(@org.jetbrains.annotations.Nullable WalletStatus result) {
                                showMessage(result.toString());
                                showLinkedWalletContainer();
                            }
                        }
                );
            }
        });

        btnLinkAccount.setOnClickListener(v -> {
            linkedAccount();
        });
    }

    /**
     * Linked wallet account
     */
    private void linkedAccount() {
        String walletAddress = etWalletAddress.getText().toString();
        int otp = Integer.parseInt(etOtp.getText().toString());
        String partnerId = etPartnerUserId.getText().toString();

        if (etWalletAddress.getText().toString().isEmpty()) {
            etWalletAddress.setError("Wallet address cannot be empty");
        } else if (etOtp.getText().toString().isEmpty()) {
            etOtp.setError("OTP cannot be empty");
        } else if (etPartnerUserId.getText().toString().isEmpty()) {
            etPartnerUserId.setError("Partner user id cannot be empty");
        } else if (userKey != null) {
            XLD.getInstance(
                    Constants.KEY,
                    Constants.SECRET,
                    true /*development mode*/
            ).linkWallet(
                    userKey,
                    walletAddress,
                    otp,
                    partnerId,
                    new OnXLDListener<LinkedWallet>() {
                        @Override
                        public void loading(boolean isLoading) {
                            showLoading();
                        }

                        @Override
                        public void error(@org.jetbrains.annotations.Nullable String error) {
                            showMessage(error);
                            showLinkedWalletContainer();
                        }

                        @Override
                        public void success(@org.jetbrains.annotations.Nullable LinkedWallet result) {
                            showMessage("linkwallet " + result);
                            showLinkedWalletContainer();
                        }
                    }
            );
        }
    }


    private void showLinkedWalletContainer() {
        startLoading.setVisibility(View.GONE);
        btnRetryStart.setVisibility(View.GONE);
        btnGetConversion.setVisibility(View.VISIBLE);
        btnGetWalletStatus.setVisibility(View.VISIBLE);
        tvMessage.setVisibility(View.VISIBLE);
        btnLinkAccount.setVisibility(View.VISIBLE);
        linkWalletContainer.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        startLoading.setVisibility(View.VISIBLE);
        btnGetConversion.setVisibility(View.GONE);
        btnGetWalletStatus.setVisibility(View.GONE);
        btnRetryStart.setVisibility(View.GONE);
        tvMessage.setVisibility(View.GONE);
        btnLinkAccount.setVisibility(View.GONE);
        linkWalletContainer.setVisibility(View.GONE);
    }

    private void showGetConversion() {
        startLoading.setVisibility(View.GONE);
        btnRetryStart.setVisibility(View.GONE);
        btnGetConversion.setVisibility(View.VISIBLE);
        btnGetWalletStatus.setVisibility(View.VISIBLE);
        btnLinkAccount.setVisibility(View.VISIBLE);
        tvMessage.setVisibility(View.VISIBLE);
        linkWalletContainer.setVisibility(View.VISIBLE);
    }

    private void showMessage(String message) {
        startLoading.setVisibility(View.GONE);
        tvMessage.setVisibility(View.VISIBLE);
        tvMessage.setText(message);
        if (userKey == null) {
            btnRetryStart.setVisibility(View.VISIBLE);
            btnGetConversion.setVisibility(View.GONE);
            btnLinkAccount.setVisibility(View.GONE);
            startLoading.setVisibility(View.GONE);
        }
    }
}
