package org.pasut.smarthome.mandosapi.controllers

import com.google.actions.api.*
import org.json.JSONObject
import org.pasut.smarthome.mandosapi.processors.BuyItemProcessor
import org.pasut.smarthome.mandosapi.processors.ClearShoppingListProcessor
import org.pasut.smarthome.mandosapi.processors.DeleteItemProcessor
import org.pasut.smarthome.mandosapi.processors.ShowShoppingListProcessor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.ExecutionException


@RestController
class AlexaConversationController(private val buyItemProcessor: BuyItemProcessor,
                                  private val showShoppingListProcessor: ShowShoppingListProcessor,
                                  private val clearShoppingListProcessor: ClearShoppingListProcessor,
                                  private val deleteItemProcessor: DeleteItemProcessor) : DialogflowApp() {
    companion object {
        val LOG: Logger = LoggerFactory.getLogger(AlexaConversationController::class.java)
    }

    @PostMapping("conversation/alexa")
    @Throws(ExecutionException::class, InterruptedException::class)
    fun onPost(@RequestBody body: Map<String, Object>, @RequestHeader headers: Map<String?, String?>?): ResponseEntity<String?>? {
        LOG.info("Entrando al post --> Body: {} --> headers: {}", body, headers)
        LOG.info("Version: {}", body.get("version"));

        val request = body.get("request") as Map<String, String>;
        val requestType = request.get("type");

        var result: MutableMap<String, Object> = mutableMapOf();
        result.put("version", body.get("version")!!);
        var response: MutableMap<String, Object> = mutableMapOf();
        var speech: MutableMap<String, String> = mutableMapOf();
        speech.put("type", "PlainText");

        response.put("shouldEndSession", true as Object);
        speech.put("text", "Mmm no entendí.");

        if (requestType.equals("SessionEndedRequest")) {
            speech.put("text", "Muy bien, te quiero adioos!");
            response.put("shouldEndSession", true as Object);
        }

        if (requestType.equals("LaunchRequest")) {
            speech.put("text", "¿Que querés hacer?");
            response.put("shouldEndSession", false as Object);
        }

        if (requestType.equals("IntentRequest")) {
            val intent = request.get("intent") as Map<String, String>;
            if (intent.get("name").equals("get_shopping_list")) {
                speech.put("text", "Ok, te voy a mostrar la lista");
                response.put("shouldEndSession", true as Object);
            }
        }

        speech.put("playBehavior", "REPLACE_ENQUEUED");

        response.put("outputSpeech", speech as Object);

        result.put("response", response as Object);

        return ResponseEntity.ok(JSONObject(result).toString());
    }

    @ForIntent("buy item")
    fun buyItem(request: ActionRequest): ActionResponse? {
        LOG.info("buy item start.")

        val itemName = request.getParameter("item").toString()
        val response = buyItemProcessor.process(itemName)

        val responseBuilder = getResponseBuilder(request).add(response).endConversation()
        val actionResponse = responseBuilder.build()
        LOG.info("Response: {}", actionResponse.toString())
        LOG.info("buy item end.")
        return actionResponse
    }

    @ForIntent("show shopping list")
    fun showShoppingList(request: ActionRequest): ActionResponse? {
        LOG.info("show shopping list start.")

        val response = showShoppingListProcessor.process()
        val responseBuilder = getResponseBuilder(request).add(response).endConversation()
        val actionResponse = responseBuilder.build()
        LOG.info("Response: {}", actionResponse.toString())
        LOG.info("buy item end.")
        return actionResponse
    }

    @ForIntent("delete item")
    fun deleteItem(request: ActionRequest): ActionResponse? {
        LOG.info("delete item start.")

        val itemName = request.getParameter("item").toString()
        val response = deleteItemProcessor.process(itemName)

        val responseBuilder = getResponseBuilder(request).add(response).endConversation()
        val actionResponse = responseBuilder.build()
        LOG.info("Response: {}", actionResponse.toString())
        LOG.info("delete item end.")
        return actionResponse
    }

    @ForIntent("clear shopping list")
    fun clearShoppingList(request: ActionRequest): ActionResponse? {
        LOG.info("clear shopping list start.")

        val response = clearShoppingListProcessor.process()
        val responseBuilder = getResponseBuilder(request).add(response).endConversation()
        val actionResponse = responseBuilder.build()
        LOG.info("Response: {}", actionResponse.toString())
        LOG.info("clear shopping list end.")
        return actionResponse
    }
}