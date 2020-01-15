package org.pasut.smarthome.mandosapi.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.util.*

@Configuration
class WebConfig : WebMvcConfigurer {
    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>?>) {
        converters.add(lowerCaseMessageConverter)
    }

    private val lowerCaseMessageConverter: MappingJackson2HttpMessageConverter
        get() {
            val mapper = ObjectMapper()
            mapper.propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            val converter = MappingJackson2HttpMessageConverter(mapper)
            converter.supportedMediaTypes = Arrays.asList(MediaType.ALL)
            return converter
        }
}