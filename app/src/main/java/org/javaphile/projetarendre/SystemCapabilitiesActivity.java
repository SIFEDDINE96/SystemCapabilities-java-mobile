package org.javaphile.projetarendre;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;


public class SystemCapabilitiesActivity extends AppCompatActivity {

    private static final int REQUEST_CALL_PERMISSION = 1;
    private EditText phoneNumberInput, smsNumberInput, smsContentInput, emailInput, subjectInput, messageInput, addressInput, urlInput;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_capabilities);

        phoneNumberInput = findViewById(R.id.editTextPhoneNumber);
        smsNumberInput = findViewById(R.id.editTextSmsNumber);
        smsContentInput = findViewById(R.id.editTextSmsContent);
        emailInput = findViewById(R.id.editTextEmail);
        subjectInput = findViewById(R.id.editTextSubject);
        messageInput = findViewById(R.id.editTextMessage);
        addressInput = findViewById(R.id.editTextAddress);
        urlInput = findViewById(R.id.editTextUrl);

        Button callButton = findViewById(R.id.buttonCall);
        callButton.setOnClickListener(v -> makePhoneCall());

        Button smsButton = findViewById(R.id.buttonSendSms);
        smsButton.setOnClickListener(v -> sendSms());

        Button emailButton = findViewById(R.id.buttonSendEmail);
        emailButton.setOnClickListener(v -> sendEmail());

        Button shareButton = findViewById(R.id.buttonShare);
        shareButton.setOnClickListener(v -> shareContent());

        Button mapButton = findViewById(R.id.buttonMap);
        mapButton.setOnClickListener(v -> openMap());

        Button browserButton = findViewById(R.id.buttonOpenBrowser);
        browserButton.setOnClickListener(v -> openBrowser());
    }

    private void makePhoneCall() {
        String phoneNumber = phoneNumberInput.getText().toString();
        if (phoneNumber.trim().isEmpty()) {
            Toast.makeText(this, "Veuillez entrer un numéro de téléphone", Toast.LENGTH_SHORT).show();
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
            } else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(callIntent);
            }
        }
    }

    private void sendSms() {
        String smsNumber = smsNumberInput.getText().toString();
        String smsContent = smsContentInput.getText().toString();
        if (smsNumber.trim().isEmpty() || smsContent.trim().isEmpty()) {
            Toast.makeText(this, "Veuillez entrer le numéro et le contenu du SMS", Toast.LENGTH_SHORT).show();
        } else {
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setData(Uri.parse("sms:" + smsNumber));
            smsIntent.putExtra("sms_body", smsContent);
            startActivity(smsIntent);
        }
    }

    private void sendEmail() {
        String email = emailInput.getText().toString();
        String subject = subjectInput.getText().toString();
        String message = messageInput.getText().toString();
        if (email.trim().isEmpty() || subject.trim().isEmpty() || message.trim().isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs de l'email", Toast.LENGTH_SHORT).show();
        } else {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(emailIntent, "Choisissez une application"));
        }
    }

    private void shareContent() {
        String message = messageInput.getText().toString();
        if (message.trim().isEmpty()) {
            Toast.makeText(this, "Veuillez entrer un texte à partager", Toast.LENGTH_SHORT).show();
        } else {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(shareIntent, "Partager via"));
        }
    }

    private void openMap() {
        String address = addressInput.getText().toString();
        if (address.trim().isEmpty()) {
            Toast.makeText(this, "Veuillez entrer une adresse", Toast.LENGTH_SHORT).show();
        } else {
            Intent mapIntent = new Intent(Intent.ACTION_VIEW);
            mapIntent.setData(Uri.parse("geo:0,0?q=" + Uri.encode(address)));
            startActivity(mapIntent);
        }
    }

    private void openBrowser() {
        String url = urlInput.getText().toString();
        if (url.trim().isEmpty()) {
            Toast.makeText(this, "Veuillez entrer une URL", Toast.LENGTH_SHORT).show();
        } else {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://" + url;
            }
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setData(Uri.parse(url));
            startActivity(browserIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission d'appel refusée", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
