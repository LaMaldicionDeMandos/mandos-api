package org.pasut.smarthome.mandosapi.controllers

import org.pasut.smarthome.mandosapi.processors.LastPowerOnProcessor
import org.pasut.smarthome.mandosapi.services.DeviceMonitorService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping(value = ["/devices"], produces = [APPLICATION_JSON_VALUE], consumes = [APPLICATION_JSON_VALUE])
class DeviceMonitorController(private val devicesService: DeviceMonitorService, private val processor: LastPowerOnProcessor) {
    companion object {
        val LOG:Logger = LoggerFactory.getLogger(DeviceMonitorController::class.java)
    }

    @GetMapping("on")
    fun lastPowerOn(@RequestParam("name") name: String):ResponseEntity<String> {
        LOG.info("last power on of {}", name);
        return ResponseEntity.ok(processor.process(name));
    }
}