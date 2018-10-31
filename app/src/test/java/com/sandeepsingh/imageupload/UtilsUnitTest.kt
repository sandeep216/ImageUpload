package com.sandeepsingh.imageupload

import org.junit.Assert.assertEquals
import org.junit.Test
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Sandeep on 10/29/18.
 */
class UtilsUnitTest {

    @Test
    fun timeStampLengthUnitTest() {
        val timestamp = 1234567890000L
        assertEquals(13,timestamp.toString().length)
    }

    @Test
    fun validTimestampForRequiredFormat(){
        val timestamp = 1540828971000L
        val formatter = SimpleDateFormat("dd/MM/yyyy")

        val calendar = Calendar.getInstance()
        calendar.setTimeInMillis(timestamp)
        val date = formatter.format(calendar.time)
        assertEquals("29/10/2018",date)
    }
}
