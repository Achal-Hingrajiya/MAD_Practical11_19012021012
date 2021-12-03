package src

class LoginInfo {
    companion object{

        lateinit var full_name : String;
        lateinit var  phone_number : String;
        lateinit var city: String;
        lateinit var email : String;
        lateinit var password: String;

//         var full_name : String = "Achal Hingrajiya"
//         var  phone_number : String = "+91 6355689874"
//         var city: String = "Rajkot, Gujarat"
//         var email : String = "achalhingrajiya19@gnu.ac.in"
//         var password: String = "abcd"
        var logged_in : Boolean= false;
//

        fun registered () : Boolean{
            return this :: email.isInitialized
//            return true

        }
        fun signUp(full_name : String, phone_number : String, city : String, email : String, password : String){
            this.full_name = full_name
            this.phone_number = phone_number
            this.city = city
            this.email = email
            this.password = password

        }
        fun login(email : String, password : String):Boolean{
            if (this.email == email && this.password == password){
                logged_in = true
                return true
            }
            return false
        }
        fun logout(){
            logged_in = false
        }
    }
}