package com.example.ex4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * MainActivity class
 */
public class MainActivity extends AppCompatActivity {
    Button Connect;
    public TcpClient tcp_client;

    /**
     * onCreate function- create a connection when toching the button of
     * the second activity
     * @param savedInstanceState Bundle type
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Connect = (Button) findViewById(R.id.connect1);
        Connect.setOnClickListener(new View.OnClickListener() {
            @Override
            //call open_activity_joystick when clicking
            public void onClick(View view) {
                connectClick(view);
                open_activity_joystick();
            }
        });
    }

    /**
     * connectClick function- cconnecting to the joystick activity
     * @param view view
     */
    public void connectClick(View view){
        Toast.makeText(MainActivity.this, "Connect Click",Toast.LENGTH_SHORT).show();
        EditText ip = (EditText)findViewById(R.id.ipText);
        EditText port = (EditText)findViewById(R.id.portText);
        String ipStr =  ip.getText().toString();
        String portStr =  port.getText().toString();
        TcpClient.getInstance().setIpAndPort(ipStr, Integer.parseInt(portStr));
    }

    /**
     * open_activity_joystick function- open the joystick activity
     */
    public void open_activity_joystick(){
        Intent intent = new Intent(this,
                JoystickActivity.class);
        startActivity(intent);
    }

}
