package com.cerbon.talk_balloons.client

import com.mojang.blaze3d.vertex.MeshData
//? if >= 1.21.8 {
/*import com.mojang.blaze3d.textures.GpuTextureView
*///? }

@JvmRecord
data class QueuedBalloonRender(
    val meshData: MeshData,
    //? if >= 1.21.8 {
    /*val text: Map<GpuTextureView, MeshData> = mapOf()
    *///? } else {
    val unused: Map<Unit, Unit> = mapOf()
    //? }
)
