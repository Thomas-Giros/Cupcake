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

    private var _totalOrderedQuantity = MutableLiveData<Int>()
    val totalOrderedQuantity: LiveData<Int> = _totalOrderedQuantity

    private var _orderedQuantityPerFlavor = MutableLiveData<MutableMap<String,Int>>()
    val orderedQuantityPerFlavor: LiveData<MutableMap<String,Int>> = _orderedQuantityPerFlavor

    private var _cupCakeFlavors = MutableLiveData<MutableList<String>>()
    val cupCakeFlavors: LiveData<MutableList<String>> = _cupCakeFlavors

    private var _pickupDate = MutableLiveData<String>()
    val pickupDate: LiveData<String> = _pickupDate

    private var _clientName = MutableLiveData<String>()
    val clientName: LiveData<String> = _clientName

    private var _clientAdress = MutableLiveData<String>()
    val clientAdress: LiveData<String> = _clientAdress

    private var _price = MutableLiveData<Double>()
    val price: LiveData<String> = Transformations.map(_price) {
        NumberFormat.getCurrencyInstance().format(it)
    }

    init {
        resetOrder()
    }

    fun setClientName(name: String){
        _clientName.value = name
    }

    fun setClientAdress(address: String){
        _clientAdress.value = address
    }

    fun setQuantity(numberCupcakes: Int){
        _orderQuantity.value = numberCupcakes
        calculatePrice()
    }

    fun getFlavorList(): List<String>{
        val listOfFlavors: List<String>
        if (_pickupDate.value!!.uppercase().contains("SAT")   || _pickupDate.value!!.uppercase().contains("SUN"))
            listOfFlavors = allFlavorList + bonusFlavorList
        else
            listOfFlavors = allFlavorList
        return listOfFlavors
    }

    fun addFlavor(desiredFlavor: String){
        if (_totalOrderedQuantity.value?.toInt() ?: 0 < _orderQuantity.value?.toInt() ?: -1)
        {
            _totalOrderedQuantity.value = _totalOrderedQuantity.value!! + 1
            _cupCakeFlavors.value?.add(desiredFlavor)
            _orderedQuantityPerFlavor.value?.set(desiredFlavor,
                _orderedQuantityPerFlavor.value!![desiredFlavor]!! + 1
            )
        }
    }

    fun removeFlavor(undesiredFlavor: String){
        if (_totalOrderedQuantity.value?.toInt() ?: 0 > 0)
        {
            _totalOrderedQuantity.value = _totalOrderedQuantity.value!! - 1
            _cupCakeFlavors.value?.remove(undesiredFlavor)
            _orderedQuantityPerFlavor.value?.set(undesiredFlavor,
                _orderedQuantityPerFlavor.value!![undesiredFlavor]!! - 1
            )
        }
    }

    fun orderedQuantityPerFlavor(flavour: String): String{
        return _orderedQuantityPerFlavor.value?.get(flavour).toString()
    }

    fun cupCakeFlavorsToString():String{
        val keys = _orderedQuantityPerFlavor.value?.keys?.filter{
                key -> _orderedQuantityPerFlavor.value?.get(key)!! > 0 }
        val itemToString = mutableListOf<String>()

        if (keys != null) {
            for (key in keys){
                itemToString.add("(${_orderedQuantityPerFlavor.value?.get(key)}) $key")
            }
        }
        return itemToString.joinToString(", ", postfix = ".")
    }

    fun setDate(pickupDate: String){
        _pickupDate.value = pickupDate
        resetFlavorList()
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

    fun orderComplete(): Boolean{
        return _totalOrderedQuantity.value!!.toInt() == _orderQuantity.value!!.toInt()
    }


    fun resetFlavorList() {
        _totalOrderedQuantity.value = 0
        _orderedQuantityPerFlavor.value = mutableMapOf()
        for (i in getFlavorList()){
            _orderedQuantityPerFlavor.value!![i] = 0
        }
    }
    /*
    * Re-initializes the cupCake app data.
    */
    fun resetOrder() {
        _orderQuantity.value = 0
        _totalOrderedQuantity.value = 0
        _cupCakeFlavors.value = mutableListOf()
        _orderedQuantityPerFlavor.value = mutableMapOf()
        _pickupDate.value = pickupDatesValues()[0]
        _price.value = 0.0
        _clientAdress.value = ""
        _clientName.value = ""
        calculatePrice()

        resetFlavorList()
    }

}
