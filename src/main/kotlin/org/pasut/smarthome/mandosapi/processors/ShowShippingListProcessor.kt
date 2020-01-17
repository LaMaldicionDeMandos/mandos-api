package org.pasut.smarthome.mandosapi.processors

import org.pasut.smarthome.mandosapi.model.ShoppingListItem
import org.pasut.smarthome.mandosapi.services.ShoppingListService
import org.springframework.stereotype.Component

@Component
class ShowShippingListProcessor(private val service:ShoppingListService) {
    fun process():String {
        val items = service.list()
        var response = "<speak>Ok, tu lista de compras <seq>"
        items.forEach { item:ShoppingListItem -> response+= "<media begin=\"0.5s\"><speak>${item.name}</speak></media>" }
        return "$response</seq></speak>"
    }
}