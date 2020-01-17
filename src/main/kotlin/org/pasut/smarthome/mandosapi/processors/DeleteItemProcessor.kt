package org.pasut.smarthome.mandosapi.processors

import org.pasut.smarthome.mandosapi.services.ShoppingListService
import org.springframework.stereotype.Component

@Component
class DeleteItemProcessor(private val service:ShoppingListService) {
    fun process(itemName: String):String {
        val item = service.delete(itemName)
        return if (item == null) "No encontré el item ${itemName}." else "ok, se sacó de la lista ${item.name}."
    }
}