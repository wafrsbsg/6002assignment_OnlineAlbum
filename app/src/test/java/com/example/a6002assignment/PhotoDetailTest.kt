package com.example.a6002assignment

import android.location.Address
import android.location.Geocoder
import org.junit.Test
import android.content.Context
import android.location.Location
import java.util.*

class PhotoDetailTest {
    @Test
    fun distance() {
        val float = FloatArray(1)
        Location.distanceBetween(38.2070, -122.2151, 37.6147, -121.8948,float)
    }
}