package com.cerbon.talk_balloons.client.config

import com.cerbon.talk_balloons.client.resources.BalloonStyleManager
import com.cerbon.talk_balloons.config.SynchronizedConfigType
import com.cerbon.talk_balloons.config.TBConfig
import com.cerbon.talk_balloons.config.TBConfigManager
import com.cerbon.talk_balloons.network.TBClientPacketHandler
import com.cerbon.talk_balloons.util.TBConstants
import dev.isxander.yacl3.api.Binding
import dev.isxander.yacl3.api.controller.DropdownStringControllerBuilder
import dev.isxander.yacl3.dsl.*
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
//? if < 1.21.11 {
import net.minecraft.resources.ResourceLocation as Identifier
//?} else {
/*import net.minecraft.resources.Identifier
 *///?}
import java.awt.Color
import java.util.EnumSet

private fun <T> bindingFromSunset(name: String): Binding<T> {
    val value = TBConfigManager.config.rootCategory.getValueById<T>(name)!!
    return Binding.generic(value.default, value::value) {
        value.value = it
    }
}

fun generateConfigGui(lastScreen: Screen?) = YetAnotherConfigLib(TBConstants.MOD_ID) {
    save {
        TBConfigManager.config.save()
        TBClientPacketHandler.syncBalloonConfig()
    }

    val global by categories.registering {
        val style by groups.registering {
            val balloonStyle by options.registering<String> {
                binding = bindingFromSunset<TBConfig.IdentifierHolder>("balloonStyle")
                    .xmap(TBConfig.IdentifierHolder::identifier, TBConfig::IdentifierHolder)
                    .xmap(Identifier::toString, Identifier::parse)
                controller {
                    DropdownStringControllerBuilder.create(it)
                        .values(BalloonStyleManager.styleIds.map(Identifier::toString))
                }
            }

            val textColor by options.registering<Color> {
                binding = bindingFromSunset<Int>("textColor")
                    .xmap(::Color, Color::getRGB)
                controller = colorPicker(false)
            }

            val balloonTint by options.registering<Color> {
                binding = bindingFromSunset<Int>("balloonTint")
                    .xmap(::Color, Color::getRGB)
                controller = colorPicker(false)

                available {
                    val id = Identifier.tryParse(balloonStyle.pendingValue())

                    if (id != null) {
                        return@available BalloonStyleManager.getStyleById(id).allowsTint
                    }

                    false
                }
            }

            val balloonOpacity by options.registering {
                binding = bindingFromSunset("balloonOpacity")
                controller = slider(30..255)
            }

            val balloonSneakingOpacity by options.registering {
                binding = bindingFromSunset("balloonSneakingOpacity")
                controller = slider(30..255)
            }

            val balloonsHeightOffset by options.registering {
                binding = bindingFromSunset("balloonsHeightOffset")
                controller = slider(range = 0f..16f, step = 0.1f)
            }

            val distanceBetweenBalloons by options.registering {
                binding = bindingFromSunset("distanceBetweenBalloons")
                controller = slider(range = 0..20)
            }

            val maxBalloons by options.registering {
                binding = bindingFromSunset("maxBalloons")
                controller = slider(range = 1..16, step = 1)
            }

            val minBalloonWidth by options.registering {
                binding = bindingFromSunset("minBalloonWidth")
                controller = slider(range = 0..512, step = 8)
            }

            val maxBalloonWidth by options.registering {
                binding = bindingFromSunset("maxBalloonWidth")
                controller = slider(range = 0..512, step = 8)
            }

            val balloonAge by options.registering {
                binding = bindingFromSunset("balloonAge")
                controller = slider(range = 0..120, step = 1)
            }

            val balloonPadding by options.registering {
                binding = bindingFromSunset("balloonPadding")
                controller = slider(range = 0..64, step = 1)
            }
        }

        val preferences by groups.registering {
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
