# PinPad SDK consumer rules.
# Keep Gson-serialized models (PaymentResult, LoginResult, ClipPaymentLogin, preferences):
# R8 in the consuming app must not rename or strip their fields, otherwise the JSON contract
# between the SDK and the PinPad application breaks.
-keep class com.payclip.blaze.pinpad.sdk.domain.models.** { *; }
-keepattributes Signature, *Annotation*
