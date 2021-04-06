//Gabriela Matuszewska
//Udało mi się zrobić wszystko oprócz ostatniego punktu z kolejnością działań,
//nie jestem też pewna czy udało mi się zrobić obsługę wszystkich niepoprawnych wartości
//(punkt 3c) (zrobiłam m.in: obsługę przed wielokrotnym wpisywaniem 0, niemożliwością wpisania: 34.56.7)

package com.example.gabriela_matuszewska_sr_12_30

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var equalButton: Button
    private lateinit var clearButton: Button
    private lateinit var decimalButton: Button
    private lateinit var display: TextView

    private lateinit var digits: Array<Button>
    private lateinit var binaryOperation: Array<Button>
    private lateinit var unaryOperation: Array<Button>


    var lastDot: Boolean =  false
    var lastDigit: Boolean =  false
    var lastUnaryOperation: Boolean = false
    var lastBinaryOperation: Boolean =  false
    var lastEqual: Boolean = false

    // value to add to calculator engine
    private var lastNumber = ""

    // value for textView
    private var toDisplay = ""
        set(value) {
            if (value == ""){
                setTextView("0")
            } else {
                setTextView(value)
            }
            field = value
        }

    var calculatorEngine: CalculatorBrainInterface = CalculatorBrain(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        equalButton= findViewById(R.id.equals)
        clearButton= findViewById(R.id.clear)
        decimalButton= findViewById(R.id.dot)
        display = findViewById(R.id.display)


        equalButton.setOnClickListener { evaluateFormula() }
        clearButton.setOnClickListener { clear() }
        decimalButton.setOnClickListener { decimalPressed() }

        val digitsIDs = arrayOf(R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9)
        digits = (digitsIDs.map{id -> findViewById<Button>(id) }).toTypedArray()
        digits.forEach { button -> button.setOnClickListener { i -> digitPressed( i as Button) } }

        val binaryOperationIDs = arrayOf(R.id.add, R.id.sub, R.id.mul, R.id.div)
        val unaryOperationIDs = arrayOf(R.id.sqrt, R.id.percent)

        binaryOperation = (binaryOperationIDs.map{ id -> findViewById<Button>(id)}).toTypedArray()
        binaryOperation.forEach { button -> button.setOnClickListener { i -> binaryOperationPressed( i as Button) } }

        unaryOperation = (unaryOperationIDs.map{ id -> findViewById<Button>(id)}).toTypedArray()
        unaryOperation.forEach { button -> button.setOnClickListener { i -> unaryOperationPressed( i as Button) } }

    }


    private fun unaryOperationPressed(op: Button) {
        if(!lastDigit && !lastUnaryOperation && !lastDot){
            toDisplay = ""
            return
        }else if (lastDigit && calculatorEngine.getOperationsSize() == 0 && !lastEqual && !lastUnaryOperation){
            calculatorEngine.addNumber(lastNumber)
            calculatorEngine.addOperation(op)
            toDisplay = calculatorEngine.evaluateFormula().toString()
            lastNumber = toDisplay
        }else if (lastDigit && calculatorEngine.getOperationsSize() == 0 && !lastEqual && lastUnaryOperation){
            calculatorEngine.removeLastNumber()
            calculatorEngine.addNumber(lastNumber)
            calculatorEngine.addOperation(op)
            toDisplay = calculatorEngine.evaluateFormula().toString()
            lastNumber = toDisplay
        } else if(lastBinaryOperation){
            calculatorEngine.removeLastOperation()
            calculatorEngine.addOperation(op)
        } else if(lastDigit && calculatorEngine.getOperationsSize() == 0 && lastEqual) {
            calculatorEngine.addOperation(op)
            toDisplay = calculatorEngine.evaluateFormula().toString()
            lastNumber = ""
        }else if (lastDigit && calculatorEngine.getOperationsSize() > 0) {
            calculatorEngine.addNumber(lastNumber)
            val res = calculatorEngine.evaluateFormula().toString()
            calculatorEngine.removeLastNumber()
            calculatorEngine.addNumber(res)
            calculatorEngine.addOperation(op)
            toDisplay = calculatorEngine.evaluateFormula().toString()
            lastNumber = ""
        }

        lastUnaryOperation = true
        lastEqual = false
        lastDigit = true
        lastBinaryOperation = false
        lastDot = false

    }

    private fun binaryOperationPressed(op: Button) {
        if(!lastDigit && !lastBinaryOperation && !lastDot && !lastUnaryOperation){
            toDisplay = ""
            return
        }
        if (!lastBinaryOperation && lastDigit && calculatorEngine.getOperationsSize() > 0) {
            calculatorEngine.addNumber(lastNumber)
            toDisplay = calculatorEngine.evaluateFormula().toString()
            calculatorEngine.addOperation(op)
            lastNumber = ""
        } else if((!lastBinaryOperation && lastDigit && calculatorEngine.getOperationsSize() == 0 && lastEqual) || (!lastBinaryOperation && lastDigit && calculatorEngine.getOperationsSize() == 0 && lastUnaryOperation)) {
            calculatorEngine.addOperation(op)
            lastNumber = ""
        } else if(!lastBinaryOperation && lastDigit && calculatorEngine.getOperationsSize() == 0 && !lastEqual){
            calculatorEngine.addNumber(lastNumber)
            calculatorEngine.addOperation(op)
            lastNumber = ""
        } else{
            calculatorEngine.removeLastOperation()
            calculatorEngine.addOperation(op)
        }
        lastDigit = false
        lastDot = false
        lastBinaryOperation = true
        lastUnaryOperation = false
        lastEqual = false
    }


    private fun digitPressed(digit: Button) {
        if((digit.id == R.id.button0 && toDisplay == "") || (digit.id == R.id.button0 && toDisplay == "0")){
            return
        }
        if(lastEqual){
            toDisplay = ""
            calculatorEngine.removeLastNumber()
            lastNumber = ""
        }else if(lastBinaryOperation || lastUnaryOperation){
            toDisplay = ""
        }
        lastDigit = true
        lastBinaryOperation = false
        lastUnaryOperation = false
        lastDot = false
        lastEqual = false
        toDisplay+=digit.text.toString()
        lastNumber+=digit.text.toString()
    }

    private fun decimalPressed() {
        if((lastBinaryOperation && !lastEqual) || (lastUnaryOperation && !lastEqual)){
            toDisplay+="0."
            lastNumber+="0."
            lastBinaryOperation = false
            lastUnaryOperation = false
            lastDigit = false
            lastDot = true
        } else if(lastDot || lastEqual) {
            return
        } else if(!toDisplay.contains(".")){
            toDisplay+="."
            lastNumber+="."
            lastBinaryOperation = false
            lastUnaryOperation = false
            lastDigit = false
            lastDot = true
            lastEqual = false
        }

    }

    private fun clear() {
        toDisplay = ""
        lastBinaryOperation = false
        lastUnaryOperation = false
        lastDigit = false
        lastDot = false
        lastEqual = false
        lastNumber = ""
        calculatorEngine.clearAll()
    }

    private fun evaluateFormula() {
        if(lastDigit && !lastEqual && !lastBinaryOperation) {
            calculatorEngine.addNumber(lastNumber)
            toDisplay = calculatorEngine.evaluateFormula().toString()
            lastNumber = toDisplay
        }
        if (lastNumber.toDouble().isNaN()){
            lastNumber = ""
        }
        lastEqual = true
        lastBinaryOperation = false
        lastDigit = true
        lastDot = false
    }

    private fun setTextView(s: String) {
        display.text = s
    }
}