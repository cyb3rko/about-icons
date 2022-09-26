package com.cyb3rko.abouticons.modals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cyb3rko.abouticons.R
import com.cyb3rko.androidlicenses.AndroidLicenses
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textview.MaterialTextView

class IconLicenseBottomSheet(
    private val licenseName: String
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_license, container, false)
        view.findViewById<MaterialTextView>(R.id.message_view).text = AndroidLicenses.get(licenseName)
        return view
    }

    companion object {
        const val TAG = "License Bottom Sheet"
    }
}
