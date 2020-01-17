package org.pasut.smarthome.mandosapi.controllers

import com.google.actions.api.*
import org.pasut.smarthome.mandosapi.processors.BuyItemProcessor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.ExecutionException


@RestController
class GoogleHomeConversationController(private val buyItemProcessor: BuyItemProcessor) : DialogflowApp() {
    companion object {
        val LOG: Logger = LoggerFactory.getLogger(GoogleHomeConversationController::class.java)
    }

    @PostMapping("conversation/googlehome")
    @Throws(ExecutionException::class, InterruptedException::class)
    fun onPost(@RequestBody body: String?, @RequestHeader headers: Map<String?, String?>?): ResponseEntity<String?>? {
        LOG.info("Entrando al post --> Body: {} --> headers: {}", body, headers)
        val response = handleRequest(body, headers).get()
        LOG.info("Response: {}", response)
        return ResponseEntity(response, HttpStatus.OK)
    }

    @ForIntent("buy item")
    fun buyItem(request: ActionRequest): ActionResponse? {
        LOG.info("buy item start.")

        val itemName = request.getParameter("item").toString()
        val item = buyItemProcessor.process(itemName)
        val response = "ok, se agregó ${item.name} a la lista de compras"
        val responseBuilder = getResponseBuilder(request).add(response).endConversation()
        val actionResponse = responseBuilder.build()
        LOG.info("Response: {}", actionResponse.toString())
        LOG.info("buy item end.")
        return actionResponse
    }
}