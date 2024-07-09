package com.masum.payment_getway

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.view.KeyEvent
import android.view.View.GONE
import android.view.View.VISIBLE
import android.webkit.SslErrorHandler
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import ninja.saad.bkashdemo.data.BkashPaymentRequest
import com.masum.payment_getway.util.JSInterface

class BkashPaymentActivity : AppCompatActivity() {

    private var paymentRequest = ""

    private lateinit var bkashWebView:WebView
    private lateinit var loadingProgressBar:ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bkash_payment)
        bkashWebView=findViewById(R.id.bkashWebView)
        loadingProgressBar=findViewById(R.id.loadingProgressBar)
        supportActionBar!!.title = "Bkash Payment"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        // url = intent.getStringExtra("url")!!.toString()
        val request =
            BkashPaymentRequest(intent.getStringExtra("amount"), intent.getStringExtra("intent"))
        paymentRequest = "{paymentRequest:${Gson().toJson(request)}}"

        initBkashWebView()
        initBkashWebViewClient(paymentRequest)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    if (bkashWebView.canGoBack()) {
                        bkashWebView.goBack()
                    } else {
                        showAlert()
                    }
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initBkashWebView() {
        bkashWebView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            cacheMode = WebSettings.LOAD_NO_CACHE
            allowFileAccessFromFileURLs = true
            allowUniversalAccessFromFileURLs = true
        }

        bkashWebView.apply {
            clearCache(true)
            isFocusableInTouchMode = true
            bkashWebView.addJavascriptInterface(
                JSInterface(this@BkashPaymentActivity),
                "AndroidNative"
            )
            //Get Url via Intent
            loadUrl("https://www.bkash.com")

        }
    }

    private fun initBkashWebViewClient(paymentRequest: String) {
        bkashWebView.webViewClient = object : WebViewClient() {

            @SuppressLint("WebViewClientOnReceivedSslError")
            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler,
                error: SslError?
            ) {
                handler.proceed()
            }

            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                loadingProgressBar.visibility = VISIBLE
                if (url == "https://www.bkash.com/terms-and-conditions") {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                    return true
                }
                return super.shouldOverrideUrlLoading(view, url)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                loadingProgressBar.visibility = VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                bkashWebView.let {
                    it.loadUrl("javascript:callReconfigure($paymentRequest )")
                    it.loadUrl("javascript:clickPayButton()")
                }

                loadingProgressBar.visibility = GONE


            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        showAlert()
    }

    private fun showAlert() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to cancel this payment?")
            .setCancelable(false)
            .setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialog, id ->
                    finish()
                })
            .setNegativeButton("No",
                DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
        val alert: AlertDialog = builder.create()
        alert.show()
    }

}