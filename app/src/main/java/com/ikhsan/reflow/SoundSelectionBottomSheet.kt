package com.ikhsan.reflow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SoundSelectionBottomSheet(private val onSoundSelected: (String) -> Unit) : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_sound_selection, container, false)

        view.findViewById<RadioButton>(R.id.rbHujan).setOnClickListener { onSoundSelected("Hujan Hutan"); dismiss() }
        view.findViewById<RadioButton>(R.id.rbkicau).setOnClickListener { onSoundSelected("Nature Birds"); dismiss() }
        return view
    }
}