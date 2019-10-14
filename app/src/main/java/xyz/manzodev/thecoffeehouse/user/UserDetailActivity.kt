package xyz.manzodev.thecoffeehouse.user

import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import xyz.manzodev.thecoffeehouse.CartInfo
import xyz.manzodev.thecoffeehouse.R
import xyz.manzodev.thecoffeehouse.databinding.ActivityUserDetailBinding
import xyz.manzodev.thecoffeehouse.user.model.User
import java.io.ByteArrayOutputStream
import java.util.*


class UserDetailActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var activityUserDetailBinding: ActivityUserDetailBinding
    var user: User? = null
    private lateinit var ref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityUserDetailBinding = DataBindingUtil.setContentView(this,R.layout.activity_user_detail)

        configUser()


        activityUserDetailBinding.layoutName.setOnClickListener(this)
        activityUserDetailBinding.layoutBirthday.setOnClickListener(this)
        activityUserDetailBinding.layoutGender.setOnClickListener(this)
        activityUserDetailBinding.layoutPhone.setOnClickListener(this)
        activityUserDetailBinding.btnLogOut.setOnClickListener(this)
        activityUserDetailBinding.ivProfile.setOnClickListener(this)
        activityUserDetailBinding.ivDismiss.setOnClickListener(this)
    }

    private fun configUser() {
//        user = intent.getSerializableExtra("user") as User?
//        user?.let{
//            activityUserDetailBinding.user = it
//            ref = FirebaseDatabase.getInstance().getReference("User").child(it.uid)
//            ref.addValueEventListener(object:ValueEventListener{
//                override fun onCancelled(p0: DatabaseError) {
//                }
//
//                override fun onDataChange(data: DataSnapshot) {
//                    var user = data.getValue(User::class.java)
//                    activityUserDetailBinding.user = user
//                }
//
//            })
//        }
        User.getUser {u->
            u?.let{
                ref = FirebaseDatabase.getInstance().getReference("User").child(it.uid)
                user = it
                CartInfo.getInstance().user = user
            }
            activityUserDetailBinding.user = u
        }


    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.layout_name ->{
                var bundle = Bundle()
                bundle.putString("field","Tên")
                bundle.putString("value",user!!.name)

                var userEditFragment = UserEditFragment()
                userEditFragment.arguments = bundle

                supportFragmentManager.beginTransaction().add(R.id.user_detail_container,userEditFragment,UserEditFragment::class.java.simpleName)
                    .addToBackStack(UserEditFragment::class.java.simpleName).commit()
            }
            R.id.layout_phone->{
                var bundle = Bundle()
                bundle.putString("field","Số điện thoại")
                bundle.putString("value",user!!.phone)

                var userEditFragment = UserEditFragment()
                userEditFragment.arguments = bundle

                supportFragmentManager.beginTransaction().add(R.id.user_detail_container,userEditFragment,UserEditFragment::class.java.simpleName)
                    .addToBackStack(UserEditFragment::class.java.simpleName).commit()
            }
            R.id.layout_birthday ->{
                var c = Calendar.getInstance()
                var year = c.get(Calendar.YEAR)
                var month = c.get(Calendar.MONTH)
                var day = c.get(Calendar.DAY_OF_MONTH)
                val datePickerDialog = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { _, p1, p2, p3 ->
                        updateField("birthday","${(if(p3 >= 10)  p3 else "0$p3")} / ${(if(p2 >=9)  p2+1 else "0${(p2+1)}")} / $p1") }, year, month, day
                )

                datePickerDialog.show()
            }
            R.id.layout_gender->{
                var genderPickerFragment = GenderPickerFragment()
                genderPickerFragment.show(supportFragmentManager,GenderPickerFragment::class.java.simpleName)
            }

            R.id.iv_profile->{
                var imagePickerFragment = ImagePickerFragment()
                imagePickerFragment.show(supportFragmentManager,ImagePickerFragment::class.java.simpleName)
            }
            R.id.btn_log_out->{
                FirebaseAuth.getInstance().signOut()
                finish()
            }
            R.id.tv_dismiss->{
                finish()
            }
        }
    }

    fun updateField(field:String,value:Any?){
        when(field){
            in "Tên" ->{
                ref.child("name").setValue(value as String)
            }
            in "Số điện thoại"->{
                ref.child("phone").setValue(value as String)
            }
            in "birthday"->{
                ref.child("dateOfBirth").setValue(value as String)
            }
            in "gender"->{
                ref.child("gender").setValue(value as String)
            }
            in "image_profile"->{
                activityUserDetailBinding.ivProfile.setImageBitmap(value as Bitmap)
                uploadImageProfile(value)
            }
        }
    }

    private fun uploadImageProfile(bitmap:Bitmap){
        Toast.makeText(this,"Uploading",LENGTH_SHORT).show()
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("User").child(user!!.uid + ".jpg")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()


        val uploadTask = storageRef.putBytes(data)
        uploadTask.addOnFailureListener { }
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {

                throw task.exception!!
            }

            // Continue with the task to get the download URL
            storageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                user!!.imgUrl = downloadUri.toString()
                FirebaseDatabase.getInstance().reference.child("User").child(user!!.uid)
                    .child("imgUrl").setValue(downloadUri.toString())
                Toast.makeText(this,"Uploaded",LENGTH_SHORT).show()
            }
        }
    }


}
