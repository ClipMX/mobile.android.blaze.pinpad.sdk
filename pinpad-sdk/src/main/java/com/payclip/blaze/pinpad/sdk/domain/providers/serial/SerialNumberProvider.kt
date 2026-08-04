package com.payclip.blaze.pinpad.sdk.domain.providers.serial

/**
 * Provides the serial number identifier sent to the backend as `serial_number_pos`.
 */
internal interface SerialNumberProvider {

    fun getSerialNumber(): String
}
