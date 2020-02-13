package org.pasut.smarthome.mandosapi.processors

import org.pasut.smarthome.mandosapi.services.DeviceMonitorService
import org.springframework.stereotype.Component

@Component
class LastPowerOnProcessor(private val service:DeviceMonitorService) {
    fun process(deviceName: String):String {
        val _deviceName = deviceName.replace("la ","").replace("de ", "").replace("el ", "").replace("del ", "")
        val devicePowerDate = service.lastPowerOn(_deviceName);
        return devicePowerDate
    }
}