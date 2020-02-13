package org.pasut.smarthome.mandosapi.services

import org.pasut.smarthome.mandosapi.model.DevicePowerDate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.*

@Service
class DeviceMonitorService(private val restTemplate: RestTemplate,
                           @Value("\${app.devices.api.url}") private val deviceUrl: String) {

    fun lastPowerOn(name: String): Date {
        val date = restTemplate.getForObject(deviceUrl.plus("/event/on?name=${name}"), DevicePowerDate::class.java)
        return date?.date!!
    }
}