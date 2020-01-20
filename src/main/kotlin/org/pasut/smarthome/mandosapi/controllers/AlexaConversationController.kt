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
    fun onPost(@RequestBody body: Map<String, *>, @RequestHeader headers: Map<String?, String?>?): ResponseEntity<String?>? {
        LOG.info("Entrando al post --> Body: {} --> headers: {}", body, headers)
        LOG.info("Version: {}", body.get("version"));
        val version = getRequestVersion(body);
        val requestType = getRequestType(body);

        var result: MutableMap<String, Object> = mutableMapOf();
        result.put("version", version as Object);

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
            if (getIntentName(body).equals("get_shopping_list")) {
                speech.put("type", "SSML");
                speech.put("ssml", showShoppingListProcessor.process());
                response.put("shouldEndSession", true as Object);
            }
            if (getIntentName(body).equals("AMAZON.HelpIntent")) {
                speech.put("text", "No se que decirte, yo tambien necesito ayuda.");
                response.put("shouldEndSession", false as Object);
            }
        }

        speech.put("playBehavior", "REPLACE_ENQUEUED");

        response.put("outputSpeech", speech as Object);

        result.put("response", response as Object);

        return ResponseEntity.ok(JSONObject(result).toString());
    }

    private fun getRequest(request: Map<String, *>): Map<String, *> {
        return request.get("request") as Map<String, *>;
    }

    private fun getRequestVersion(request: Map<String, *>):String {
        return request.get("version").toString();
    }

    private fun getRequestType(request: Map<String, *>): String {
        return getRequest(request).get("type").toString();
    }

    private fun getIntent(request: Map<String, *>):Map<String, String> {
        return getRequest(request).get("intent") as Map<String, String>;
    }

    private fun getIntentName(request: Map<String, *>):String {
        return getIntent(request).get("name").toString();
    }

}