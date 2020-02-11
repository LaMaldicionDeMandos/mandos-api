package org.pasut.smarthome.mandosapi.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.Date

@JsonIgnoreProperties(ignoreUnknown = true)
data class DevicePowerDate(val _id: String? = "", val date:Date? = Date())