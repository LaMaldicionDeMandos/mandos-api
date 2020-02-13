package org.pasut.smarthome.mandosapi.processors

import org.pasut.smarthome.mandosapi.services.DeviceMonitorService
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId

@Component
class LastPowerOnProcessor(private val service:DeviceMonitorService) {
    companion object {
        val months = listOf("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")
    }
    fun process(deviceName: String):String {
        val _deviceName = deviceName.replace(" la "," ").replace(" de ", " ").replace(" el ", " ").replace(" del ", " ")
        val devicePowerDate = service.lastPowerOn(_deviceName);
        return "${deviceName} se prendion por ultima vez ${processYear(LocalDateTime.now(), devicePowerDate.toInstant().atZone(ZoneId.of(ZoneId.SHORT_IDS["AGT"])).toLocalDateTime())}"
    }

    private fun processYear(now: LocalDateTime, date:LocalDateTime): String {
        if (now.year.equals(date.year)) return processMonth(now, date, "")
        if (now.year.equals(date.year + 1)) return processMonth(now, date, "El año pasado")
        return processMonth(now, date, " ") + " del año ${date.year}"
    }

    private fun processMonth(now: LocalDateTime, date: LocalDateTime, prefix: String): String {
        if (prefix.isEmpty() && now.month.equals(date.month)) return processDay(now, date, "")
        return "${prefix} ${processDay(now, date, prefix)} de ${months[date.month.value - 1]}"
    }

    private fun processDay(now: LocalDateTime, date: LocalDateTime, prefix: String): String {
        if (prefix.isEmpty() && now.dayOfMonth == date.dayOfMonth) return processHour(now, date, "")
        if (prefix.isEmpty() && now.dayOfMonth == date.dayOfMonth + 1) return processHour(now, date, "Ayer")
        return processHour(now, date, "${prefix} el ${date.dayOfMonth}")
    }

    private fun processHour(now: LocalDateTime, date: LocalDateTime, prefix: String): String {
        if (prefix.isEmpty() && now.hour <= date.hour + 2) return "Hace ${(now.hour - date.hour)*60 + now.minute - date.minute} minutos"
        return "${prefix} a las ${date.hour} y ${date.minute}"
    }
}