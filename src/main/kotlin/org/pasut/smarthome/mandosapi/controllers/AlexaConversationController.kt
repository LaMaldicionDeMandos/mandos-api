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
        LOG.info("Version: {}", body["version"]);
        val version = getRequestVersion(body);
        val requestType = getRequestType(body);

        var builder = ResponseBuilder(version);

        if (requestType == "SessionEndedRequest") {
            builder.text("Muy bien, te quiero adioos!");
        }

        if (requestType == "LaunchRequest") {
            builder.text("¿Que querés hacer?").shouldEnd(false);
        }

        if (requestType == "IntentRequest") {
            when (getIntentName(body)) {
                "get_shopping_list" -> builder.ssml(showShoppingListProcessor.process());
                "add_item" -> builder.text(buyItemProcessor.process(getItem(body)));
                "delete_item" -> builder.text(deleteItemProcessor.process(getItem(body)));
                "clear_list" -> builder.text(clearShoppingListProcessor.process());
                "AMAZON.HelpIntent" -> builder.text("No se que decirte, yo tambien necesito ayuda.").shouldEnd(false);
                "AMAZON.CancelIntent" -> builder.text("Ok, acción cancelada.");
            }
        }

        return ResponseEntity.ok(JSONObject(builder.build()).toString());
    }

    private fun getItem(request: Map<String, *>):String {
        return getAttribute(request, "item") as String;
    }

    private fun getAttribute(request: Map<String, *>, attributeName:String): Any? {
        return getAttributes(request)[attributeName];
    }

    private fun getAttributes(request: Map<String, *>):Map<String, *> {
        return getSession(request)["attributes"] as Map<String, *>;
    }

    private fun getSession(request: Map<String, *>): Map<String, *> {
        return request["session"] as Map<String, *>;
    }

    private fun getRequest(request: Map<String, *>): Map<String, *> {
        return request["request"] as Map<String, *>;
    }

    private fun getRequestVersion(request: Map<String, *>):String {
        return request["version"].toString();
    }

    private fun getRequestType(request: Map<String, *>): String {
        return getRequest(request)["type"].toString();
    }

    private fun getIntent(request: Map<String, *>):Map<String, String> {
        return getRequest(request)["intent"] as Map<String, String>;
    }

    private fun getIntentName(request: Map<String, *>):String {
        return getIntent(request)["name"].toString();
    }

}

class ResponseBuilder {
    private val result: MutableMap<String, Object> = mutableMapOf();
    private val response: MutableMap<String, Object> = mutableMapOf();
    private val speech: MutableMap<String, String> = mutableMapOf();

    constructor(version:String) {
        speech["type"] = "PlainText";
        speech["text"] = "Mmm no entendí.";
        speech["playBehavior"] = "REPLACE_ENQUEUED";

        response["shouldEndSession"] = true as Object;

        result["version"] = version as Object;
    }

    fun text(text: String): ResponseBuilder {
        speech["type"] = "PlainText";
        speech["text"] = text;
        return this;
    }

    fun ssml(text: String): ResponseBuilder {
        speech["type"] = "SSML";
        speech["ssml"] = text;
        return this;
    }

    fun shouldEnd(value: Boolean): ResponseBuilder {
        response["shouldEndSession"] = value as Object;
        return this;
    }

    fun build():Map<String, Object> {
        response["outputSpeech"] = speech as Object;
        result["response"] = response as Object;
        return result;
    }
}