package org.pasut.smarthome.mandosapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MandosApiApplication

fun main(args: Array<String>) {
	runApplication<MandosApiApplication>(*args)
}
