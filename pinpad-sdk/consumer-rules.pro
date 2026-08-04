# PinPad SDK consumer rules.
# Keep Gson-serialized DTOs: R8 in the consuming app must not rename or strip their fields,
# otherwise the JSON contract with the Clip backend breaks.
-keep class com.payclip.blaze.pinpad.sdk.data.models.** { *; }
-keepattributes Signature, *Annotation*

# Gson TypeToken generic signatures.
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken
