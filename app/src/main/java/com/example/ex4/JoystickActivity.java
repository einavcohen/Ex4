package com.example.ex4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * JoystickActivity class
 */
public class JoystickActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new Joystick(this));
        TcpClient.getInstance().Connect();
    }

    /**
     * onDestroy function- close the connection between the server and the client
     */
    protected void onDestroy()
    {
        super.onDestroy();
        TcpClient.getInstance().close();
    }
}
