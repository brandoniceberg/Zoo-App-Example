package com.iceberg.zooapp

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_plan_a_trip.*
import java.lang.ref.WeakReference

class PlanATrip : AppCompatActivity() {

    private val BIO_TAG = "BIOMETRIC AUTH"
    private val PIN_TAG = "PIN AUTH"
    private val url = "https://2584.blackbaudhosting.com/2584/tickets?tab=3&txobjid=b1bdb2ed-6323-4f50-98b9-0e4c0c67a6d3"
    private val REQUEST_CODE = 534

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan_a_trip)

        val actionBar: ActionBar = supportActionBar!!
        actionBar.title = getString(R.string.title_activity_plan_a_visit)
        actionBar.setHomeAsUpIndicator(R.drawable.white_back_arrow)
        actionBar.setDisplayShowHomeEnabled(true)

        if (Build.VERSION.SDK_INT >= 16){
            val view = tzooImageView
            view.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            LinearLayout.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }

        //When purchase button is clicked
        purchaseButton.setOnClickListener {

            //Verify with biometrics before allowing user to purchase tickets
            val biometricManager = BiometricManager.from(this)
            when (biometricManager.canAuthenticate()) {
                BiometricManager.BIOMETRIC_SUCCESS -> {
                    Log.d(BIO_TAG, "Biometric hardware is available")
                    try {
                        biometricCheck()
                    }catch (e: Exception){
                        Log.d(BIO_TAG, e.message!!)
                    }
                }
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                    Log.e(BIO_TAG, "This device does not have biometric hardware.")
                    try {
                        authenticatePIN()
                    } catch (e: Exception) {
                        Log.e(PIN_TAG, e.message!!)
                    }
                }
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                    Log.e(BIO_TAG, "Biometric hardware not currently available.")
                }
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                    Log.e(BIO_TAG, "This device has no biometrics enrolled")
                    //Inform user that there is no registered biometric credentials
                    try {
                        authenticatePIN()
                    } catch (e: Exception) {
                        Log.e(PIN_TAG, e.message!!)
                    }
                }
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home ->{
                onBackPressed()
            }
        }
        return true
    }

    private fun authenticatePIN () {
        val km: KeyguardManager = this.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (Build.VERSION.SDK_INT >= 23 && km.isDeviceSecure) {
            val authIntent: Intent = km.createConfirmDeviceCredentialIntent("Just making sure it's you", "Enter device PIN to make a purchase")
            startActivityForResult(authIntent, REQUEST_CODE)
        } else {
            //Device has biometric capabilities; suggest user to user to user a PIN or biometric unlock
            Toast.makeText(this, "Please consider setting up a device PIN or Biometric option for a more secure purchasing experience.", Toast.LENGTH_LONG).show()
            loadWebPage()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                Log.d(PIN_TAG, "PIN authentication succeeded")
                loadWebPage()
            } else -> {
            Log.d(PIN_TAG, "REQUEST CODE did not match")
        }
        }
    }


    private fun biometricCheck() {
        val biometricPrompt = BiometricPrompt(this, ContextCompat.getMainExecutor(this), object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                //Biometric Approved load ticket WebPage
                loadWebPage()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Log.d(BIO_TAG, "Biometric authentication failed")
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Log.d(BIO_TAG, "An biometric authentication error has occurred")
            }


        })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Just making sure it's you")
            .setSubtitle("Verify to make a purchase")
            .setDeviceCredentialAllowed(true)
            .setConfirmationRequired(false)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun loadWebPage() {
        val builder: CustomTabsIntent.Builder = CustomTabsIntent.Builder()
        val customTabsIntent: CustomTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }

}
