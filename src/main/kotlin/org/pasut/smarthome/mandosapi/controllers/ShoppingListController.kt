package org.pasut.smarthome.mandosapi.controllers

import org.pasut.smarthome.mandosapi.model.ShoppingListItem
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping(value = ["/shoppinglist"], produces = [APPLICATION_JSON_VALUE], consumes = [APPLICATION_JSON_VALUE])
class ShoppingListController {
    companion object {
        val LOG:Logger = LoggerFactory.getLogger(ShoppingListController.javaClass)
    }

    @PostMapping("/item")
    fun newItem(@RequestBody item:ShoppingListItem):ResponseEntity<Void> {
        LOG.info("Nuevo item {}", item.name)
        return ResponseEntity.status(201).build()
    }
}