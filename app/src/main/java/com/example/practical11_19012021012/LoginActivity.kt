package com.example.practical11_19012021012

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import src.LoginInfo

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (LoginInfo.logged_in){
            Toast.makeText(this, "Welcome back, ${LoginInfo.full_name}!", Toast.LENGTH_SHORT).show()
            Intent(this, SplashActivity :: class.java).apply {
                startActivity(this)
            }
        }

        var signUpBtn = findViewById<Button>(R.id.tbtn_signup)

        signUpBtn.setOnClickListener {
            Intent(this, SignUpActivity :: class.java).apply {
                startActivity(this)
            }
        }

        var loginButton = findViewById<Button>(R.id.login)

        loginButton.setOnClickListener {
            val email : String = findViewById<TextInputEditText>(R.id.lg_email).text.toString()
            val password : String = findViewById<TextInputEditText>(R.id.lg_password).text.toString()
            Log.i("LoginActivity","Email: $email Password: $password")

            if (LoginInfo.registered()){

                if (email.isNotBlank() && password.isNotBlank()){
                    if (LoginInfo.login(email, password)){
                        Toast.makeText(this, "Logged In Successfully!", Toast.LENGTH_SHORT).show()

                        Intent(this, SplashActivity :: class.java).apply {
                            startActivity(this)
                        }
                    }
                    else{
                        Toast.makeText(this, "Email or Password is incorrect. Retry.", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this, "Please Sign Up first.", Toast.LENGTH_SHORT).show()
            }
        }
    }


}