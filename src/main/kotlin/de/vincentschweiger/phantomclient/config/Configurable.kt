package de.vincentschweiger.phantomclient.config

import net.minecraft.block.Block
import net.minecraft.item.Item
import de.vincentschweiger.phantomclient.module.Module

open class Configurable(name: String, value: MutableList<Value<*>> = mutableListOf(), valueType: ValueType = ValueType.CONFIGURABLE) :
        Value<MutableList<Value<*>>>(name, value = value, valueType) {

    open fun initConfigurable() {
        value.filterIsInstance<Configurable>().forEach {
            it.initConfigurable()
        }
    }

    @get:JvmName("getContainedValues")
    val containedValues: Array<Value<*>>
        get() = this.value.toTypedArray()

    fun getContainedValuesRecursively(): Array<Value<*>> {
        val output = mutableListOf<Value<*>>()

        this.getContainedValuesRecursivelyInternal(output)

        return output.toTypedArray()
    }

    fun getContainedValuesRecursivelyInternal(output: MutableList<Value<*>>) {
        for (currentValue in this.value) {
            if (currentValue is ToggleableConfigurable) {
                output.add(currentValue)
                output.addAll(currentValue.value.filter { it.name.equals("Enabled", true) })
            } else {
                if (currentValue is Configurable) {
                    currentValue.getContainedValuesRecursivelyInternal(output)
                } else {
                    output.add(currentValue)
                }
            }

            if (currentValue is ChoiceConfigurable) {
                output.add(currentValue)

                currentValue.choices.filter { it.isActive }.forEach {
                    it.getContainedValuesRecursivelyInternal(output)
                }
            }
        }
    }

    // Common value types

    protected fun <T : Configurable> tree(configurable: T): T {
        value.add(configurable)
        return configurable
    }

    protected fun <T : Any> value(name: String, default: T, valueType: ValueType = ValueType.INVALID) =
            Value(name, default, valueType).apply { this@Configurable.value.add(this) }

    protected fun <T : Any> rangedValue(name: String, default: T, range: ClosedRange<*>, valueType: ValueType) =
            RangedValue(name, default, range, valueType).apply { this@Configurable.value.add(this) }

    // Fixed data types

    protected fun boolean(name: String, default: Boolean) = value(name, default, ValueType.BOOLEAN)

    protected fun double(name: String, default: Double) = value(name, default, ValueType.DOUBLE)

    protected fun float(name: String, default: Float, range: ClosedFloatingPointRange<Float>) =
            rangedValue(name, default, range, ValueType.FLOAT)

    protected fun floatRange(
            name: String,
            default: ClosedFloatingPointRange<Float>,
            range: ClosedFloatingPointRange<Float>
    ) = rangedValue(name, default, range, ValueType.FLOAT_RANGE)

    protected fun int(name: String, default: Int, range: IntRange) = rangedValue(name, default, range, ValueType.INT)

    protected fun intRange(name: String, default: IntRange, range: IntRange) = rangedValue(name, default, range, ValueType.INT_RANGE)

    protected fun text(name: String, default: String) = value(name, default, ValueType.TEXT)

    protected fun curve(name: String, default: Array<Float>) = value(name, default, ValueType.CURVE)

    protected fun block(name: String, default: Block) = value(name, default, ValueType.BLOCK)

    protected fun item(name: String, default: Item) = value(name, default, ValueType.ITEM)

    protected fun <T : NamedChoice> enumChoice(name: String, default: T, choices: Array<T>) =
            ChooseListValue(name, default, choices).apply { this@Configurable.value.add(this) }

    protected fun Module.choices(name: String, active: Choice, choices: Array<Choice>) =
            ChoiceConfigurable(this, name, active) { choices }.apply { this@Configurable.value.add(this) }

    protected fun Module.choices(name: String, active: Choice, choicesCallback: (ChoiceConfigurable) -> Array<Choice>) =
            ChoiceConfigurable(this, name, active, choicesCallback).apply { this@Configurable.value.add(this) }

}