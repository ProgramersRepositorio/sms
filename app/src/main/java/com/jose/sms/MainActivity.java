package com.jose.sms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ImageButton btnEnviar;
    TextView txtTelefono, txtLatitud, txtLongitud, txtPrecision, txtAltura;
    LocationManager localizador;
    Location localizacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnEnviar = findViewById(R.id.buttonEnviarSms);
        txtTelefono = findViewById(R.id.textTelefono);
        txtLatitud = findViewById(R.id.textLatitud);
        txtLongitud = findViewById(R.id.textLongitud);
        txtPrecision = findViewById(R.id.textPrecision);
        txtAltura = findViewById(R.id.textAltitud);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mensaje = obtenerLocalizacion();
                enviarMensaje(txtTelefono.getText().toString(), mensaje);
            }
        });
    }

    private void enviarMensaje(String numero, String mensaje) {
        //comprobamos si tenemos los permisos necesarios
        if (ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest
                        .permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                    {Manifest.permission.SEND_SMS,}, 1000);
        }
        try {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(numero, null, mensaje, null, null);
            Toast.makeText(getApplicationContext(), "Mensaje Enviado.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Mensaje no enviado, datos incorrectos.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    private String obtenerLocalizacion() {
        String texto = "";

        //comprobamos si tenemos los permisos necesarios
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        localizador = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        localizacion = localizador.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        txtLatitud.setText(String.valueOf(localizacion.getLatitude()));
        txtLongitud.setText(String.valueOf(localizacion.getLongitude()));
        txtPrecision.setText(String.valueOf(localizacion.getAccuracy()));
        txtAltura.setText(String.valueOf(localizacion.getAltitude()));

        texto = "Latitud: "+txtLatitud.getText().toString()+", Longitud: "+txtLongitud.getText().toString()+
                ",Altitud: "+ txtAltura.getText()+"Precision: "+txtPrecision.getText();

        return texto;
    }
}