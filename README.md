<a name="readme-top"></a>


<br />
<div align="center">
  <a href="https://github.com/ClipMX/mobile.android.blaze.pinpad.sdk">
    <img src="https://assets-global.website-files.com/635aa55e76b13b5f73be2fe0/635ab4fc38f5e85102e32c6e_logo-clip-orange.svg" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">PinPad SDK</h3>

  <p align="center">
    📄 Welcome to PinPad SDK official documentation 📄
    <br />
    <br />
  </p>
</div>



<!-- ABOUT THE PROJECT -->
## About The Project

Tired of the hassle associated with integrating payment services into your application? Look no further! Our SDK is carefully tailored to your needs.

Whether you're an experienced developer or a beginner, integrating PayClip payment services into your checkout process has never been easier.


### Why choose our SDK?
 - Seamless integration: Easily integrate PayClip's secure payment services while continuing to use your own applications to sell your products and services.
 - Streamline Transactions: Eliminate the complexity of  credit card transaction management. Our SDK allows you to delegate the entire transaction process to PayClip, ensuring smooth and hassle-free payments.
 – Peace of Mind: Worried about the security of your customer data? With our SDK, you can rest assured that all your transaction information is securely handled by PayClip. Say goodbye to the stress of developing and maintaining your own payment system.


### Simplifying the Payment Process 
Developing your own payment system can be a difficult task with challenges and security concerns. Why navigate these waters alone when you can leverage PayClip's expertise?

With our SDK, you can forget about the pain of managing payment transactions and do what you do best. You can focus on growing your business.Leave the troublesome work to us. All the while, you and your customers will enjoy a seamless payment experience.

Are you ready to simplify your payment process? Get started with  PayClip Payment SDK today!

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started: Quick Guide

Welcome to the quick guide to get you started with our SDK! In just a few simple steps, you'll be up and running with our library in a few minutes.

### Prerequisites
Before you begin, make sure you have the following:

##### Supported Device
Please note that our SDK is currently only supported on Clip Total devices, as it requires integration with the PinPad application installed on these devices.

##### Dependency
To download the SDK dependency, you'll need to add Jitpack to your Maven repositories. Follow these steps:

```settings.gradle.kts
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

And then add the dependency to `build.gradle.kts`.

```build.gradle.kts
dependencies {
    implementation("com.github.ClipMX:mobile.android.blaze.pinpad.sdk:latest-version")
}
```

##### Installation Steps
- Add Jitpack Repository: Open your settings.gradle.kts file and add Jitpack repository to your Maven repositories list.
- Add Dependency: Open your build.gradle.kts file and add the SDK dependency.

###### Stay Updated
We strongly recommend staying informed about new versions of our SDK and updating your library frequently. While we strive to provide clean updates with no breaking changes, keeping your SDK version up-to-date ensures you have access to the latest features and improvements.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



## How to use the SDK?

Our payment SDK is designed to be incredibly user-friendly, allowing you to configure the client according to your specific needs. Below is an example implementation for basic usage in a Compose application:

Compose
```Payment.kt
@Composable
fun PaymentScreen() {
    val scope = rememberCoroutineScope()
    val client = remember {
        ClipPayment.Builder()
            // Configure your payment
            .build()
    }

    client.setPaymentHandler()

    Button(
        onClick = {
            scope.launch {
                client.start(
                    reference = REFERENCE,
                    amount = AMOUNT
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
            // Configure your payment
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
                amount = AMOUNT
            )
        }
    }
}
```

In this example, there are three vital components for client configuration:

1. **Client Initialization**: You must instantiate the client in your application.

Compose
```Payment.kt
val client = remember {
    ClipPayment.Builder()
        // Configure your payment
        .build()
}
```

Activity
```Activity.kt
private val builder: ClipPayment by lazy {
    ClipPayment.Builder()
        // Configure your payment
        .build()
}
```

2. **Payment Handler**: Payment handling is mandatory as the client needs to know the composable context to launch activities and handle results. If you are using Activity, be sure to call handler before activity onCreate.

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

3. **Payment Launch**: With the client instantiated and the payment handler configured, you can call the launcher. This method requires two parameters: the amount to charge and a descriptive message about the payment.

```Payment.kt
scope.launch {
    client.start(
        reference = REFERENCE,
        amount = AMOUNT
    )
}
```

And that's it! With these steps, you can start processing payments in your application 🤑

### Additional Configuration Parameters

Our payment SDK offers several additional configuration parameters to customize your integration experience. Below are explanations of each parameter and examples of how to use them:

- **isDemo**: This parameter allows you to toggle the usage of the Clip Demo Server. By default, it is set to false, meaning it operates with the production server for real payments. Setting it to true enables the use of the demo server, which is useful for testing purposes. By default, it is set to false.
```Payment.kt
    ClipPayment.Builder()
        .isDemo(isDemo: Boolean)
```

- **isAutoReturnEnabled**: This parameter sets the return mode for the terminal after a transaction. When set to true, the terminal automatically returns to your application after completing or encountering an error during the transaction process. If set to false, the terminal displays its own detailed screen explaining the situation. By default, it is set to false.
```Payment.kt
    ClipPayment.Builder()
        .isAutoReturnEnabled(isAutoReturnEnabled: Boolean)
```

- **setPaymentPreferences**: This parameter sets payment preferences.
  - **isQPSEnabled**: This parameter sets if the tip screen will be shown when the payment process starts or not. By default it is false, and the tip screen will not be shown.
  - **isMSIEnabled**: This parameter sets if the monthly interest-free installments will be enabled. By default it is false, and the qps will be disabled.
  - **isMCIEnabled**: This parameter sets if the monthly installments will be enabled. By default it is true, and the msi will be activated.
  - **isDCCEnabled**: This parameter sets if the dynamic currency convert will be enabled. By default it is true, and the mci will be activated.
  - **isTipEnabled**: This parameter sets if the tip screen will be shown when the payment process starts or not. By default it is false, and the tip screen will not be shown.
```Payment.kt
    ClipPayment.Builder()
        .setPaymentPreferences(preferences: PaymentPreferences)
```

- **addListener**: With this parameter, you can register a listener to receive transaction results. This allows you to handle the outcome of the transaction within your application.
```Payment.kt
    ClipPayment.Builder()
        .addListener(listener: PaymentListener)
```

<p align="right">(<a href="#readme-top">back to top</a>)</p>



### Error Codes
In the event of an error during the transaction process, the client may return one of the following error codes along with a description of the error:

| CODE | DESCRIPTION | 
| --- | --- |
| EMPTY_AMOUNT | Amount should not be 0.0. |
| EMPTY_MESSAGE | Message should not be empty. |
| SERVICE_ERROR | Something failed attempting to create payment order |
| LIMIT_ERROR | You have reached the terminal usage limit. |
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


<p align="right">(<a href="#readme-top">back to top</a>)</p>


