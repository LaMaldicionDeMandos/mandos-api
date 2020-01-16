package org.pasut.smarthome.mandosapi.services

import org.pasut.smarthome.mandosapi.model.ShoppingListItem
import org.pasut.smarthome.mandosapi.repositories.ShoppingListRepository
import org.springframework.data.mongodb.core.query.TextCriteria
import org.springframework.stereotype.Service

@Service
class ShoppingListService(private val repo:ShoppingListRepository) {

    val newItem = { item: ShoppingListItem -> repo.save(item) }
    val list = { repo.findAll() }
    val clear = { repo.deleteAll() }
    fun delete(name: String):ShoppingListItem? {
        val items = repo.findAllBy(TextCriteria.forDefaultLanguage()
                .caseSensitive(false).matching(name))
        if (items.isEmpty()) return null
        val item = items.first()
        repo.delete(item)
        return item
    }
}