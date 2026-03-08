package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val share = findViewById<TextView>(R.id.share)
        share.setOnClickListener {
            shareApp()
        }

        val support = findViewById<TextView>(R.id.support)
        support.setOnClickListener {
            sendEmailToSupport()
        }

        val agreement = findViewById<TextView>(R.id.agreement)
        agreement.setOnClickListener {
            val offerUrl = getString(R.string.offerUrl)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(offerUrl))
            startActivity(intent)
        }
    }

    private fun shareApp() {

        val shareText = getString(R.string.shareText)

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        val shareVia = getString(R.string.share_via)

        if (shareIntent.resolveActivity(packageManager) != null) {
            startActivity(Intent.createChooser(shareIntent, shareVia))
        } else {
            Toast.makeText(this, R.string.toastText, Toast.LENGTH_SHORT).show()
        }

    }

    private fun sendEmailToSupport() {

        val recipientEmail = getString(R.string.recipientEmail)
        val subject = getString(R.string.subject)
        val body = getString(R.string.body)

        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipientEmail))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }

        if (emailIntent.resolveActivity(packageManager) != null) {
            startActivity(Intent.createChooser(emailIntent, getString(R.string.email_chooser_title)))
        } else {
            Toast.makeText(this, R.string.toastText, Toast.LENGTH_SHORT).show()
        }

    }

}