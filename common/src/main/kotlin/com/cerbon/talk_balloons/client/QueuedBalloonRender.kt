package com.cerbon.talk_balloons.client

//? if > 1.20.1 {
/*import com.mojang.blaze3d.vertex.MeshData
*///? } else {
import com.mojang.blaze3d.vertex.BufferBuilder
//? }
//? if >= 1.21.8 {
/*import com.mojang.blaze3d.textures.GpuTextureView
*///? }

@JvmRecord
data class QueuedBalloonRender(
    //? if > 1.20.1 {
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
