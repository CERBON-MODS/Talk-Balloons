package com.cerbon.talk_balloons.client

//? if > 1.20.4 {
/*import com.mojang.blaze3d.vertex.MeshData
*///? } else {
import com.mojang.blaze3d.vertex.BufferBuilder
//? }
//? if >= 1.21.8 && < 26.3 {
/*import com.mojang.blaze3d.textures.GpuTextureView
*///? } else if >= 26.3 {
/*import com.mojang.renderpearl.api.textures.GpuTextureView
*///? }

@JvmRecord
data class QueuedBalloonRender(
    //? if > 1.20.4 {
    /*val meshData: MeshData,
    *///? } else {
    val meshData: BufferBuilder.RenderedBuffer,
    //? }
    //? if >= 1.21.8 {
    /*val text: Map<GpuTextureView, MeshData> = mapOf()
    *///? } else {
    val unused: Map<Unit, Unit> = mapOf()
    //? }
)
