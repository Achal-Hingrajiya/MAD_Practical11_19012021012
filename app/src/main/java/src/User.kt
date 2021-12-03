package src

import java.io.Serializable

class User (val full_name: String,
            val phone_number : String,
            val city : String,
            val email : String,
            val password : String?) : Serializable{

}