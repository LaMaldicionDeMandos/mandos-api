package org.pasut.smarthome.mandosapi.processors

import org.pasut.smarthome.mandosapi.model.ShoppingListItem
import org.pasut.smarthome.mandosapi.services.ShoppingListService
import org.springframework.stereotype.Component

@Component
class BuyItemProcessor(private val service:ShoppingListService) {
    val process = { itemName: String -> service.newItem(ShoppingListItem(name = itemName))}
}