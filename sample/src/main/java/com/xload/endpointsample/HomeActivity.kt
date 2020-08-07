package com.xload.endpointsample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.xload.endpointsforandroid.api.models.LinkedWallet
import com.xload.endpointsforandroid.api.models.WalletStatus
import com.xload.endpointsforandroid.modules.XLD
import com.xload.endpointsforandroid.utils.OnXLDListener
import com.xload.endpointsforandroid.utils.OnXLDStartListener
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    // xldUserId
    var userKey: String? = null

    // partnerId
    var partnerId: String? = null

    lateinit var uniqueId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setClickListener()

        uniqueId = intent.getStringExtra(Constants.UNIQUE_ID)
        println("DEBUG: HomeActivity = ${uniqueId}")

        startRequest(uniqueId)
    }

    private fun startRequest(uniqueId: String) {
        // get the userKey
        XLD.getInstance(
            key = Constants.KEY,
            secret = Constants.SECRET
        ).start(uniqueId, Constants.SUPER_SPIN_APP_ID, object: OnXLDStartListener {
            override fun loading(isLoading: Boolean) {
                showLoading()
            }

            override fun error(error: String?) {
                showMessage(error ?: "")
            }

            override fun success(key: String) {
                // please save this key somewhere
                userKey = key
                showLinkedWalletContainer()
            }

        })
    }

    private fun setClickListener() {
        // set sample data
        etPartnerUserId.setText(Constants.SAMPLE_PARTNER_ID)
        etWalletAddress.setText(Constants.SAMPLE_WALLET_ADDRESS)
        etOtp.setText(Constants.SAMPLE_OTP.toString())


        btnRetryStart.setOnClickListener {
            showLoading()
            startRequest(uniqueId)
        }

        /**
         * Get converstion sample
         */
        btnGetConversion.setOnClickListener {
            XLD.getInstance(
                Constants.KEY,
                Constants.SECRET,
                true
            ).getConversion(Constants.SUPER_SPIN_APP_ID, object : OnXLDListener<Double> {
                override fun loading(isLoading: Boolean) {
                    showLoading()
                }

                override fun error(error: String?) {
                    showMessage(error?: "")
                    btnGetConversion.visibility = View.VISIBLE
                }

                override fun success(result: Double?) {
                    showMessage("conversion = ${result}")
                    showGetConversion()
                }

            })
        }

        /**
         * Get wallet status
         */
        btnGetWalletStatus.setOnClickListener {
            userKey?.let {xldUserId ->
                XLD.getInstance(Constants.KEY, Constants.SECRET).getWalletStatus(
                    xldUserId,
                    object : OnXLDListener<WalletStatus> {
                        override fun loading(isLoading: Boolean) {
                            btnGetConversion.visibility = View.VISIBLE
                            showLoading()
                        }

                        override fun error(error: String?) {
                            showMessage(error ?: "")
                            btnGetConversion.visibility = View.VISIBLE
                        }

                        override fun success(result: WalletStatus?) {
                            showMessage(result.toString())
                            showLinkedWalletContainer()
                        }

                    }
                )
            }
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
        val partnerId = etPartnerUserId.text.toString()

        when {
            etWalletAddress.text.toString().isEmpty() -> {
                etWalletAddress.setError("Wallet address cannot be empty")
                return
            }
            etOtp.text.toString().isEmpty() -> {
                etOtp.setError("OTP cannot be empty")
                return
            }
            etPartnerUserId.text.isEmpty() -> {
                etPartnerUserId.setError("Partner user id cannot be empty")
                return
            }
            else -> userKey?.let { xldUserId ->
                XLD.getInstance(Constants.KEY, Constants.SECRET).linkWallet(
                    xldUserId = xldUserId,
                    xldWalletAddress = walletAddress,
                    xldOtp = otp,
                    partnerUserId = partnerId,
                    listener = object : OnXLDListener<LinkedWallet> {
                        override fun loading(isLoading: Boolean) {
                            showLoading()
                        }

                        override fun error(error: String?) {
                            showMessage(error ?: "")
                            showLinkedWalletContainer()
                        }

                        override fun success(result: LinkedWallet?) {
                            println("DEBUG: LinkWallet = $result")
                            showMessage("linkedwallet = $result")
                            showLinkedWalletContainer()
                        }

                    }
                )
            }
        }


    }

    private fun showLinkedWalletContainer() {
        startLoading.visibility = View.GONE
        btnRetryStart.visibility = View.GONE
        btnGetConversion.visibility = View.VISIBLE
        btnGetWalletStatus.visibility = View.VISIBLE
        tvMessage.visibility = View.VISIBLE
        btnLinkAccount.visibility = View.VISIBLE
        linkWalletContainer.visibility = View.VISIBLE
    }

    private fun showLoading() {
        startLoading.visibility = View.VISIBLE
        btnGetConversion.visibility = View.GONE
        btnGetWalletStatus.visibility = View.GONE
        btnRetryStart.visibility = View.GONE
        tvMessage.visibility = View.GONE
        btnLinkAccount.visibility = View.GONE
        linkWalletContainer.visibility = View.GONE
    }

    private fun showGetConversion() {
        startLoading.visibility = View.GONE
        btnRetryStart.visibility = View.GONE
        btnGetConversion.visibility = View.VISIBLE
        btnGetWalletStatus.visibility = View.VISIBLE
        btnLinkAccount.visibility = View.VISIBLE
        tvMessage.visibility = View.VISIBLE
        linkWalletContainer.visibility = View.VISIBLE
    }

    private fun showMessage(string: String) {
        startLoading.visibility = View.GONE
        tvMessage.visibility = View.VISIBLE
        tvMessage.setText(string)
        if (userKey.isNullOrBlank()) {
            btnRetryStart.visibility = View.VISIBLE
            btnGetConversion.visibility = View.GONE
            btnLinkAccount.visibility = View.GONE
            startLoading.visibility = View.GONE
        }
    }
}
