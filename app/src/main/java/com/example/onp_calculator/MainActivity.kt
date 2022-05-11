package com.example.onp_calculator

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.SupportActionModeWrapper
import androidx.preference.PreferenceManager
import java.lang.Math.*
import java.util.ArrayDeque
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    var decPrec = 2
    var limitToFour = false
    private var stack = ArrayDeque<String>()
    private var currentVal = ""

    // Utility functions
    private fun Double.roundDecimal(decimals: Int): Double{
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return round(this * multiplier) / multiplier
    }
    private fun decimalControl(s: String): String{
        var str = ""
        if (s.endsWith(".0")){
            str = s.substring(0, s.length-2)
        }
        else if (s.endsWith(".")){
            str = s.substring(0, s.length-1)
        }
        else{
            str = s
        }

        if (str.contains('.')){
            if (str.indexOf('.') == str.length-1)
                return str.substring(0, str.length-min(str.length-1,1))
            else {
                if (decPrec == 0 || str.endsWith(".0"))
                    return str.toDouble().roundDecimal(decPrec).toInt().toString()
                else
                    return str.toDouble().roundDecimal(decPrec).toString()
            }
        }
        else{
            return str.toInt().toString()
        }
    }
    private fun getFromStack(index: Int): String{
        if (stack.size <= index){
            return ""
        }
        else{
            return decimalControl(stack.elementAt(index))
        }
    }
    private fun updateDisplayValues(){
        val display: TextView = findViewById(R.id.display)
        var constructedText = ""
        if (currentVal.isEmpty()){
            constructedText += "4:${getFromStack(3)}\n"
            constructedText += "3:${getFromStack(2)}\n"
            constructedText += "2:${getFromStack(1)}\n"
            constructedText += "1:${getFromStack(0)}"
        }
        else{
            constructedText += "3:${getFromStack(2)}\n"
            constructedText += "2:${getFromStack(1)}\n"
            constructedText += "1:${getFromStack(0)}\n"
            constructedText += currentVal
        }
        display.text = constructedText
    }
    @SuppressLint("ResourceAsColor")
    private fun setSettings(){
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        // Changing decimal precision on the stack
        val decimalPrec = sharedPreferences.getString("decimal_acc", "")
        decPrec = decimalPrec!!.toInt()

        // Changing background color on the stack
        val backgroudColor = sharedPreferences.getString("display_back", "")
        val display: TextView = findViewById(R.id.display)
        var colorId = getResources().getIdentifier(backgroudColor, "color", getPackageName())
        display.background = ColorDrawable(getResources().getColor(colorId))

        // Limit the stack to use only 4 values
        limitToFour = sharedPreferences.getBoolean("limit", false)

        updateDisplayValues()
    }

    // Button Handlers
    private fun settingsButtonHandler(buttonHandle: Button){
        buttonHandle.setOnClickListener{
            val switchActivityIntent = Intent(this, SettingsActivity::class.java)
            startActivity(switchActivityIntent)
        }
        setSettings()
    }
    private fun numberButtonHandler(buttonHandle: Button, num: Int){
        buttonHandle.setOnClickListener {
            currentVal += num.toString()
            updateDisplayValues()
        }
    }
    private fun pmButtonHandler(buttonHandle: Button){
        buttonHandle.setOnClickListener {
            if (currentVal.startsWith("-"))
                currentVal = currentVal.substring(1)
            else
                currentVal = "-$currentVal"
            updateDisplayValues()
        }
    }
    private fun dotButtonHandler(buttonHandle: Button){
        buttonHandle.setOnClickListener {
            if (currentVal.isEmpty() || currentVal == "-")
                currentVal += "0."
            else if (!currentVal.contains('.'))
                currentVal += "."
            updateDisplayValues()
        }
    }
    private fun enterButtonHandler(buttonHandle: Button){
        buttonHandle.setOnClickListener {
            if (currentVal.isNotEmpty() && (limitToFour == false || stack.size < 4)) {
                if (currentVal != "-") {
                    stack.push(currentVal)
                    currentVal = ""
                }
            }
            else if (limitToFour == true && stack.size >= 4){
                Toast.makeText(this, "Stack limited to 4 values", Toast.LENGTH_SHORT).show()
            }
            updateDisplayValues()
        }
    }
    private fun dropButtonHandler(buttonHandle: Button){
        buttonHandle.setOnClickListener {
            if (stack.isEmpty()){
                Toast.makeText(this, "Stack is empty", Toast.LENGTH_SHORT).show()
            }
            else{
                stack.pop()
            }
            updateDisplayValues()
        }
    }
    private fun swapButtonHandler(buttonHandle: Button){
        buttonHandle.setOnClickListener {
            if (stack.size<2){
                Toast.makeText(this, "Stack too small", Toast.LENGTH_SHORT).show()
            }
            else{
                val first = stack.pop()
                val second = stack.pop()
                stack.push(first)
                stack.push(second)
            }
            updateDisplayValues()
        }
    }
    private fun acButtonHandler(buttonHandle: Button){
        buttonHandle.setOnClickListener {
            if (stack.isEmpty()){
                Toast.makeText(this, "Stack is empty", Toast.LENGTH_SHORT).show()
            }
            else{
                stack = ArrayDeque<String>()
            }
            updateDisplayValues()
        }
    }
    private fun undoButtonHandler(buttonHandle: Button){
        buttonHandle.setOnClickListener {
            if (currentVal.isNotEmpty())
                currentVal = currentVal.substring(0,currentVal.length-1)
            updateDisplayValues()
        }
    }
    private fun calcButtonHandler(buttonHandle: Button, calcType: Char){
        buttonHandle.setOnClickListener {
            if (stack.size < 2){
                Toast.makeText(this, "Too little values on the stack", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val a = stack.pop().toDouble()
            val b = stack.pop().toDouble()
            if (a == 0.0 && (calcType == 'd' || calcType == 'r')){
                Toast.makeText(this, "Can't divide by zero", Toast.LENGTH_SHORT).show()
                stack.push(b.toString())
                stack.push(a.toString())
                return@setOnClickListener
            }
            if (b < 0 && calcType == 'r'){
                Toast.makeText(this, "Forbidden operation", Toast.LENGTH_SHORT).show()
                stack.push(b.toString())
                stack.push(a.toString())
                return@setOnClickListener
            }
            when (calcType){
                'a'     -> stack.push((b+a).toString())
                'm'     -> stack.push((b-a).toString())
                'x'     -> stack.push((b*a).toString())
                'd'     -> stack.push((b/a).toString())
                'p'     -> stack.push(b.pow(a).toString())
                'r'     -> stack.push(b.pow(1/a).toString())
                else    -> Log.i("calcButtonHandler", "Unknown calculation id")
            }
            updateDisplayValues()
        }
    }

    private fun buttonHandler(buttonId: Int, num: Int = 0){
        val buttonHandle: Button = findViewById(buttonId)
        when (buttonId){
            R.id.settings   -> settingsButtonHandler(buttonHandle)
            R.id.pm         -> pmButtonHandler(buttonHandle)
            R.id.dot        -> dotButtonHandler(buttonHandle)
            R.id.enter      -> enterButtonHandler(buttonHandle)
            R.id.drop       -> dropButtonHandler(buttonHandle)
            R.id.swap       -> swapButtonHandler(buttonHandle)
            R.id.ac         -> acButtonHandler(buttonHandle)
            R.id.undo       -> undoButtonHandler(buttonHandle)
            R.id.plus       -> calcButtonHandler(buttonHandle, 'a')
            R.id.minus      -> calcButtonHandler(buttonHandle, 'm')
            R.id.multi      -> calcButtonHandler(buttonHandle, 'x')
            R.id.div        -> calcButtonHandler(buttonHandle, 'd')
            R.id.pow        -> calcButtonHandler(buttonHandle, 'p')
            R.id.root       -> calcButtonHandler(buttonHandle, 'r')
            R.id.zero, R.id.one, R.id.two, R.id.three, R.id.four,
            R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine -> numberButtonHandler(buttonHandle, num)
            else -> Log.i("buttonHandler", "Unknown button id")
        }
    }

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