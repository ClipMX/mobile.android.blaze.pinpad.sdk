<a  name="readme-top"></a>
<br  />
<div  align="center">
<a  href="https://github.com/ClipMX/mobile.android.blaze.pinpad.sdk">
	<img  src="https://assets-global.website-files.com/635aa55e76b13b5f73be2fe0/635ab4fc38f5e85102e32c6e_logo-clip-orange.svg"  alt="Logo"  width="80"  height="80">
</a>
<h1  align="center">PinPad SDK</h1>
<a href="https://github.com/ClipMX/mobile.android.blaze.pinpad.sdk/actions"><img src="https://github.com/stripe/stripe-android/workflows/CI/badge.svg" alt="CI" style="max-width: 100%;"></a>
<a href="https://github.com/ClipMX/mobile.android.blaze.pinpad.sdk/releases"><img src="https://img.shields.io/badge/release-1.0.2-orange" alt="GitHub release" data-canonical-src="https://img.shields.io/badge/release-1.0.2-orange?maxAge=60" style="max-width: 100%;"></a>
<p  align="center">
📄 Welcome to PinPad SDK official documentation 📄
<br  />
<br  />
</p>
</div>

<!-- ABOUT THE PROJECT -->
# About The Project

Tired of the hassle associated with integrating payment services into your application? Look no further! Our SDK is carefully tailored to your needs.
Whether you're an experienced developer or a beginner, integrating PayClip payment services into your checkout process has never been easier.


# Table of contents

 - :question: <a href="#why-our-sdk">Why choose our SDK? </a> 
	- :dollar: <a href="#payment-process">Simplifying the Payment Process</a> 

		- :credit_card: <a href="#app-to-app">App-to-App SDK</a> 
		- :inbox_tray: <a href="#server-driven">Server driven integration</a> 

- :pencil: <a href="#getting-started"> Getting Started: Quick Guide</a>  
	 - <a href="#prerequisites">Prerequisites</a>  
	 - <a href="#supported-devices">Supported Device</a> 
	 - <a href="#api-key">API Key</a>
	 - <a href="#dependency">Dependency</a>
	 - <a href="#installing-dependency">Installing dependency</a>

- :iphone: <a href="#how-to"> How to use the SDK?</a>  
	- <a href="#additional-config">Additional Configuration Parameters</a>
	- <a href="#error-codes">Error Codes in SDK</a>

- :arrows_counterclockwise: <a href="#stay-updated"> Stay Updated</a>  


## Why choose our SDK?
<a  name="why-our-sdk"></a>

- Seamless integration: Easily integrate PayClip's secure payment services while continuing to use your own applications to sell your products and services.

- Streamline Transactions: Eliminate the complexity of credit card transaction management. Our SDK allows you to delegate the entire transaction process to PayClip, ensuring smooth and hassle-free payments.

– Peace of Mind: Worried about the security of your customer data? With our SDK, you can rest assured that all your transaction information is securely handled by PayClip. Say goodbye to the stress of developing and maintaining your own payment system.


### Simplifying the Payment Process
<a  name="payment-process"></a>


With Clip, you can unlock the full potential of your application by integrating our robust and versatile SDKs, enabling secure and efficient payment processing. This guide is your comprehensive resource to navigate through the integration process, providing step-by-step instructions and best practices to ensure a seamless payment experience within your app. Make sure you explore our [hardware offering](https://docs.google.com/document/d/1e2SuQS-2bUQGauQYLP_CTFpA1C-WMjkfz2a3xXyvFbw/edit#heading=h.boow2j1dl3de) to make sure your chosen hardware meets your software minimum requirements for optimal performance.  
  

Before you can start, let's take an overview of the current available solutions:

#### App to App SDK
<a  name="server-driven"></a>


Clip's app-to-app integration will allow you to run your POS app within our Clip hardware device and accept in-person payments completely integrated with our Pinpad like application.

**![](https://lh7-us.googleusercontent.com/YEONKiX7FM4Q7gZvvXs7UwT2mjJjCIYuEEuE3jtuowbRPeL5RrG4KPcAGux5_1M5F-6KxpEfCG4DfrFddbS_zqVMD72CH1Pmrdi1bOtOKm-bNotg3Xc6GL1Biqw_cYD39CRlaJWmlTqUKYaUP20ik_Q)**
 > **Note:** App-to-App integration is only available on our Clip Total Terminals. Bluetooth integration SDK is under development.

#### Server Driven Solution
<a  name="app-to-app"></a>


With Clip's Punto de Venta API you'll be able to create payment intents from your backend that can then be processed by our payment terminals using our Clip classic app or our Pinpad version.

**![](https://lh7-us.googleusercontent.com/nrFry0a5ZIs-bIsFBvOpuKkNKtjc6eCIH3jH5sX70-bPpAKRIJfTPlPUHJj8rdbV-Tpi2Zvd3IrdpFJNMgIsVjYE4IgSIyteAqYrqpU6ePP8gavNU02Ep4_fSB9UfK-kiAbr0dWi5EDeDTNbLf9Lk-c)**


<p  align="right">(<a  href="#readme-top">back to top</a>)</p>

<!-- GETTING STARTED -->


## Getting Started: Quick Guide
<a  name="getting-started"></a>

 
Welcome to the quick guide to get you started with our SDK! In just a few simple steps, you'll be up and running with our library in a few minutes.


### Prerequisites
<a  name="prerequisites"></a>

Before you begin, make sure you have the following:

  

#### Supported Device
<a  name="supported-devices"></a>

Please note that our SDK is currently only supported on Clip Total devices, as it requires integration with the PinPad application installed on these devices.

  

#### API Key
<a  name="api-key"></a>

You'll need an API key to authenticate with our services. If you don't have one yet, you can generate it on the [Clip Developers](https://developer.clip.mx/reference/token-de-autenticacion#2-crea-un-token-de-autenticaci%C3%B3n-con-codificaci%C3%B3n-base64) page.

  

#### Dependency
<a  name="dependency"></a>

To download the SDK dependency, you'll need to add JitPack to your Maven repositories. Follow these steps:

```settings.gradle.kts

dependencyResolutionManagement {

repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

repositories {

mavenCentral()

maven { url = uri("https://maven.pkg.github.com/ClipMX/mobile.android.blaze.pinpad.sdk") }

}

}

```

And then add the dependency to `build.gradle.kts`.

```build.gradle.kts

dependencies {

implementation("com.blaze.pinpad:pinpad-sdk:latest-version")

}

```

#### Installing dependency
<a  name="installing-dependency"></a>


- Add JitPack Repository: Open your settings.gradle.kts file and add the JitPack repository to your Maven repositories list.

- Add Dependency: Open your build.gradle.kts file and add the SDK dependency.

  

  

<p  align="right">(<a  href="#readme-top">back to top</a>)</p>

  
  
  

## How to use the SDK?
<a  name="how-to"></a>
  

Our payment SDK is designed to be incredibly user-friendly, allowing you to configure the client according to your specific needs. Below is an example implementation for basic usage in a Compose application:

  

Compose

```Payment.kt

@Composable

fun PaymentScreen() {

val scope = rememberCoroutineScope()

val client = remember {

ClipPayment.Builder()

.setUser(YOUR_CLIP_USER)

.setApiKey(YOUR_CLIP_TOKEN)

.build()

}

  

client.setPaymentHandler()

  

Button(

onClick = {

scope.launch {

client.start(

reference = REFERENCE,

amount = AMOUNT,

message = MESSAGE

)

}

}

) {

...

}

}

```

  

Activity

```Activity.kt

class MainActivity : ComponentActivity() {

  

private val builder: ClipPayment by lazy {

ClipPayment.Builder()

.setUser(YOUR_CLIP_USER)

.setApiKey(YOUR_CLIP_TOKEN)

.build()

}

  

init {

builder.setPaymentHandler(this@MainActivity)

}

  

override fun onCreate(savedInstanceState: Bundle?) {

super.onCreate(savedInstanceState)

  

lifecycleScope.launch {

builder.start(

reference = REFERENCE,

amount = AMOUNT,

message = MESSAGE

)

}

}

}

```

  

In this example, there are three vital components for client configuration:

  

1.  **Client Initialization**: You must instantiate the client in your application. The client requires two mandatory parameters: your Clip user and the API key generated earlier.

  

Compose

```Payment.kt

val client = remember {

ClipPayment.Builder()

.setUser(YOUR_CLIP_USER)

.setApiKey(YOUR_CLIP_TOKEN)

.build()

}

```

  

Activity

```Activity.kt

private val builder: ClipPayment by lazy {

ClipPayment.Builder()

.setUser(YOUR_CLIP_USER)

.setApiKey(YOUR_CLIP_TOKEN)

.build()

}

```

  

2.  **Payment Handler**: Payment handling is mandatory as the client needs to know the composable context to launch activities and handle results. If you are using Activity, be sure to call handler before activity onCreate.

  

Compose

```Payment.kt

@Composable

fun PaymentScreen() {

...

  

client.setPaymentHandler()

  

...

}

```

  

Activity

```Activity.kt

init {

builder.setPaymentHandler(this@MainActivity)

}

```

  

3.  **Payment Launch**: With the client instantiated and the payment handler configured, you can call the launcher. This method requires two parameters: the amount to charge and a descriptive message about the payment.

  

```Payment.kt

scope.launch {

client.start(

reference = REFERENCE,

amount = AMOUNT,

message = MESSAGE

)

}

```

  

### Additional Configuration Parameters
<a  name="additional-config"></a>
  

Our payment SDK offers several additional configuration parameters to customize your integration experience. Below are explanations of each parameter and examples of how to use them:

  

-  **isDemo**: This parameter allows you to toggle the usage of the Clip Demo Server. By default, it is set to false, meaning it operates with the production server for real payments. Setting it to true enables the use of the demo server, which is useful for testing purposes. By default, it is set to false.

```Payment.kt

ClipPayment.Builder()

.isDemo(isDemo: Boolean)

```

  

-  **isAutoReturnEnabled**: This parameter sets the return mode for the terminal after a transaction. When set to true, the terminal automatically returns to your application after completing or encountering an error during the transaction process. If set to false, the terminal displays its own detailed screen explaining the situation. By default, it is set to false.

```Payment.kt

ClipPayment.Builder()

.isAutoReturnEnabled(isAutoReturnEnabled: Boolean)

```

  

-  **addListener**: With this parameter, you can register a listener to receive transaction results. This allows you to handle the outcome of the transaction within your application.

```Payment.kt

ClipPayment.Builder()

.addListener(listener: PaymentListener)

```

  

-  **setLoadingState**: This parameter provides you with a loading state while our SDK is performing various tasks such as calling APIs. It accepts a MutableStateFlow<Boolean> parameter, allowing you to manage the loading state within your application.

```Payment.kt

ClipPayment.Builder()

.setLoadingState(state: MutableStateFlow<Boolean>)

```

  

<p  align="right">(<a  href="#readme-top">back to top</a>)</p>

  
  
  

### Error Codes in SDK
<a  name="error-codes"></a>

In the event of an error during the transaction process, the client may return one of the following error codes along with a description of the error:

| CODE | DESCRIPTION |
| --- | --- |
| EMPTY_AMOUNT | Amount should not be 0.0. |
| EMPTY_MESSAGE | Message should not be empty. |
| GENERIC_DECLINE | The transaction was declined for unspecified reasons. |
| RECEIVE_DECLINE_CALL_ISSUER | The transaction was declined. Please call the card issuer for further assistance. |
| INSUFFICIENT_FUNDS | Insufficient funds available for the transaction. |
| RECEIVE_DECLINE_CALL_ISSUER_2 | Another instance of transaction decline. Please call the card issuer for further assistance. |
| NO_CONN | No connection available during the transaction. |
| MC_FALLBACK | Mastercard fallback transaction initiated. |
| VISA_CTLS_FALLBACK | Visa contactless fallback transaction initiated. |
| AMEX_MERCHANT_BLOCKED | American Express transaction declined due to merchant blocking. |
| NOT_SUFFICIENT_FUNDS | Insufficient funds available for the transaction. |
| DO_NOT_HONOR | The card issuer declined the transaction. |
| DESTINATION_NOT_AVAILABLE | The destination for the transaction is not available. |
| INVALID_MERCHANT | Invalid merchant for the transaction. |
| RESTRICTED_CARD | The card used for the transaction is restricted. |
| INVALID_TRANSACTION | The transaction is invalid. |
| TRANSACTION_NOT_PERMITTED_TO_CARDHOLDER | The transaction is not permitted to the cardholder. |
| ISSUER_OR_SWITCH_IS_INOPERATIVE | The card issuer or switch is inoperative. |
| PICK_UP_CARD | The card should be picked up by the merchant. |
| EXPIRED_CARD | The card used for the transaction has expired. |
| EXCEEDS_WITHDRAWAL_AMOUNT_LIMIT | The transaction amount exceeds the withdrawal limit. |
| FAIL_3DS_AUTHENTICATION | 3DS authentication for the transaction failed. |
| ALLOWABLE_NUMBER_OF_PIN_TRIES_EXCEEDED | Maximum allowable number of PIN tries exceeded. |
| INVALID_CARD_NUMBER_NO_SUCH_NUMBER | Invalid card number provided. |
| GENERIC_ERROR | Generic error occurred during the transaction. |
| REFER_TO_CARD_ISSUER | The transaction should be referred to the card issuer. |
| INVALID_AMOUNT | The transaction amount is invalid. |
| INVALID_PIN_ONE_TIME | Invalid one-time PIN provided. |
| CONTACTLESS_FALLBACK_VISA_MASTERCARD | Contactless fallback transaction for Visa or Mastercard initiated |
| QPS_FALLBACK_FOREIGN_CARDS | Quick Payment Service (QPS) fallback transaction for foreign cards initiated. |
| BILLER_SYSTEM_UNAVAILABLE | Biller system is unavailable for the transaction. |
| TERMINAL_ERROR | Error occurred at the terminal. |
| NO_CONNECTION | No connection detected during the transaction. |
| CANCELLED | The transaction was cancelled. |
| UNKNOWN_ERROR | An unknown error occurred. |

  
   ## Stay Updated
<a  name="stay_updated"></a>

We strongly recommend staying informed about new versions of our SDK and updating your library frequently. While we strive to provide clean updates with no breaking changes, keeping your SDK version up-to-date ensures you have access to the latest features and improvements.
  

And that's it! With these steps, you can start processing payments in your application 🤑
  

<p  align="right">(<a  href="#readme-top">back to top</a>)</p>