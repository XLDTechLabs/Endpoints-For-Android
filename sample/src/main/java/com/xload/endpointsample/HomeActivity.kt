package com.xload.endpointsample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.xload.endpointsforandroid.api.models.LinkedWallet
import com.xload.endpointsforandroid.api.models.WalletStatus
import com.xload.endpointsforandroid.modules.XLD
import com.xload.endpointsforandroid.utils.OnXLDListener
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    var xldUserId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setClickListener()
    }

    private fun setClickListener() {
        // set sample data
        etConversionAppId.setText(Constants.APP_ID)
        etAppId.setText(Constants.APP_ID)
        etPartnerId.setText(Constants.SAMPLE_PARTNER_ID)
        etWalletAddress.setText(Constants.SAMPLE_WALLET_ADDRESS)
        etOtp.setText(Constants.SAMPLE_OTP.toString())

        /**
         * Get converstion sample
         */
        btnGetConversion.setOnClickListener {
            getConversion()
        }

        /**
         * Get wallet status
         */
        btnGetWalletStatus.setOnClickListener {
            getWalletStatus()
        }

        /**
         * Link account sample
         */
        btnLinkAccount.setOnClickListener {
           linkedAccount()
        }

    }

    /**
     * Linked wallet account
     */
    private fun linkedAccount() {
        val walletAddress = etWalletAddress.text.toString()
        val otp = etOtp.text.toString().toInt()
        val partnerId = etPartnerId.text.toString()
        val appId: String = etAppId.text.toString()

        when {
            etAppId.text.toString().isEmpty() -> {
                etAppId.setError("App ID cannot be empty")
                return
            }
            etWalletAddress.text.toString().isEmpty() -> {
                etWalletAddress.setError("Wallet address cannot be empty")
                return
            }
            etOtp.text.toString().isEmpty() -> {
                etOtp.setError("OTP cannot be empty")
                return
            }
            etPartnerId.text.isEmpty() -> {
                etPartnerId.setError("Partner user id cannot be empty")
                return
            }
            else -> {
                submitLinkedWallet()
                XLD.getInstance(Constants.KEY, Constants.SECRET, true).linkWallet(
                    appId = appId,
                    walletAddress = walletAddress,
                    otp = otp,
                    partnerUserId = partnerId,
                    listener = object : OnXLDListener<LinkedWallet> {
                        override fun loading(isLoading: Boolean) {
                        }

                        override fun error(error: String?) {
                            startLoading.visibility = View.GONE
                            showMessage(error ?: "")
                        }

                        override fun success(result: LinkedWallet?) {
                            println("DEBUG: LinkWallet = $result")
                            xldUserId = result?.xldUserId

                            etStatusUserId.setText(result?.xldUserId)
                            etConversionAppId.setText(result?.appId)

                            startLoading.visibility = View.GONE
                            llAppStatus.visibility = View.VISIBLE

                            val response = StringBuilder("Wallet\n")
                                .append("appId=${result?.appId}\n")
                                .append("isLinked=${result?.linked}\n")
                                .append("otp=${result?.otp}\n")
                                .append("partnerId=${result?.partnerUserId}\n")
                                .append("walletAddress=${result?.walletAddress}\n")
                                .append("xldUserId=${result?.xldUserId}\n")
                                .toString()
                            showMessage(response)
                        }

                    }
                )
            }
        }
    }

    /**
     * Get wallet status by app ID
     */
    private fun getWalletStatus() {
        if (etStatusUserId.text.toString().isEmpty()) {
            etStatusUserId.setError("XLD User ID cannot be empty.")
            return
        } else {
            startLoading.visibility = View.VISIBLE

            val userId = etStatusUserId.text.toString()
            XLD.getInstance(
                Constants.KEY,
                Constants.SECRET,
                true
            ).getWalletStatus(userId,
                object : OnXLDListener<WalletStatus> {
                    override fun loading(isLoading: Boolean) {
                    }

                    override fun error(error: String?) {
                        startLoading.visibility = View.GONE
                        showMessage(error ?: "")
                    }

                    override fun success(result: WalletStatus?) {
                        startLoading.visibility = View.GONE
                        val msg = StringBuilder("Wallet status:\n")
                            .append("isLinked=${result?.linked}\n")
                            .append("partnerUserId=${result?.partnerUserId}\n")
                            .append("xldUserId=${result?.xldUserId}\n")
                        showMessage(msg.toString())
                    }

                }
            )
        }
    }

    private fun getConversion() {
        if (etConversionAppId.text.toString().isEmpty()) {
            etConversionAppId.setError("App ID cannot be empty.")
            return
        } else {
            showGetConversionLoading()

            val appId = etConversionAppId.text.toString()
            XLD.getInstance(
                Constants.KEY,
                Constants.SECRET,
                true
            ).getConversion(appId, object : OnXLDListener<Double> {
                override fun loading(isLoading: Boolean) {
                }

                override fun error(error: String?) {
                    startLoading.visibility = View.GONE
                    showMessage(error ?: "")
                }

                override fun success(result: Double?) {
                    startLoading.visibility = View.VISIBLE
                    val result = StringBuilder("Conversion\n")
                        .append("conversion = ${result}")
                        .toString()
                    showMessage(result)
                }

            })
        }
    }

    private fun submitLinkedWallet() {
        llAppStatus.visibility = View.GONE
        startLoading.visibility = View.VISIBLE
    }

    private fun showMessage(msg: String) {
        tvMessage.setText(msg)
    }

    private fun showGetConversionLoading() {
        startLoading.visibility = View.VISIBLE
        if (etStatusUserId.text.toString().isEmpty()) {
            llAppStatus.visibility = View.GONE
        }
    }
}
