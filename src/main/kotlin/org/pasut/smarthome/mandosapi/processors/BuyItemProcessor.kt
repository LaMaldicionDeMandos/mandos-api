package org.pasut.smarthome.mandosapi.processors

import org.pasut.smarthome.mandosapi.model.ShoppingListItem
import org.pasut.smarthome.mandosapi.services.ShoppingListService
import org.springframework.stereotype.Component

@Component
class BuyItemProcessor(private val service:ShoppingListService) {
    fun process(itemName: String):String {
        val item = service.newItem(ShoppingListItem(name = itemName))
        return "ok, se agregó ${item.name} a la lista de compras"
    }
}