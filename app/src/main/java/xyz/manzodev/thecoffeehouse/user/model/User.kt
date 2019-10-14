package xyz.manzodev.thecoffeehouse.user.model


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.annotations.Expose
import xyz.manzodev.thecoffeehouse.CartInfo
import java.io.Serializable

class User() : Serializable{
    lateinit var uid:String

    @Expose
    lateinit var name:String

    var email : String?=null

    @Expose
    lateinit var imgUrl:String

    var dateOfBirth : String? = null

    @Expose
    var phone : String? = null

    var gender : String? =null

    constructor(uid:String,name:String, email :String?, imgUrl:String) : this()

    companion object{
        private lateinit var userHolder: User
        private var dataHolder:DataSnapshot?=null

        fun convertUser(firebaseUser : FirebaseUser) : User{
            return User(firebaseUser.uid!!,firebaseUser.displayName!!,firebaseUser.email,firebaseUser.photoUrl.toString())
        }

        fun getUser(onFinish : (u:User?)->Unit) {
            var firebaseUser = FirebaseAuth.getInstance().currentUser
            if (firebaseUser != null) {
                FirebaseDatabase.getInstance().getReference("User").child(firebaseUser!!.uid)
                    .addValueEventListener(object :
                        ValueEventListener {
                        override fun onDataChange(data: DataSnapshot) {
                            if  (dataHolder.toString() != data.toString()){
                                var a = dataHolder.toString() == data.toString()
                                dataHolder = data
                                var user = data.getValue(User::class.java)
                                userHolder = user!!
                            }
                            onFinish(userHolder)

                        }

                        override fun onCancelled(p0: DatabaseError) {}
                    })
            } else {
                onFinish(null)
            }
        }
    }

}