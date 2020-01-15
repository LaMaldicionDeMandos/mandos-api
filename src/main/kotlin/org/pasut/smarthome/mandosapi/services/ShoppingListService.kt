package org.pasut.smarthome.mandosapi.services

import org.pasut.smarthome.mandosapi.model.ShoppingListItem
import org.pasut.smarthome.mandosapi.repositories.ShoppingListRepository
import org.springframework.stereotype.Service

@Service
class ShoppingListService(private val repo:ShoppingListRepository) {

    val newItem = { item: ShoppingListItem -> repo.save(item) }
}