package com.example.erabook.adapters

import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.erabook.R
import com.example.erabook.data.FocusMinutes

class FocusMinutesAdapter(context: Context) : ArrayAdapter<FocusMinutes>(
    context, 0,
    FocusMinutes.values()
) {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View =
            convertView ?: layoutInflater.inflate(R.layout.item_focus_spinner, parent, false)

        getItem(position)?.let { focusMinutes ->
            setFocusMinutes(view, focusMinutes)
        }

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        if (position == 0) {
            view = layoutInflater.inflate(R.layout.item_focus_header, parent, false)
            view.setOnClickListener {
                val root = parent.rootView
                root.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK))
                root.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK))
            }
        } else {
            view = layoutInflater.inflate(R.layout.item_focus_spinner, parent, false)

            getItem(position)?.let { focusMinutes ->
                setFocusMinutes(view, focusMinutes)
            }
        }
        return view
    }


    override fun getItem(position: Int): FocusMinutes? {
        if (position == 0) {
            return null
        }
        return super.getItem(position - 1)
    }

    override fun getCount() = super.getCount() + 1

    override fun isEnabled(position: Int) = position != 0


    private fun setFocusMinutes(view: View, focusMinutes: FocusMinutes) {
        view.findViewById<TextView>(R.id.minutes).text = focusMinutes.minutes.toString()

    }
}