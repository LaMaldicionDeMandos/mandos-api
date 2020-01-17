package org.pasut.smarthome.mandosapi.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.index.TextIndexed
import java.io.Serializable

@Document(collection = "shopping")
data class ShoppingListItem(@Id val id: String? = null, @TextIndexed(weight = 2f) val name:String = ""): Serializable {}