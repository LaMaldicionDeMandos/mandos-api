package org.pasut.smarthome.mandosapi.controllers

import com.google.actions.api.*
import org.json.JSONObject
import org.pasut.smarthome.mandosapi.processors.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.net.URL
import java.util.concurrent.ExecutionException


@RestController
class AlexaConversationController(private val buyItemProcessor: BuyItemProcessor,
                                  private val showShoppingListProcessor: ShowShoppingListProcessor,
                                  private val clearShoppingListProcessor: ClearShoppingListProcessor,
                                  private val deleteItemProcessor: DeleteItemProcessor,
                                  private val lastPowerOnProcessor: LastPowerOnProcessor) : DialogflowApp() {
    companion object {
        val LOG: Logger = LoggerFactory.getLogger(AlexaConversationController::class.java)
    }

    @PostMapping("conversation/alexa")
    @Throws(ExecutionException::class, InterruptedException::class)
    fun onPost(@RequestBody body: Map<String, *>, @RequestHeader headers: Map<String, String>): ResponseEntity<String?> {
        LOG.info("Entrando al post --> Body: {} --> headers: {}", body, headers)
        LOG.info("Version: {}", body["version"]);

        val version = getRequestVersion(body);
        val requestType = getRequestType(body);

        var builder = ResponseBuilder(version);

        if (requestType == "SessionEndedRequest") {
            builder.text("Muy bien, te quiero adioos!");
        }

        if (requestType == "LaunchRequest") {
            if (isNewUser(body)) builder.text("Te doy la bienvenida, me dicen dDon Mandos, te explicaré algunas cosas que podes pedirme, " +
                    "podes ver tu lista de compras, agregar nuevos items para comprar, eliminar de la lista de compras y limpiar la lista una vez hayas realizado las compras. " +
                    "También podes pedir ayuda diciendo al palabra \"ayuda.\", ¿Que querés hacer ahora?").shouldEnd(false);
            else builder.text("Hola, que alegría volver a escucharte. ¿Que querés hacer?")
        }

        if (requestType == "IntentRequest") {
            when (getIntentName(body)) {
                "get_shopping_list" -> builder.ssml(showShoppingListProcessor.process());
                "add_item" -> builder.text(buyItemProcessor.process(getItem(body)));
                "delete_item" -> builder.text(deleteItemProcessor.process(getItem(body)));
                "clear_list" -> builder.text(clearShoppingListProcessor.process());
                "last_power_on" -> builder.text(lastPowerOnProcessor.process(getDevice(body)));
                "AMAZON.HelpIntent" -> builder.text("Te voy a decir alguna frases que podes usar..." +
                        "Decí \"Ver mis compras\" y te diré los items que agendaste. " +
                        "Podes decir \"quiero comprar frutas, para agregar frutas a tu lista de compras. " +
                        "También podes decir \"eliminá frutas\", para quitar las frutas de tu lista de compras.").shouldEnd(false);
                "AMAZON.CancelIntent" -> builder.text("Ok, acción cancelada.");
            }
        }

        return ResponseEntity.ok(JSONObject(builder.build()).toString());
    }

    private fun isNewUser(request: Map<String, *>): Boolean {
        return getSession(request).get("new") as Boolean;
    }

    private fun getItem(request: Map<String, *>):String {
        return getSlot(request, "item") as String;
    }

    private fun getDevice(request: Map<String, *>):String {
        return getSlot(request, "device") as String;
    }

    private fun getSlot(request: Map<String, *>, attributeName:String): Any? {
        return (getSlots(request)[attributeName] as Map<String, *>)["value"];
    }

    private fun getSlots(request: Map<String, *>):Map<String, *> {
        return getIntent(request)["slots"] as Map<String, *>;
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