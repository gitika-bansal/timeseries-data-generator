package com.learning.data.generator

import org.joda.time.DateTime


sealed class Result<Value,Error>{
    class Success<Value>(val value : Value) : Result<Value, Nothing>()
    class Failure<Error>(val error : Error) : Result<Nothing, Error>()
}

data class Stock(val id : Int, val name : String, val price : Double, val time : DateTime)

data class DataConfig(val meanValue : Double, val stdDeviation : Double, val intervalMs : Int, val count : Int)




