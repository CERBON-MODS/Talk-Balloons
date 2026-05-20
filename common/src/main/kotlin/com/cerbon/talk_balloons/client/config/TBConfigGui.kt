package com.cerbon.talk_balloons.client.config

//? if < 1.21.11 {
import net.minecraft.resources.ResourceLocation as Identifier
//?} else {
/*import net.minecraft.resources.Identifier
 *///?}
import com.cerbon.talk_balloons.client.BalloonRenderer
import com.cerbon.talk_balloons.client.QueuedBalloonRender
import com.cerbon.talk_balloons.client.resources.BalloonStyleManager
import com.cerbon.talk_balloons.config.ITBConfig
import com.cerbon.talk_balloons.config.SynchronizedConfigType
import com.cerbon.talk_balloons.config.TBConfig
import com.cerbon.talk_balloons.config.TBConfigManager
import com.cerbon.talk_balloons.network.TBClientPacketHandler
import com.cerbon.talk_balloons.util.HistoricalData
import com.cerbon.talk_balloons.config.SynchronizedConfigData
import com.cerbon.talk_balloons.util.BalloonData
import com.cerbon.talk_balloons.util.TBConstants
import com.google.common.collect.Queues
import com.mojang.blaze3d.vertex.PoseStack
import dev.isxander.yacl3.api.Binding
import dev.isxander.yacl3.api.Controller
import dev.isxander.yacl3.api.Option
import dev.isxander.yacl3.api.controller.ControllerBuilder
import dev.isxander.yacl3.api.utils.Dimension
import dev.isxander.yacl3.dsl.*
import dev.isxander.yacl3.gui.AbstractWidget
import dev.isxander.yacl3.gui.YACLScreen
import dev.isxander.yacl3.gui.controllers.dropdown.AbstractDropdownController
import dev.isxander.yacl3.gui.controllers.dropdown.AbstractDropdownControllerElement
import dev.isxander.yacl3.gui.image.ImageRenderer
import net.minecraft.client.Minecraft
//? if < 26.1 {
import net.minecraft.client.gui.GuiGraphics
//? } else {
/*import net.minecraft.client.gui.GuiGraphicsExtractor as GuiGraphics
*///? }
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import java.awt.Color
import java.util.*
import java.util.concurrent.CompletableFuture
import kotlin.reflect.KProperty

private fun <T> bindingFromSunset(name: String): Binding<T> {
    val value = TBConfigManager.config.rootCategory.getValueById<T>(name)!!
    return Binding.generic(value.default, value::value) {
        value.value = it
    }
}

const val FULL_BRIGHT = 15728880

class GuiBalloonRenderer(private val config: ITBConfig, private val sneaking: Boolean = false) : ImageRenderer {
    val messages = HistoricalData<BalloonData>(3).apply {
        add(BalloonData.create(Component.translatable("talk_balloons.config.preview.short"), 0))
        add(BalloonData.create(Component.translatable("talk_balloons.config.preview.long"), 0))
        add(BalloonData.create(Component.translatable("talk_balloons.config.preview.tiny"), 0))
    }

    companion object {
        @JvmStatic val renderQueue = Queues.newConcurrentLinkedQueue<QueuedBalloonRender>()
    }

    override fun render(graphics: GuiGraphics, x: Int, y: Int, renderWidth: Int, tickDelta: Float): Int {
        val poseStack = PoseStack()
        poseStack.pushPose()

        val balloonHeight = BalloonRenderer.calculateEstimatedBalloonHeight(messages, Minecraft.getInstance().font, SynchronizedConfigData.EMPTY, config)
        val scaleDown = ((renderWidth - 5f) / config.maxBalloonWidth.toFloat()).coerceAtMost(1f)
        poseStack.translate(
            x.toFloat() + (renderWidth / 2f),
            y.toFloat() + (balloonHeight * scaleDown),
            9000f
        )
        poseStack.scale(-40f, -40f, -40f)
        poseStack.scale(scaleDown, scaleDown, 1f)

        BalloonRenderer.submitBalloons(poseStack, 0f, Minecraft.getInstance().font,
            messages,
            -config.balloonsHeightOffset, sneaking,
            SynchronizedConfigData.EMPTY, FULL_BRIGHT, renderQueue, config
        )

        //? if < 1.21.6 {
        BalloonRenderer.renderBalloons(renderQueue)
        //? }

        poseStack.popPose()

        return balloonHeight
    }

    override fun close() {
    }
}

class IdentifierDropdownControllerBuilder(val option: Option<Identifier>, val values: List<Identifier>, val translationKey: String) : ControllerBuilder<Identifier> {
    override fun build(): Controller<Identifier> {
        return IdentifierDropdownController(option, values, translationKey)
    }
}

class IdentifierDropdownController(option: Option<Identifier>, values: List<Identifier>, val translationKey: String) : AbstractDropdownController<Identifier>(option, values.map { it.toString() }) {
    override fun getString(): String {
        return this.option.pendingValue().toString()
    }

    override fun formatValue(): Component {
        if (this.string.isEmpty())
            return super.formatValue()

        return Component.translatableWithFallback(
            Identifier.tryParse(this.string)
                ?.toLanguageKey(this.translationKey) ?: return super.formatValue(),
            this.string
        )
    }

    override fun setFromString(value: String?) {
        if (value != null) {
            Identifier.tryParse(value)?.let {
                this.option.requestSet(it)
            }
        }
    }

    override fun provideWidget(screen: YACLScreen, widgetDimension: Dimension<Int>): AbstractWidget {
        return IdentifierDropdownControllerElement(this, screen, widgetDimension)
    }

    override fun getValidValue(value: String?, offset: Int): String {
        if (offset == -1) return this.string

        val valueLowerCase = value!!.lowercase()
        return getAllowedValues(value).stream()
            .filter { it.lowercase(Locale.getDefault()).contains(valueLowerCase) }
            .sorted { s1, s2 ->
                val s1LowerCase = s1.lowercase()
                val s2LowerCase = s2.lowercase()
                if (s1LowerCase.startsWith(valueLowerCase) && !s2LowerCase.startsWith(valueLowerCase)) return@sorted -1
                if (!s1LowerCase.startsWith(valueLowerCase) && s2LowerCase.startsWith(valueLowerCase)) return@sorted 1
                s1.compareTo(s2)
            }
            .skip(offset.toLong())
            .findFirst()
            .orElseGet(this::getString)
    }
}

class IdentifierDropdownControllerElement(private val controller: IdentifierDropdownController, screen: YACLScreen, dim: Dimension<Int>) : AbstractDropdownControllerElement<Identifier, String>(controller, screen, dim) {
    override fun computeMatchingValues(): List<String> {
        return this.controller.allowedValues
            .sortedWith { first, second ->
                if (first.startsWith(this.inputField) && !second.startsWith(this.inputField))
                    -1
                else if (!first.startsWith(this.inputField) && second.startsWith(this.inputField))
                    1
                else
                    first.compareTo(second)
            }
    }

    override fun getString(p0: String?): String? {
        return p0
    }

    override fun getValueText(): Component {
        if (this.inputField.isEmpty()
            || this.controller == null // how the hell?
        )
            return super.valueText

        if (this.inputFieldFocused)
            return Component.literal(this.inputField)

        return Component.translatableWithFallback(
            this.controller.option().pendingValue()
                .toLanguageKey(this.controller.translationKey),
            this.inputField
        )
    }
}

operator fun <T, V> CompletableFuture<OptionRegistrar>.getValue(thisRef: T, property: KProperty<*>): V {
    return this.futureRef<V>(property.name).get().pendingValue()
}

fun generateConfigGui(lastScreen: Screen?): Screen = YetAnotherConfigLib(TBConstants.MOD_ID) {
    save {
        TBConfigManager.config.save()
        TBClientPacketHandler.syncBalloonConfig()
    }

    val configHolder: ITBConfig = object : ITBConfig {
        override val balloonsHeightOffset: Float by categories["global"]["style"]
        override val distanceBetweenBalloons: Int by categories["global"]["style"]
        override val maxBalloons: Int by categories["global"]["preferences"]
        override val minBalloonWidth: Int by categories["global"]["style"]
        override val maxBalloonWidth: Int by categories["global"]["style"]
        override val balloonPadding: Int by categories["global"]["style"]
        override val balloonAge: Int by categories["global"]["preferences"]
        override val balloonStyle: ITBConfig.IdentifierHolder
            get() = categories["global"]["style"].futureRef<Identifier>("balloonStyle").get().pendingValue().holder
        override val textColor: Int
            get() = categories["global"]["style"].futureRef<Color>("textColor").get().pendingValue().rgb
        override val balloonTint: Int
            get() = categories["global"]["style"].futureRef<Color>("balloonTint").get().pendingValue().rgb
        override val balloonOpacity: Float by categories["global"]["style"]
        override val balloonSneakingOpacity: Float by categories["global"]["style"]
        override val showOwnBalloon: Boolean by categories["global"]["preferences"]
        override val onlyDisplayBalloons: Boolean by categories["global"]["preferences"]
        override val syncedConfigs: EnumSet<SynchronizedConfigType> by TBConfig::syncedConfigs
        override val balloonFadeOut: Float by categories["global"]["preferences"]
    }

    val global by categories.registering category@{
        val style by groups.registering {
            descriptionBuilder {
                customImage(GuiBalloonRenderer(configHolder))
            }

            val balloonStyle by options.registering<Identifier> {
                descriptionBuilder {
                    addDefaultText(1)
                    customImage(GuiBalloonRenderer(configHolder))
                }

                binding = bindingFromSunset<ITBConfig.IdentifierHolder>("balloonStyle")
                    .xmap(ITBConfig.IdentifierHolder::identifier, ITBConfig::IdentifierHolder)

                controller {
                    IdentifierDropdownControllerBuilder(it, BalloonStyleManager.styleIds.toList(), "talk_balloons.style")
                }
            }

            val textColor by options.registering<Color> {
                descriptionBuilder {
                    addDefaultText(1)
                    customImage(GuiBalloonRenderer(configHolder))
                }

                binding = bindingFromSunset<Int>("textColor")
                    .xmap(::Color, Color::getRGB)
                controller = colorPicker(false)
            }

            val balloonTint by options.registering<Color> {
                descriptionBuilder {
                    addDefaultText(1)
                    customImage(GuiBalloonRenderer(configHolder))
                }

                binding = bindingFromSunset<Int>("balloonTint")
                    .xmap(::Color, Color::getRGB)
                controller = colorPicker(false)

                available {
                    val id = balloonStyle.pendingValue()

                    if (id != null) {
                        return@available BalloonStyleManager.getStyleById(id).allowsTint
                    }

                    false
                }

                balloonStyle.addEventListener { option, _ ->
                    val id = option.pendingValue()
                    val tintRef = options.futureRef<Color>("balloonTint").get()

                    if (id != null) {
                        tintRef.setAvailable(BalloonStyleManager.getStyleById(id).allowsTint)
                    }
                }
            }

            val balloonOpacity by options.registering {
                descriptionBuilder {
                    addDefaultText(1)
                    customImage(GuiBalloonRenderer(configHolder))
                }

                binding = bindingFromSunset("balloonOpacity")
                controller = slider(0.15f..1f, step = 0.01f, formatter = { value ->
                    Component.literal("${(value * 100).toInt()}%")
                })
            }

            val balloonSneakingOpacity by options.registering {
                descriptionBuilder {
                    addDefaultText(1)
                    customImage(GuiBalloonRenderer(configHolder, true))
                }

                binding = bindingFromSunset("balloonSneakingOpacity")
                controller = slider(0.15f..1f, step = 0.01f, formatter = { value ->
                    Component.literal("${(value * 100).toInt()}%")
                })
            }

            val balloonsHeightOffset by options.registering {
                descriptionBuilder {
                    addDefaultText(1)
                    customImage(GuiBalloonRenderer(configHolder))
                }

                binding = bindingFromSunset("balloonsHeightOffset")
                controller = slider(range = -16f..16f, step = 0.1f, formatter = { value ->
                    Component.translatable("talk_balloons.config.unit.block${if (value == 1f) "" else "s"}", value)
                })
            }

            val distanceBetweenBalloons by options.registering {
                descriptionBuilder {
                    addDefaultText(1)
                    customImage(GuiBalloonRenderer(configHolder))
                }

                binding = bindingFromSunset("distanceBetweenBalloons")
                controller = slider(range = 0..20, formatter = { value ->
                    Component.translatable("talk_balloons.config.unit.pixel${if (value == 1) "" else "s"}", value)
                })
            }

            val minBalloonWidth by options.registering {
                descriptionBuilder {
                    addDefaultText(1)
                    customImage(GuiBalloonRenderer(configHolder))
                }

                binding = bindingFromSunset("minBalloonWidth")
                controller = slider(range = 8..512, step = 8, formatter = { value ->
                    Component.translatable("talk_balloons.config.unit.pixel${if (value == 1) "" else "s"}", value)
                })
            }

            val maxBalloonWidth by options.registering {
                descriptionBuilder {
                    addDefaultText(1)
                    customImage(GuiBalloonRenderer(configHolder))
                }

                binding = bindingFromSunset("maxBalloonWidth")
                controller = slider(range = 8..512, step = 8, formatter = { value ->
                    Component.translatable("talk_balloons.config.unit.pixel${if (value == 1) "" else "s"}", value)
                })
            }

            val balloonPadding by options.registering {
                descriptionBuilder {
                    addDefaultText(1)
                    customImage(GuiBalloonRenderer(configHolder))
                }

                binding = bindingFromSunset("balloonPadding")
                controller = slider(range = 0..64, step = 1, formatter = { value ->
                    Component.translatable("talk_balloons.config.unit.pixel${if (value == 1) "" else "s"}", value)
                })
            }
        }

        val preferences by groups.registering {
            val maxBalloons by options.registering {
                descriptionBuilder {
                    addDefaultText(1)
                }

                binding = bindingFromSunset("maxBalloons")
                controller = slider(range = 1..16, step = 1)
            }

            val balloonAge by options.registering {
                descriptionBuilder {
                    addDefaultText(1)
                }

                binding = bindingFromSunset("balloonAge")
                controller = slider(range = 0..120, step = 1, formatter = { value ->
                    if (value == 0)
                        Component.translatable("talk_balloons.config.always_display")
                    else
                        Component.translatable("talk_balloons.config.unit.second${if (value == 1) "" else "s"}", value)
                })
            }

            val balloonFadeOut by options.registering {
                descriptionBuilder {
                    addDefaultText(1)
                }

                binding = bindingFromSunset("balloonFadeOut")
                controller = slider(range = 0f..5f, step = 0.05f, formatter = { value ->
                    Component.translatable("talk_balloons.config.unit.second${if (value == 1f) "" else "s"}", value)
                })
            }

            val showOwnBalloon by options.registering {
                descriptionBuilder {
                    addDefaultText(1)
                }

                binding = bindingFromSunset("showOwnBalloon")
                controller = tickBox()
            }

            val onlyDisplayBalloons by options.registering {
                descriptionBuilder {
                    addDefaultText(1)
                }

                binding = bindingFromSunset("onlyDisplayBalloons")
                controller = tickBox()
            }
        }

        val syncedConfigs by groups.registering {
            descriptionBuilder {
                addDefaultText(1)
            }

            val sunsetValue = TBConfigManager.config.rootCategory.getValueById<EnumSet<SynchronizedConfigType>>("syncedConfigs")!!

            for (syncedType in SynchronizedConfigType.entries) {
                options.register<Boolean>(syncedType.serializedName) {
                    binding = Binding.generic(sunsetValue.default.contains(syncedType), {
                        sunsetValue.value.contains(syncedType)
                    }) {
                        val copied = EnumSet.copyOf(sunsetValue.value)
                        if (it)
                            copied.add(syncedType)
                        else
                            copied.remove(syncedType)

                        sunsetValue.value = copied
                    }
                    controller = tickBox()
                }
            }
        }
    }
}.generateScreen(lastScreen)
