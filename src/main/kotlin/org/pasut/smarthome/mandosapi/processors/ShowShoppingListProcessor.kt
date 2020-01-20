package org.pasut.smarthome.mandosapi.processors

import org.pasut.smarthome.mandosapi.model.ShoppingListItem
import org.pasut.smarthome.mandosapi.services.ShoppingListService
import org.springframework.stereotype.Component

@Component
class ShowShoppingListProcessor(private val service:ShoppingListService) {
    fun process():String {
        val items = service.list()
        if (items.isEmpty()) return "La lista de compras está vacía."
        var response = "<speak>Ok, tu lista de compras: "
        items.forEach { item:ShoppingListItem -> response+= "${item.name},<break time=300ms />" }
        return "$response</speak>"
    }
}