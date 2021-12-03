package com.example.practical11_19012021012

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import src.LoginInfo

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        var signUpBtn = findViewById<Button>(R.id.btn_sign_up)

        signUpBtn.setOnClickListener {
            val email : String = findViewById<TextInputEditText>(R.id.su_email).text.toString()
            val fullName : String = findViewById<TextInputEditText>(R.id.su_full_name).text.toString()
            val phoneNumber : String = findViewById<TextInputEditText>(R.id.su_phone_no).text.toString()
            val password : String = findViewById<TextInputEditText>(R.id.su_password).text.toString()
            val confPassword : String = findViewById<TextInputEditText>(R.id.su_conf_password).text.toString()
            val city : String = findViewById<TextInputEditText>(R.id.su_city).text.toString()

            if (
                email.isNotBlank() &&
                fullName.isNotBlank() &&
                phoneNumber.isNotBlank() &&
                password.isNotBlank() &&
                confPassword.isNotBlank() &&
                city.isNotBlank()
            ){

                Log.i("SignUpActivity","Name: $fullName Phone: $phoneNumber City: $city Email: $email Pass: $password ConfPass: $confPassword")

                if (password == confPassword){
                    LoginInfo.signUp(fullName, phoneNumber, city, email, password)
                    Toast.makeText(this, "Signed Up Successfully!", Toast.LENGTH_SHORT).show()

                    Intent(this, LoginActivity :: class.java).apply {
                        startActivity(this)
                    }
                }else{
                    Toast.makeText(this, "Passwords doesn't match!", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_LONG).show()

            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Intent(this, LoginActivity :: class.java).apply {
            startActivity(this)
        }
    }
}