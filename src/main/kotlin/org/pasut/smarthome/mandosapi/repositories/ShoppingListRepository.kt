package org.pasut.smarthome.mandosapi.repositories

import org.pasut.smarthome.mandosapi.model.ShoppingListItem
import org.springframework.stereotype.Repository
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.core.query.TextCriteria

@Repository
interface ShoppingListRepository : MongoRepository<ShoppingListItem, String> {
    fun findAllBy(criteria:TextCriteria):List<ShoppingListItem>
}