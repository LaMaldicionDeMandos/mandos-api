package org.pasut.smarthome.mandosapi.processors

import org.pasut.smarthome.mandosapi.services.ShoppingListService
import org.springframework.stereotype.Component

@Component
class ClearShoppingListProcessor(private val service:ShoppingListService) {
    fun process():String {
        service.clear()
        return "ok, se vacio la lista de compras"
    }
}