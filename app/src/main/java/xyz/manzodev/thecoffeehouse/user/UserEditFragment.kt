package xyz.manzodev.thecoffeehouse.user


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

import xyz.manzodev.thecoffeehouse.R


class UserEditFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_user_edit, container, false)
        var field: String? = null


        var tvDismiss = view.findViewById<TextView>(R.id.tv_dismiss)
        var tvField = view.findViewById<TextView>(R.id.tv_field)
        var etField = view.findViewById<EditText>(R.id.et_field)
        var btnField = view.findViewById<TextView>(R.id.btn_field)

        var bundle = arguments
        bundle?.let {
            field = it.getString("field")
            var data  = it.getString("value")

            tvField.text = field
            etField.hint = "$field của bạn"
            data?.let{ it -> etField.setText(it)}

            btnField.text = "Cập nhật " + field!!.toLowerCase()
        }

        tvDismiss.setOnClickListener { activity!!.onBackPressed() }

        btnField.setOnClickListener {
            field?.let {
                var value = etField.text.toString()
                if (value.isNotBlank()) {
                    (activity!! as UserDetailActivity).updateField(it,value)
                    activity!!.onBackPressed()
                }
            }
        }

        view.setOnClickListener {  }
        return view
    }

}
