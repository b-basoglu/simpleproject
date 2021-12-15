package com.simple.firstapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {
    val toastValueMLD : MutableLiveData<String> = MutableLiveData("activityDefault")
}