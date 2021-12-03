package src

import android.content.SharedPreferences

class Authentication {

    companion object{
        fun getUserInfo(sharedPreferences: SharedPreferences): User {
            val full_name =sharedPreferences.getString("full_name", "")!!
            val phone_number =sharedPreferences.getString("phone_number", "")!!
            val city = sharedPreferences.getString("city", "")!!
            val email =sharedPreferences.getString("email", "")!!
            val password =sharedPreferences.getString("password", "")!!

            return User(full_name,phone_number,city,email,password)
        }


        fun signUp(sharedPreferences: SharedPreferences, user: User){
            val editor = sharedPreferences.edit()

            editor.putString("full_name",user.full_name)
            editor.putString("phone_number",user.phone_number)
            editor.putString("city",user.city)
            editor.putString("email", user.email)
            editor.putString("password", user.password)
            editor.putBoolean("registered",true)
            editor.apply()

        }

        fun login(sharedPreferences: SharedPreferences, email : String, password : String):Boolean{
            val username = sharedPreferences.getString("email","")
            val pwd = sharedPreferences.getString("password","")

            if (username == email && pwd == password){

                val editor = sharedPreferences.edit()
                editor.putBoolean("loggedIn", true)
                editor.apply()

                return true
            }
            return false
        }

    }


}