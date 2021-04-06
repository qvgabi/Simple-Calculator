package com.example.gabriela_matuszewska_sr_12_30

import android.widget.Button

interface CalculatorBrainInterface {

    public fun addOperation(op: Button)
    public fun addNumber(value: String)
    public fun evaluateFormula():Double
    public fun removeLastOperation()
    public fun removeLastNumber()
    public fun getOperationsSize(): Int
    public fun clearAll()

}