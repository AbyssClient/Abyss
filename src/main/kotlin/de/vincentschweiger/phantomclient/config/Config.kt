package de.vincentschweiger.phantomclient.config

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.*
import java.io.ObjectInputFilter.Config
import java.lang.reflect.Field
import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaSetter

/**
 * @author Sk1er
 */
class DefaultConfig(private val file: File) {
    private val gson = GsonBuilder().setPrettyPrinting().create()
    private val configObjects: MutableList<Any> = ArrayList()
    var config = JsonObject()

    init {
        try {
            if (file.exists()) {
                val fr = FileReader(file)
                val br = BufferedReader(fr)
                config = JsonParser().parse(br.lines().collect(Collectors.joining())).asJsonObject
                fr.close()
                br.close()
            } else {
                config = JsonObject()
                saveFile()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveFile() {
        try {
            file.createNewFile()
            val fw = FileWriter(file)
            val bw = BufferedWriter(fw)
            bw.write(gson.toJson(config))
            bw.close()
            fw.close()
        } catch (ignored: Exception) {
        }
    }

    fun save() {
        configObjects.forEach(Consumer { o: Any -> saveToJsonFromRamObject(o) })
        saveFile()
    }

    fun register(o: Any): Any {
        var found = false
        for (member in o::class.declaredMemberProperties) {
            if (member.findAnnotation<ConfigOpt>() != null) {
                found = true
                break
            }
        }
        if (!found) return o
        if (o is PreConfigHandler) o.preUpdate()
        loadToClass(o)
        configObjects.add(o)
        if (o is PostConfigHandler) o.postUpdate()
        return o
    }

    private fun loadToClass(o: Any) {
        loadToClassObject(o)
    }

    private fun loadToClassObject(o: Any) {
        val c: KClass<*> = o::class
        if (!config.has(c.simpleName)) config.add(c.simpleName, JsonObject())

        for (member in c.declaredMemberProperties.stream().filter { it.hasAnnotation<ConfigOpt>() && config.has(c.simpleName) }) {
            member.isAccessible = true
            val tmp = config[c.simpleName].asJsonObject
            if (tmp.has(member.name)) {
                try {
                    gson.fromJson(tmp[member.name], member.returnType.javaClass)
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
            }
        }

        Arrays.stream(c.java.declaredFields).filter { f: Field -> f.isAnnotationPresent(ConfigOpt::class.java) && config.has(c.simpleName) }.forEach { f: Field ->
            f.isAccessible = true
            val co = f.getAnnotation(ConfigOpt::class.java)
            val tmp = config[c.simpleName].asJsonObject
            if (tmp.has(f.name)) {
                try {
                    f[o] = gson.fromJson(tmp[f.name], f.type)
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun saveToJsonFromRamObject(o: Any) {
        loadToJson(o)
    }

    private fun loadToJson(o: Any) {
        if (o is PreSaveHandler) o.preSave()
        val c: Class<*> = o.javaClass
        Arrays.stream(c.declaredFields).filter { f: Field -> f.isAnnotationPresent(ConfigOpt::class.java) && config.has(c.name) }.forEach { f: Field ->
            f.isAccessible = true
            val classObject = config[c.name].asJsonObject
            try {
                classObject.add(f.name, gson.toJsonTree(f[o], f.type))
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }
    }
}

interface PostConfigHandler {
    fun postUpdate()
}

interface PreConfigHandler {
    fun preUpdate()
}

interface PreSaveHandler {
    fun preSave()
}