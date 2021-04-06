package com.example.gabriela_matuszewska_sr_12_30

import android.content.Context
import android.widget.Button

class CalculatorBrain(var context: Context) : CalculatorBrainInterface {
    private var currentValue: Double = 0.0
    var numbers = mutableListOf<Double>()
    var operations = mutableListOf<Button>()


    override fun addNumber(value: String) {
        var num = value.toDouble()
        numbers.add(num)
    }

    override fun addOperation(op: Button) {
        operations.add(op)
    }

    override fun evaluateFormula(): Double {
        var res: Double = 0.0
        if(numbers.size == 2 && operations.isNotEmpty()){
            res = evalTwo(numbers.removeLast(), numbers.removeLast(), operations.removeLast())
            if (!res.isNaN()){
                numbers.add(res)
            }

        }else if(numbers.size == 1 && operations.isNotEmpty()){
            res = evalOne(numbers.removeLast(), operations.removeLast())
            if (!res.isNaN()){
                numbers.add(res)
            }
        }
        return res

    }

    override fun removeLastOperation() {
        operations.removeLast()
    }

    override fun removeLastNumber() {
        numbers.removeLast()
    }

    private fun evalTwo(a: Double, b: Double, op: Button):Double{
        when(op){
            op.findViewById<Button>(R.id.add)-> return a + b
            op.findViewById<Button>(R.id.sub) -> return b - a
            op.findViewById<Button>(R.id.mul) -> return a * b
            op.findViewById<Button>(R.id.div) -> {
                if (a!=0.0){
                    return b / a
                }
            }
        }
        return Double.NaN
//        throw Error()
    }

    private fun evalOne(a: Double, op: Button): Double{
        when(op){
            op.findViewById<Button>(R.id.sqrt) -> {
                if (a>0.0){
                    return kotlin.math.sqrt(a)
                }
            }
            op.findViewById<Button>((R.id.percent)) -> return a / 100
        }
        return Double.NaN
//        throw Error()
    }

    override fun getOperationsSize(): Int {
        return operations.size
    }

    override fun clearAll() {
        numbers.clear()
        operations.clear()
    }

}