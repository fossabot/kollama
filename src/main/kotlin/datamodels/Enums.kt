package datamodels

import datamodels.serializers.ThinkModeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


enum class ChatRole {
    @SerialName("assistant")
    Assistant,
    @SerialName("user")
    User,
    @SerialName("system")
    System;
}

@Serializable
enum class ToolType {
    @SerialName("function")
    Function
}

@Serializable(with = ThinkModeSerializer::class)
enum class ThinkMode {
    ON,
    OFF,
    LOW,
    MEDIUM,
    HIGH;

}
