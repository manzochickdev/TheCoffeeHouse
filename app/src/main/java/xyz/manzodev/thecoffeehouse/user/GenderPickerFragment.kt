package xyz.manzodev.thecoffeehouse.user


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import xyz.manzodev.thecoffeehouse.R
import xyz.manzodev.thecoffeehouse.databinding.FragmentGenderPickerBinding


class GenderPickerFragment : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var fragmentGenderPickerBinding = DataBindingUtil.inflate<FragmentGenderPickerBinding>(inflater,R.layout.fragment_gender_picker, container, false)
        fragmentGenderPickerBinding.btnMale.setOnClickListener {
            (activity!! as UserDetailActivity).updateField("gender","Nam")
            dismiss()
        }
        fragmentGenderPickerBinding.btnFemale.setOnClickListener {
            (activity!! as UserDetailActivity).updateField("gender","Nữ")
            dismiss()
        }
        fragmentGenderPickerBinding.btnCancel.setOnClickListener { dismiss() }
        return fragmentGenderPickerBinding.root
    }
}
