package com.example.onp_calculator

import android.os.Bundle

class MainActivity : ButtonHandling() {

    // button handling
    private fun setButtonHandlers(){
        buttonHandler(R.id.settings)            // Settings button
        buttonHandler(R.id.pm)                  // Plus/minus button handler
        buttonHandler(R.id.dot)                 // Decimal separator handler
        buttonHandler(R.id.enter)               // Enter button handler
        buttonHandler(R.id.drop)                // Drop button handler
        buttonHandler(R.id.swap)                // Swap button handler
        buttonHandler(R.id.ac)                  // All clear button handler
        buttonHandler(R.id.undo)                // Undo button handler
        // Calculation buttons
        buttonHandler(R.id.plus)
        buttonHandler(R.id.minus)
        buttonHandler(R.id.multi)
        buttonHandler(R.id.div)
        buttonHandler(R.id.pow)
        buttonHandler(R.id.root)
        // Number buttons
        buttonHandler(R.id.zero, 0)
        buttonHandler(R.id.one, 1)
        buttonHandler(R.id.two, 2)
        buttonHandler(R.id.three, 3)
        buttonHandler(R.id.four, 4)
        buttonHandler(R.id.five, 5)
        buttonHandler(R.id.six, 6)
        buttonHandler(R.id.seven, 7)
        buttonHandler(R.id.eight, 8)
        buttonHandler(R.id.nine, 9)
    }

    // "on" functions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setButtonHandlers()
    }
    override fun onResume() {
        super.onResume()
        setSettings()
    }
}