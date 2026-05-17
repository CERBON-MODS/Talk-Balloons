package com.cerbon.talk_balloons.client.config

import com.cerbon.talk_balloons.TalkBalloons
import com.cerbon.talk_balloons.client.BalloonRenderer
import com.cerbon.talk_balloons.client.resources.BalloonStyleManager
import com.cerbon.talk_balloons.config.ITBConfig
import com.cerbon.talk_balloons.config.SynchronizedConfigType
import com.cerbon.talk_balloons.config.TBConfig
import com.cerbon.talk_balloons.config.TBConfigManager
import com.cerbon.talk_balloons.network.TBClientPacketHandler
import com.cerbon.talk_balloons.util.HistoricalData
import com.cerbon.talk_balloons.util.SynchronizedConfigData
import com.cerbon.talk_balloons.util.TBConstants
import com.google.common.collect.Queues
import com.mojang.blaze3d.vertex.MeshData
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import dev.isxander.yacl3.api.Binding
import dev.isxander.yacl3.api.ConfigCategory
import dev.isxander.yacl3.api.Controller
import dev.isxander.yacl3.api.Option
import dev.isxander.yacl3.api.OptionEventListener
import dev.isxander.yacl3.api.controller.ControllerBuilder
import dev.isxander.yacl3.api.controller.DropdownStringControllerBuilder
import dev.isxander.yacl3.api.utils.Dimension
import dev.isxander.yacl3.dsl.*
import dev.isxander.yacl3.gui.AbstractWidget
import dev.isxander.yacl3.gui.YACLScreen
import dev.isxander.yacl3.gui.controllers.dropdown.AbstractDropdownController
import dev.isxander.yacl3.gui.controllers.dropdown.AbstractDropdownControllerElement
import dev.isxander.yacl3.gui.image.ImageRenderer
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
//? if < 1.21.11 {
import net.minecraft.resources.ResourceLocation as Identifier
//?} else {
/*import net.minecraft.resources.Identifier
 *///?}
import java.awt.Color
import java.util.EnumSet
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
    val messages = HistoricalData<Component>(3).apply {
        add(Component.literal("Short text"))
        add(Component.literal("Much longer text for comedic effect and for spacing reasons, I guess. How many characters do we think can fit here?"))
        add(Component.literal("..."))
    }

    override fun render(graphics: GuiGraphics, x: Int, y: Int, renderWidth: Int, tickDelta: Float): Int {
        val poseStack = PoseStack()
        poseStack.pushPose()

        val scaleDown = ((renderWidth - 5f) / config.maxBalloonWidth.toFloat()).coerceAtMost(1f)
        poseStack.translate(
            x.toFloat() + (renderWidth / 2f),
            y.toFloat() + (BalloonRenderer.calculateEstimatedBalloonHeight(messages, Minecraft.getInstance().font, SynchronizedConfigData.EMPTY, config) * scaleDown),
            9000f
        )
        poseStack.scale(-40f, -40f, -40f)
        poseStack.scale(scaleDown, scaleDown, 1f)

        val renderQueue = Queues.newConcurrentLinkedQueue<MeshData>()
        BalloonRenderer.submitBalloons(poseStack, 0f, Minecraft.getInstance().font,
            messages,
            -config.balloonsHeightOffset, sneaking,
            SynchronizedConfigData.EMPTY, FULL_BRIGHT, renderQueue, config
        )

        BalloonRenderer.renderBalloons(renderQueue)

        poseStack.popPose()

        return 100
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

fun generateConfigGui(lastScreen: Screen?) = YetAnotherConfigLib(TBConstants.MOD_ID) {
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
        override val balloonOpacity: Int by categories["global"]["style"]
        override val balloonSneakingOpacity: Int by categories["global"]["style"]
        override val showOwnBalloon: Boolean by categories["global"]["preferences"]
        override val onlyDisplayBalloons: Boolean by categories["global"]["preferences"]
        override val syncedConfigs: EnumSet<SynchronizedConfigType> by TBConfig::syncedConfigs
    }

    val global by categories.registering category@{
        val style by groups.registering {
            descriptionBuilder {
                customImage(GuiBalloonRenderer(configHolder))
            }

            val balloonStyle by options.registering<Identifier> {
                descriptionBuilder {
                    customImage(GuiBalloonRenderer(configHolder))
                }

                binding = bindingFromSunset<ITBConfig.IdentifierHolder>("balloonStyle")
                    .xmap(ITBConfig.IdentifierHolder::identifier, ITBConfig::IdentifierHolder)

                controller {
                    IdentifierDropdownControllerBuilder(it, BalloonStyleManager.styleIds.toList(), "")
                }
            }

            val textColor by options.registering<Color> {
                descriptionBuilder {
                    customImage(GuiBalloonRenderer(configHolder))
                }

                binding = bindingFromSunset<Int>("textColor")
                    .xmap(::Color, Color::getRGB)
                controller = colorPicker(false)
            }

            val balloonTint by options.registering<Color> {
                descriptionBuilder {
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

                    if (id != null) {
                        option.setAvailable(BalloonStyleManager.getStyleById(id).allowsTint)
                    }
                }
            }

            val balloonOpacity by options.registering {
                descriptionBuilder {
                    customImage(GuiBalloonRenderer(configHolder))
                }

                binding = bindingFromSunset("balloonOpacity")
                controller = slider(30..255)
            }

            val balloonSneakingOpacity by options.registering {
                descriptionBuilder {
                    customImage(GuiBalloonRenderer(configHolder, true))
                }

                binding = bindingFromSunset("balloonSneakingOpacity")
                controller = slider(30..255)
            }

            val balloonsHeightOffset by options.registering {
                descriptionBuilder {
                    customImage(GuiBalloonRenderer(configHolder))
                }

                binding = bindingFromSunset("balloonsHeightOffset")
                controller = slider(range = 0f..16f, step = 0.1f)
            }

            val distanceBetweenBalloons by options.registering {
                descriptionBuilder {
                    customImage(GuiBalloonRenderer(configHolder))
                }

                binding = bindingFromSunset("distanceBetweenBalloons")
                controller = slider(range = 0..20)
            }

            val minBalloonWidth by options.registering {
                descriptionBuilder {
                    customImage(GuiBalloonRenderer(configHolder))
                }

                binding = bindingFromSunset("minBalloonWidth")
                controller = slider(range = 8..512, step = 8)
            }

            val maxBalloonWidth by options.registering {
                descriptionBuilder {
                    customImage(GuiBalloonRenderer(configHolder))
                }

                binding = bindingFromSunset("maxBalloonWidth")
                controller = slider(range = 8..512, step = 8)
            }

            val balloonPadding by options.registering {
                descriptionBuilder {
                    customImage(GuiBalloonRenderer(configHolder))
                }

                binding = bindingFromSunset("balloonPadding")
                controller = slider(range = 0..64, step = 1)
            }
        }

        val preferences by groups.registering {
            val maxBalloons by options.registering {
                binding = bindingFromSunset("maxBalloons")
                controller = slider(range = 1..16, step = 1)
            }

            val balloonAge by options.registering {
                binding = bindingFromSunset("balloonAge")
                controller = slider(range = 0..120, step = 1)
            }

            val showOwnBalloon by options.registering {
                binding = bindingFromSunset("showOwnBalloon")
                controller = tickBox()
            }

            val onlyDisplayBalloons by options.registering {
                binding = bindingFromSunset("onlyDisplayBalloons")
                controller = tickBox()
            }
        }

        val syncedConfigs by groups.registering {
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
