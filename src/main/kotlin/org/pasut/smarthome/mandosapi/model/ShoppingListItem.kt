package org.pasut.smarthome.mandosapi.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable

@Document(collection = "shopping")
data class ShoppingListItem(@Id val id: String?, val name:String = ""): Serializable {}