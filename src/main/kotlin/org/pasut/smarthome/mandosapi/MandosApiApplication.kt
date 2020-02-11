package org.pasut.smarthome.mandosapi

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate


@SpringBootApplication
class MandosApiApplication {
	@Primary
	@Bean
	fun restTemplate(): RestTemplate? {
		val restTemplate = RestTemplate()
		/*
		val messageConverters: MutableList<HttpMessageConverter<*>> = mutableListOf()
		val jsonMessageConverter = MappingJackson2HttpMessageConverter()
		val mapper = ObjectMapper()
		mapper.propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
		jsonMessageConverter.objectMapper = mapper
		messageConverters.add(jsonMessageConverter)
		restTemplate.messageConverters = messageConverters
		 */
		return restTemplate
	}
}

fun main(args: Array<String>) {
	runApplication<MandosApiApplication>(*args)
}
