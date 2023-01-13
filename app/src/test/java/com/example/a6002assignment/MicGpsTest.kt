package com.example.a6002assignment

import android.location.Address
import android.location.Geocoder
import org.junit.Test
import android.content.Context
import java.util.*

class MicGpsTest {
    private lateinit var context: Context
    @Test
    fun geocoder() {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(37.6147, -121.8948, 1)
        val address: String = addresses[0].getAddressLine(0)
    }
}