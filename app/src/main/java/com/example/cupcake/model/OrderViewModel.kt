package com.example.cupcake.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

const val CUPCAKE_PRICE = 2.0
const val SAME_DAY_INCREASE = 3.0

class OrderViewModel: ViewModel() {
    private var _orderQuantity = MutableLiveData<Int>()
    val orderQuantity: LiveData<Int> = _orderQuantity

    private var _cupCakeFlavor = MutableLiveData<String>()
    val cupCakeFlavor: LiveData<String> = _cupCakeFlavor

    private var _pickupDate = MutableLiveData<String>()
    val pickupDate: LiveData<String> = _pickupDate

    private var _price = MutableLiveData<Double>()
    val price: LiveData<String> = Transformations.map(_price) {
        NumberFormat.getCurrencyInstance().format(it)
    }

    init {
        resetOrder()
    }

    fun setQuantity(numberCupcakes: Int){
        _orderQuantity.value = numberCupcakes
        calculatePrice()
    }

    fun hasNoFlavorSet(): Boolean {
        return _cupCakeFlavor.value.isNullOrEmpty()
    }

    fun setFlavor(desiredFlavor: String){
        _cupCakeFlavor.value = desiredFlavor
    }

    fun setDate(pickupDate: String){
        _pickupDate.value = pickupDate
        calculatePrice()
    }

    fun pickupDatesValues(): List<String> {
        val options = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()
        // Create a list of dates starting with the current date and the following 3 dates
        repeat(4) {
            options.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }
        return options
    }

    fun calculatePrice(){
        var totalPrice = (orderQuantity.value ?: 0) * CUPCAKE_PRICE

        if (_pickupDate.value == pickupDatesValues()[0])
            totalPrice += SAME_DAY_INCREASE

        _price.value = totalPrice
    }

    /*
* Re-initializes the cupCake app data.
*/
    fun resetOrder() {
        _orderQuantity.value = 0
        _cupCakeFlavor.value = ""
        _pickupDate.value = pickupDatesValues()[0]
        _price.value = 0.0
        calculatePrice()
    }

}