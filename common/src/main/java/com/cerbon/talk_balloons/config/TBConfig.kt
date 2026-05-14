package com.cerbon.talk_balloons.config

object TBConfig {
    var balloonsHeightOffset: Float = 0.9f
    var distanceBetweenBalloons: Int = 3

    var maxBalloons: Int = 7

    var minBalloonWidth: Int = 13
    var maxBalloonWidth: Int = 180

    var balloonPadding: Int = 1
    var balloonAge: Int = 15
    var balloonStyle: BalloonStyle = BalloonStyle.ROUNDED

    var textColor: Int = 0x141414 // RGB-encoded
    var balloonTint: Int = 0xF1F6F8 // RGB-encoded

    var showOwnBalloon: Boolean = true
    var onlyDisplayBalloons: Boolean = false
}
