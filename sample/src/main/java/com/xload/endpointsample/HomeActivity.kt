package com.xload.endpointsample

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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
                println("DEBUG: isLoading from sample app = ${isLoading}")
            }

            override fun error(error: String?) {
                println("DEBUG: error from sample app = ${error}")
            }

            override fun success(key: String) {
                // please save this key somewhere
                userKey = key
                showGetConversion()
                println("DEBUG: key from sample app = ${key}")
            }

        })
    }

    private fun setClickListener() {
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
                    btnGetConversion.visibility = View.VISIBLE
                    showMessage(error?: "")
                }

                override fun success(result: Double?) {
                    showGetConversion()
                    showMessage("conversion = ${result}")
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
                            println("walletStatus = $result")
                            partnerId = result?.partnerUserId
                            showMessage("Partner user id: ${partnerId}")
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
            val partnerUserId = etPartnerUserId.text.toString()
            userKey?.let {xldUserId ->
                XLD.getInstance(Constants.KEY, Constants.SECRET).linkWallet(
                    xldUserId = xldUserId,
                    xldWalletAddress = "",
                    xldOtp = "",
                    partnerUserId = partnerUserId,
                    listener = object : OnXLDListener<LinkedWallet> {
                        override fun loading(isLoading: Boolean) {

                        }

                        override fun error(error: String?) {
                        }

                        override fun success(result: LinkedWallet?) {
                            println("LinkWallet = $result")
                        }

                    }
                )
            }
        }

        btnCopyPartnerId.setOnClickListener {
            partnerId?.let {partnerId
                val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Copied", partnerId)
                clipboard.setPrimaryClip(clip)
            }

        }
    }

    private fun showLinkedWalletContainer() {
        startLoading.visibility = View.GONE
        btnGetConversion.visibility = View.GONE
        btnRetryStart.visibility = View.GONE
        tvMessage.visibility = View.GONE
        btnLinkAccount.visibility = View.VISIBLE
        linkWalletContainer.visibility = View.VISIBLE
        btnCopyPartnerId.visibility = View.VISIBLE
    }

    private fun showLoading() {
        startLoading.visibility = View.VISIBLE
        btnGetConversion.visibility = View.GONE
        btnRetryStart.visibility = View.GONE
        tvMessage.visibility = View.GONE
        btnLinkAccount.visibility = View.GONE
        linkWalletContainer.visibility = View.GONE
    }

    private fun showGetConversion() {
        startLoading.visibility = View.GONE
        btnRetryStart.visibility = View.GONE
        btnGetConversion.visibility = View.VISIBLE
        btnLinkAccount.visibility = View.VISIBLE
        tvMessage.visibility = View.VISIBLE
        linkWalletContainer.visibility = View.GONE
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
