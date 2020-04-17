package com.martinlaizg.geofind.views.fragment.play

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import butterknife.BindView
import butterknife.ButterKnife
import com.martinlaizg.geofind.R

class PlayThermFragment : PlayTourFragment() {
    @kotlin.jvm.JvmField
    @BindView(R.id.therm_card)
    var therm_card: CardView? = null
    private var lastDistance = 0f
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_play_therm, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun getTag(): String {
        return TAG
    }

    public override fun updateView() {
        if (distance > lastDistance) {
            therm_card!!.setCardBackgroundColor(
                    resources.getColor(android.R.color.holo_red_dark, null))
        } else {
            therm_card!!.setCardBackgroundColor(
                    resources.getColor(android.R.color.holo_green_dark, null))
        }
        lastDistance = distance
    }

    companion object {
        private val TAG = PlayThermFragment::class.java.simpleName
    }
}