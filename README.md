<a name="readme-top"></a>    
<br />
<div align="center">    
<a href="https://github.com/ClipMX/mobile.android.blaze.pinpad.sdk">    
<img src="https://assets-global.website-files.com/635aa55e76b13b5f73be2fe0/635ab4fc38f5e85102e32c6e_logo-clip-orange.svg" alt="Logo" width="80" height="80">    
</a>    
<h1 align="center">PinPad SDK</h1>    
<a href="https://github.com/ClipMX/mobile.android.blaze.pinpad.sdk/actions"><img src="https://github.com/stripe/stripe-android/workflows/CI/badge.svg" alt="CI" style="max-width: 100%;"></a>    
<a href="https://github.com/ClipMX/mobile.android.blaze.pinpad.sdk/releases"><img src="https://img.shields.io/badge/release-1.0.7-orange" alt="GitHub release" data-canonical-src="https://img.shields.io/badge/release-1.0.7-orange?maxAge=60" style="max-width: 100%;"></a>    
<p align="center">    
📄 PinPad SDK developer documentation 📄 <br />    
<br />    
</p>    
</div>    

<!-- ABOUT THE PROJECT -->    
# About The Project

The Clip SDK offers two robust solutions for integrating seamless payment processing into your applications. Whether you are developing a native Android app or working with a server-side application, Clip SDK provides the tools necessary to manage transactions efficiently. Our SDK is designed to simplify the integration process, ensuring secure and reliable payment experiences for your customers.


# Table of contents

- :question: <a href="#why-our-sdk">Why choose our SDK? </a>
    - :credit_card: <a href="#payment-process">Simplifying the Payment Process</a>

        - :iphone: <a href="#app-to-app">Terminal SDK</a>
        - :computer: <a href="#server-driven">Server Side SDK</a>

- :bulb: <a href="#getting-started"> Getting Started</a>
- :gear: <a href="#supported-devices">Supported Devices</a>
-  :iphone: <a href="#app-to-app-sdk"> Terminal SDK solution</a>
    - :heavy_exclamation_mark: <a href="#prerequisites">Minimal requirements</a>
    - :key: <a href="#api-key">API Key</a>
    - :hammer_and_wrench: <a href="#installing-dependency">Installing dependency</a>
    -  :sparkles: <a href="#how-to"> Terminal SDK code example</a>
    - :wrench: <a href="#additional-config">Additional Configuration Parameters</a>
    - :warning: <a href="#error-codes">Error Codes in Terminal SDK</a>

-   :computer: <a href="#server-driven-int"> Server Side SDK solution</a>

    - :key:  <a href="#token-aut"> Token Authentication</a>
    - :envelope: <a href="#api-call"> API methods</a>
        - <a href="#post-method"> Create payment request </a>
        -  <a href="#delete-method"> Delete payment request </a>  
    - :incoming_envelope: <a href="#payment-result"> Payment Results</a>
      - :link: <a href="#webhook-result"> Webhook</a> 
    - :warning: <a href="error-codes-server">Error codes in Server Side SDK</a>

-  <a href="#first-payment"> Terminal result process</a>   
- :arrows_counterclockwise: <a href="#stay-updated"> Stay Updated</a>


## Why choose our SDK?
<a name="why-our-sdk"></a>


-   Comprehensive SDK Solutions: Clip offers tailored solutions to suit both Android and non-Android environments, ensuring a wide range of compatibility.

-   Easy Integration: Our detailed documentation and support resources make the integration process straightforward and quick.

-   Secure and Compliant: Clip ensures that all transactions are secure and compliant with industry standards, giving you and your customers peace of mind.

-   Exceptional Support: Our dedicated support team is available to assist with any questions or issues that may arise during integration.


- Proven Reliability: Trusted by businesses of all sizes, Clip's payment solutions are reliable and scalable, capable of handling high transaction volumes.


### Simplifying the Payment Process
<a name="payment-process"></a>


With Clip, you can unlock the full potential of your application by integrating our robust and versatile SDKs, enabling secure and efficient payment processing. This guide is your comprehensive resource to navigate through the integration process, providing step-by-step instructions and best practices to ensure a seamless payment experience within your app. Make sure you explore our [hardware offering](https://docs.google.com/document/d/1e2SuQS-2bUQGauQYLP_CTFpA1C-WMjkfz2a3xXyvFbw/edit#heading=h.boow2j1dl3de) to make sure your chosen hardware meets your software minimum requirements for optimal performance.


Before you can start, let's take an overview of the current available solutions:

#### Terminal SDK
<a name="app-to-app"></a>


The Terminal SDK is designed specifically for native Android applications, providing a seamless app-to-app integration for processing payments. This solution allows merchants to install their application on Clip's Android devices, initiate sales within their app, and handle the entire transactional flow in the Clip app. Once the transaction is completed, control returns to the merchant's application with the transaction details.

**![](https://lh7-us.googleusercontent.com/YEONKiX7FM4Q7gZvvXs7UwT2mjJjCIYuEEuE3jtuowbRPeL5RrG4KPcAGux5_1M5F-6KxpEfCG4DfrFddbS_zqVMD72CH1Pmrdi1bOtOKm-bNotg3Xc6GL1Biqw_cYD39CRlaJWmlTqUKYaUP20ik_Q)**
> **Note:** Terminal SDK integration is only available on Clip Total and Clip Pro 2 devices.

#### Server Side SDK
<a name="server-driven"></a>


The Server Side SDK is designed for applications that are not running on Android. This solution enables backend systems to initiate sales by consuming the Clip PinPad API. Once a new payment intent gets posted to our Pinpad API, the device will  wake up a Clip from sleep mode to handle the transaction. The entire transaction flow occurs on the Clip device without requiring a physical connection, and the transaction details are sent back to the backend via webhooks.

**![](https://lh7-us.googleusercontent.com/nrFry0a5ZIs-bIsFBvOpuKkNKtjc6eCIH3jH5sX70-bPpAKRIJfTPlPUHJj8rdbV-Tpi2Zvd3IrdpFJNMgIsVjYE4IgSIyteAqYrqpU6ePP8gavNU02Ep4_fSB9UfK-kiAbr0dWi5EDeDTNbLf9Lk-c)**


<p align="right">(<a href="#readme-top">back to top</a>)</p>    

<!-- GETTING STARTED -->    


## Getting Started
<a name="getting-started"></a>


To facilitate the integration testing process, follow the steps below to request a test POS device from our team:

Reach Out to Us

-   Email: Contact our integration support team at [sdk@clip.mx](mailto:sdk@clip.mx)


Provide Details

-   Your Information: Include your name, contact information, company name, and industry.

-   Integration Requirements: Provide a brief overview of your integration requirements and goals.

-   Timeline: Indicate your preferred timeline for testing and integration.

Request a Test POS Device

Mention in your email or message that you would like to request a test POS device for integration testing purposes. Our team will review your request and get in touch to discuss further details.

Arrange Shipment

Once your request is approved, we will arrange for the shipment of a test POS device to your designated address. Tracking information will be provided so you can monitor the delivery status.

## Supported Devices
<a name="supported-devices"></a>

The Terminal SDK is supported on the following Clip devices:

-   Clip Pro 2
-   Clip Total
-   Clip Total 2

These devices come pre-installed with the Clip application necessary for this SDK integration.


## Terminal SDK Solution
<a name="app-to-app-sdk"></a>  
Clip's Terminal SDK allows you to integrate your POS app seamlessly within our Clip hardware devices, enabling you to accept in-person payments through our PinPad application. This integration ensures a smooth and secure transactional flow for both merchants and customers.

### Mininal requirements
<a name="prerequisites"></a>

To successfully use the Terminal SDK integration, ensure the following:

-   Contact with Sales Team: You should be in contact with our Sales Team and have received your test POS terminals.

-   Native Android: Your APK should be for a Native Android application.

-   Security Check: You’ll need to share your APK with our InfoSec team for a security check.

-   APK Installation: Once the app is approved by the InfoSec team, it will be installed in the devices.


#### API Key
<a name="api-key"></a>

You'll need an API key to authenticate with our services. If you don't have one yet, you can generate it on the [Clip Developers](https://developer.clip.mx/reference/token-de-autenticacion#2-crea-un-token-de-autenticaci%C3%B3n-con-codificaci%C3%B3n-base64) page.




#### Installing dependency
<a name="installing-dependency"></a>

- Add JitPack Repository: Open your settings file and add the JitPack repository to your Maven repositories list.

- Add Dependency: Open your build file and add the SDK dependency.

```gradle
 dependencyResolutionManagement {    
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)    
    repositories {    
	    mavenCentral()    
	    maven {
	        url = uri("https://jitpack.io")
	    }
	}
}    
```   

Example for adding the dependency into `build.gradle.kts` kotlin file.

```gradle 
dependencies {
	implementation("com.blaze.pinpad:pinpad-sdk:latest-version")
}    
```  

<p align="right">(<a href="#readme-top">back to top</a>)</p>    


##  Terminal SDK code example
<a name="how-to"></a>

Our payment SDK is designed to be incredibly user-friendly, allowing you to configure the client according to your specific needs. Below is an example implementation for basic usage in a Compose application:

**Compose**

```kotlin
@Composable
fun PaymentScreen() { 
    val scope = rememberCoroutineScope() 
    val client = remember { ClipPayment.Builder().build() }
    client.setPaymentHandler()
    Button( onClick = { 
        scope.launch { 
            client.start(amount = AMOUNT, message = MESSAGE)
        } 
    }
    ) { 
        ... 
    }
}
``` 

**Activity**

```kotlin  
class MainActivity: ComponentActivity()  { 
    private val builder: ClipPayment by lazy{ 
        ClipPayment.Builder().build() 
    }
    init { 
        builder.setPaymentHandler(this@MainActivity) 
    }
    override fun onCreate(savedInstanceState: Bundle?){ 
        super.onCreate(savedInstanceState)
        lifecycleScope.launch { 
            builder.start( reference = REFERENCE, amount = AMOUNT ) 
        } 
    }
}
```

Taking the last code example we will obtain the next step from a screen view from the client terminal UI.


In this example, there are three vital components for client configuration:

**1. Client Initialization**

To begin using the Clip API in your application, you need to initialize the client. This involves creating an instance of the client with the necessary credentials. The client requires two mandatory parameters: your Clip user and the API key you generated earlier.

Steps to Initialize the Client:

1.  Instantiate the Client
-   In your application's code, create a new instance of the Clip client.
2.  Provide Mandatory Parameters
-   Ensure you pass the following parameters when instantiating the client:
-   Clip User: Your Clip user account identifier.
-   API Key: The API key you generated from the Clip Developers Portal.

**Compose**

```kotlin
val client = remember { 
	ClipPayment.Builder().build()
}
```

**Activity**

```kotlin
private val builder: ClipPayment by lazy {
    ClipPayment.Builder().build() 
}
```

**2. Payment Handler**

Initializing the payment handler is crucial for the Clip client to properly manage payment activities and handle results. This setup ensures the client knows the composable context needed to launch activities and process their outcomes.

Steps to Initialize the Payment Handler:

1.  Initialize the Payment Handler
-   Make sure to initialize the payment handler within your activity.
2.  Call the Handler Before Activity's onCreate
-   If you are using an Activity, ensure you call the handler before the onCreate method of your activity.

**Compose**

```kotlin
@Composable 
fun PaymentScreen() {
    ...
    client.setPaymentHandler()
    ...
}
```

**Activity**

```kotlin  
init { 
	builder.setPaymentHandler(this@MainActivity)
}
```

**3. Payment Launch**

Once you have instantiated the client and configured the payment handler, you can proceed to launch the payment process. The payment launch method requires two parameters: the amount to charge and a descriptive message about the payment.

Steps to Launch a Payment:

1.  Ensure Client and Payment Handler are Initialized
-   Confirm that the client and payment handler have been properly set up as described in previous sections.
2. Call the Payment Launcher
-   Use the payment handler to initiate the payment process by providing the necessary parameters


```kotlin 
scope.launch { 
    client.start(
        reference = REFERENCE,
        amount = AMOUNT,
        message = MESSAGE
    )
}
```


### Additional Configuration Parameters
<a name="additional-config"></a>

Our payment SDK offers several additional configuration parameters to customize your integration experience. Below are explanations of each parameter and examples of how to use them:

- **isAutoReturnEnabled**: This parameter sets the return mode for the terminal after a transaction. When set to true, the terminal automatically returns to your application after completing or encountering an error during the transaction process. If set to false, the terminal displays its own detailed screen explaining the situation. By default, it is set to false.

```kotlin
ClipPayment.Builder()
	.isAutoReturnEnabled(isAutoReturnEnabled: Boolean)    
```   

- **isRetryEnabled**: This parameter sets if retries are available for the terminal after a transaction. When set to true, the terminal allows you to retry the payment in case of failure. If set to false, the terminal will only show “cancel option” in case of failure.

```kotlin    
ClipPayment.Builder()
	.isRetryEnabled(isRetryEnabled: Boolean)
```   

- **isShareEnabled**: This parameter sets if share buttons are available in transaction success. When set to true, the terminal will share options in success. If set to false, the terminal will not show share options in success.

```kotlin
ClipPayment.Builder()
	.isShareEnabled(isShareEnabled: Boolean)    
```

- **addListener**: With this parameter, you can register a listener to receive transaction results. This allows you to handle the outcome of the transaction within your application.

```kotlin
ClipPayment.Builder()
    .addListener(listener: PaymentListener) 
```   

- **setPaymentPreferences**: This parameter sets payment preferences.
    - isMSIEnabled: This parameter sets if the monthly interest-free installments will be enabled. By default it is false, and the msi will be disabled.

    - isMCIEnabled: This parameter sets if the monthly installments will be enabled. By default it is true, and the mci will be activated.

    - isDCCEnabled: This parameter sets if the dynamic currency convert will be enabled. By default it is true, and the dcc will be activated.
    -  isTipEnabled: This parameter sets if the tip screen will be shown when the payment process starts or not. By default it is false, and the tip screen will not be shown. 
    -  isAutoPrintReceiptEnabled: When transaction is successful you can enable the auto print of your receipt in POS.

```kotlin
ClipPayment.Builder()
    .setPaymentPreferences(preferences: PaymentPreferences) 
```

**Example Configuration**
Here is an example of how to configure the SDK with all the parameters:
```kotlin
// Import necessary libraries
import com.clip.sdk.ClipPayment;
import com.clip.sdk.PaymentListener;
import kotlinx.coroutines.flow.MutableStateFlow;

public  class PaymentActivity extends Activity { 
    private ClipPayment clipPayment
    private MutableStateFlow<Boolean> loadingState = new MutableStateFlow<>(false)
    @Override 
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
		// Configure ClipPayment with additional parameters
        clipPayment = new ClipPayment.Builder()
            .isDemo(true) // Use demo server for testing
            .isAutoReturnEnabled(true) // Enable auto return to app
            .isTipEnabled(true) // Show tip screen
            .addListener( new PaymentListener() {
                @Override
                public void onPaymentSuccess(TransactionResult result) {
                    // Handle payment success 
                }
                @Override 
                public void onPaymentFailure(TransactionError error) {
						// Handle payment failure
                } 
            }).build() 
    }
    private void launchPayment() {
		// Amount to charge 
        double amount = 100.00
        // Descriptive message about the payment 
        String description = "Payment for Order #1234" 
        // Launch the payment process
        clipPayment.launchPayment(amount, description) 
    }
}
```


By using these configuration parameters, you can customize the behavior of the Clip SDK to better fit your application needs and provide a seamless payment experience for your users.

Obtaining Payment Results

There are two available methods to obtain payment results when using Clip's payment SDK:

1.  Response from the code implementation as Payment Results

2.  Webhook


**Response from the Compose Session as Payment Results**


This channel provides payment results synchronously, meaning the response is received immediately after the payment transaction is completed. Here’s how it works:

-   Synchronous Response: The payment results are directly returned as part of the compose session, typically in the form of a response object or data structure.

-   Real-time Integration: Merchants can integrate this method into their application logic to handle payment results in real-time, allowing for immediate processing and response to the transaction outcome.

-   Same Session Context: Since the response is synchronous, merchants can access the payment results directly within the same session context where the payment transaction was initiated.

-   Advantages: This method offers simplicity and immediacy in accessing payment results, making it suitable for scenarios where real-time processing is required.

Example usage:

```kotlin

clipPayment.launchPayment(amount, description, new PaymentListener() { 
    @Override 
    public  void onPaymentSuccess(TransactionResult result) { 
        // Handle successful payment result 
    }
    @Override 
    public  void onPaymentFailure(TransactionError error) {
        // Handle failed payment result 
    } 
})
```

### Error Codes in Terminal SDK
<a name="error-codes"></a>

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
|PINPAD_TERMINAL_TIMEOUT_EXCEPTION | Unable to connect to pinpad terminal. Please check your internet connection in desired pinpad terminal or check that you have entered the correct serial_number_pos |

<p align="right">(<a href="#readme-top">back to top</a>)</p>    


## Server Side SDK solution
<a name="server-driven-int"></a>

The Server Side SDK allows non-Android apps to process in-person payments using Clip devices. This solution enables backend systems to initiate transactions via the Clip PinPad API, which wakes up the Clip device to handle the payment. Once the transaction is complete, the result is sent back to the backend via a webhook, and the device returns to sleep mode.


### Minimal Requirements

To use the Server Side SDK, ensure the following:

-   Customer Account: You must have a Clip customer account to access and manage the necessary services.


- API Endpoint Communication: Your backend should be able to communicate correctly with the Clip API URL endpoint to initiate and manage transactions.



1. Go to the developers portal.

<img src="https://lh7-us.googleusercontent.com/3P72h6R6Nvx19vv6JUBiFqdcKaqa3voGd2WO2yDZQICvXr4GTwSmXTi4bQ8z5iN5UJcScqH60brw-T1glAkb4I6BmObPBx4bzHh-f3HJ4CAMhjuvFGoVnXaJQYZyK5GA22YODqDUZAH8-cev9iUuHW0" alt="developer portal" width="200"/>


3. We continue creating our application

<img src="https://lh7-us.googleusercontent.com/f0RRfJZf2L_RvYW9ekPvUi-XsEdsCSBnjPQa5f_XnxdTa4yKeBcdkds_DTRjQd11uH6DvFPrV35TsL3-GoZtzNqwcRIcEweuBKTJ3ziVnFn3NoR4UEjJpNQohENP0Q1IVjvmEl7JyePZ_wD1TSwKYDM" alt="developer portal"/>


3. We neet to assign a name, and then proceed to create

<img src="https://lh7-us.googleusercontent.com/ovAlHNFMnqXicLK1ADK4hsVDZXNoqxy8UbKQZXGCb6nXrApM8PYeaN-oLKNuL5bwENU3-u45EKaR-s3ZekS_EO17VSUl4Xb89fKerBC7uqjpgUv0sfHFN_55NR8kWAvRb-4R8EGTQgEGWoZ3nwjw_GQ" width="600" alt="developer portal"/>


4. When our application is created, it is important to save the API Key and Secret Key in a safe place. We need to use after

<img src="https://lh7-us.googleusercontent.com/1DhQJKhCYbwT2xr8cJtn1VXTQN5fhSGuWsIupZsDp8_V6ftHm7IgZh5-m01AhoOjR7pa-UBH0GvhUcH6hiVQwdntBxvSYoPGphaxkmmb2-9Iw94Dohpw6_-V9DP2QUj1_dXa-P4cMvJNLjrilXm1TSk" width="600" alt="developer portal"/>



5. Finally, in the [Clip developers portal](https://developer.clip.mx/reference/token-de-autenticacion) with our previous keys, put the values in the fields as we can see in the next image.


  <img src="https://lh7-us.googleusercontent.com/FwyfiponSVsmYmoIXtWUhGmEwRHGTLW7ik4N1lfR4DTdTdqpcsAUvltQ_BjDK9VAeij_8TvZSREpwVAQOM4-VJuVb-el72kQAAnT015V1fuhl0ExYBXJOhVGBSYi3RVeb1De-Qrpjqgn_PjUFlKPvs8" width="500" alt="developer portal"/>

Finally we have a Basic value token for our next steps creating a request to the API in header authentication.


### API methods
<a name="api-call"></a>

**Create payment request**

<a name="post-method"></a>

> **Note:** You need to implement next URL to make a POST request :


<img src="https://img.shields.io/badge/POST-2175BF" alt="method" style="max-width: 100%;">  [https://api.payclip.io/f2f/pinpad/v1/payment](https://api.payclip.io/f2f/pinpad/v1/payment)  

**/Payment** method works to process new paymet request

**Schema**

```cURL
curl --location 'https://api.payclip.io/f2f/pinpad/v1/payment' \
	--header 'Authorization: Basic {TOKEN}' \
	--header 'Content-Type: application/json' \
	--data '
{
	"reference": "Dinner",
	"amount": 25.5,
	"serial_number_pos": "MySnPOS12345",
	"preferences": {
		"is_auto_return_enabled": false,
		"is_tip_enabled": false,
		"is_msi_enabled": true,
		"is_mci_enabled": true,
		"is_dcc_enabled": true,
		"is_retry_enabled": true,
        "is_share_enabled": true,
        "is_auto_print_receipt_enabled": false
		}
}
'  
``` 

| Field name                                | Description                                                                         | Type       | Notes                                                            | Required | Default value |
|-------------------------------------------|-------------------------------------------------------------------------------------|------------|------------------------------------------------------------------|----------|---------------|
| amount                                    | Transaction amount.                                                                 | Number     | --                                                               | yes      | --            |
| reference                                 | external reference id                                                               | String     | --                                                               | yes      | --            |
| serial_number_pos                         | Clip terminal serial number                                                         | Identifier | String                                                           | yes      | --            |
| preferences                               | customizable values                                                                 | Object     | Options that can enable or disable                               | No       | --            |
| preferences.is_auto_return_enabled        | Param for configuration terminal process when finish                                | Boolean    | --                                                               | No       | false         |
| preferences.is_tip_enabled                | Param for screen configuration terminal tip                                         | Boolean    | --                                                               | No       | false         |
| preferences.is_msi_enabled                | Param for enable installments without interests                                     | Boolean    | To learn terms and conditions about installments visit Clip site | No       | true          |
| preferences.is_mci_enabled                | Param to enable installments with interests                                         | Boolean    | To learn terms and conditions about installments visit Clip site | No       | true          |
| preferences.is_dcc_enabled                | Param to enable dynamic current convertion                                          | Boolean    | --                                                               | No       | true          |
| preferences.is_retry_enabled              | Param to enable to users retries their payments when these fails                    | Boolean    | --                                                               | No       | true          |
| preferences.is_share_enabled              | Param to enable share options in the end of successful transaaction                 | Boolean    | --                                                               | No       | true          |
| preferences.is_auto_print_receipt_enabled | When transaction is successful you can enable the auto print of your receipt in POS | Boolean    | --                                                               | No       | false         |

#### Wait for terminal response (OPTIONAL)

You can use the header `Pinpad-Wait-Response` to process payments synchronously. You can wait for the payment to reach the terminal and receive an immediate response.

##### Workflow:

1. Client ----> Service ----> Terminal

Include the `Pinpad-Wait-Response` header in the request. This header accepts `true` or `false` values:

- `true`: The client waits for the terminal to process the payment before receiving a response.
- `false`: The response is returned immediately after registering the payment, without waiting for terminal confirmation (as in the previous version).

If the Pinpad-Wait-Response header is not included in the request, the default value is false.

##### Timeout Behavior:

When `Pinpad-Wait-Response` is set to `true`, the service will wait up to **60 seconds** for the terminal to respond. If the terminal takes longer than 60 seconds, the service will terminate the connection with a **timeout** error.

##### Error Scenarios:

If the service fails to connect to the terminal, the most likely causes are:

- The terminal is offline (no internet connection).
- The provided `serial_number_pos` is incorrect.

##### Example Request

``` bash
curl --location --globoff '{{pinpadUrl}}/v1/payment' \
--header 'Authorization: {{authClientToken}}' \
--header 'Pinpad-Wait-Response: true' \
--header 'Content-Type: application/json' \
--data '{
    "reference": "XYZ",
    "amount": 200,
    "serial_number_pos": "P8C12311200011AA",
    "preferences": {
        "is_auto_return_enabled": false,
        "is_retry_enabled": true,
        "is_tip_enabled": true
    }
}'
```

##### Responses

- **Successful Response (when payment reaches the terminal)**: If the payment reaches the terminal successfully, the response will be the same as in the previous flow where `Pinpad-Wait-Response` is not used or set to `false`.
    
- **Error Response (Terminal Connection Failure)**: If the payment request cannot reach the terminal (e.g., no internet connection or incorrect `serial_number_pos`), the service will return the following error (504 Gateway timeout):
    
``` json
{
    "code": "PINPAD_TERMINAL_TIMEOUT_EXCEPTION",
    "message": "Unable to connect to pinpad terminal. Please check your internet connection in the desired pinpad terminal or verify the correct serial_number_pos."
}
```

##### Key Considerations

- **`Pinpad-Wait-Response` Header**: Use this to control whether the client waits for the terminal response.
- **Timeout Behavior**: Set to 60 seconds. Exceeding this results in a timeout.
- **Common Errors**: Connectivity issues or incorrect terminal identifiers will trigger an exception response.

**Delete payment request**

<a name="delete-method"></a>

If for some reason you need to cancel/delete a payment intent, you'll be able to do so using an available DELETE endpoint passing the pinpad_request_id parameter as shown below

<img src="https://img.shields.io/badge/DELETE-CA3823" alt="method" style="max-width: 100%;"> https://api.payclip.io/f2f/pinpad/v1/payment/{pinpad_request_id}  

**Schema**

```cURL
curl --location 
	--request DELETE 'https://api.payclip.io/f2f/pinpad/v1/payment/{pinpad_request_id}' \
	--header 'Authorization: Basic {TOKEN}' \
	--data ''  
```

**/Payment** method works for delete payment request

> Url param -  **pinpad_request_id:** Identifier created for payment request

| Field name | Description | Type | Notes | Required |  Default value |
| -- | -- | -- | -- | -- | -- |
| pinpad_request_id | url param for request identifier created |  String| --  |  yes | -- |


>  **IMPORTANT**: this will only work as long as the payment intent hasn't been picked up by the terminal yet. If that's the case and you still want to abort the payment, you can still do so by canceling the process within the Pin Pad app or closing the app entirely.


### Payments Result
<a name="payment-result"></a>

This integration offers two ways to communicate with your backend for payment results:

- Response from the payment intent

- Webhook

**Response from the payment intent**

We obtained a new payment in our PinPad SDK and the process wake up the terminal, proceeding to end pay.

|  | |
| --| --|
| <img src="https://img.shields.io/badge/-16B10C" alt="method" style="max-width: 100%;"> 200 | The payment order was created successfully  | 
|<img src="https://img.shields.io/badge/-CA3823" alt="method" style="max-width: 100%;"> 401 | Unauthorized. |
| <img src="https://img.shields.io/badge/-CA3823" alt="method" style="max-width: 100%;"> 403 | Forbidden. |
| <img src="https://img.shields.io/badge/-CA3823" alt="method" style="max-width: 100%;"> 404 | Wrong param |
| <img src="https://img.shields.io/badge/-CA3823" alt="method" style="max-width: 100%;"> 500 | Internal Server Error.

Body Response

```json 
{ 
	 "reference":  "string", 
	 "amount":  1000, 
	 "auto_return":  true, 
	 "is_tip_enabled":  true, 
	 "serial_number_pos":  "string" 
}  
```

Body Headers
```cURL 
content-type:  application/json 
date:  Mon,15  Apr  2024  16:44:15  GMT 
strict-transport-security:  max-age=15724800;  
includeSubDomains 
``` 

#### WebHook
<a name="webhook-result"></a>

##### Overview

![](https://lh7-us.googleusercontent.com/docsz/AD_4nXei2d3U-rrOMp2UNTvYj4Ly_r9FDfjzpfFgsMUHJHQCbM3i0Dz_J62utgbcwlfrbE6SRqyzBUJUR4oUOM9pBt4Pff6Abj59Sp54MePNbtNPE5p-69MbilawiqJWnptSHmghw7nQNbuML1H0RQJcpC_zBQY?key=gc6WOPNG8yUyIZYN2Fwh-A)
<sub>Diagram. Webhook integration for server side SDK<sub>

This system notifies the status changes of your transactions via a webhook. Please ensure you have configured a webhook URL in your Clip portal. For more information, refer to the [Clip Webhook Documentation](https://developer.clip.mx/reference/referencia-postback-webhook).

##### Notification Events

The application will notify you of the following transaction status changes:

- `PENDING`: An external party wants to initiate a payment on the Clip terminal.
- `IN_PROCESS`: The terminal accepted and started the payment process on the POS.
- `CANCELED`: The external party canceled the transaction from their backend or from the terminal. They no longer want to continue with the payment process.
- `REJECTED`: The terminal had an issue processing the payment and the operator chose to cancel the process on the terminal.
- `APPROVED`: The payment process was successfully completed on the terminal.

##### Webhook Notification Details

<img src="https://img.shields.io/badge/POST-2175BF" alt="method" style="max-width: 100%;"> https://yourdomain.com/some-webhook-path

**/your-webhook-path** you need to define a webhook path. Pinpad sends a POST to notify changes.

When a transaction status changes, the application will send a POST request to the URL you have configured. The body of the request will be as follows:

```sh
curl --location 'https://yourdomain.com/some-webhook-path' \
	--data '
{ 
	"id": "pinpad-51bf57ad-a186-4ac4-8a88-22133s8829",
	"origin": "pinpad-payments-api",
	"event_type": "PINPAD_INTENT_STATUS_CHANGED"
}'
```

##### Json payload

| Field name | Description                                                          | Type   |
| ---------- | -------------------------------------------------------------------- | ------ |
| id         | Represents the internal ID used to identify a transaction.           | String |
| origin     | Indicates the application that generated and sent this notification. | String |
| event_type | Indicates the application that generated and sent this notification. | String |



##### Polling Strategy

We use a polling strategy for notifications. This means that our system will notify you that there is an update for a specific transaction. To retrieve the details of the update, you need to make a GET request to the following URL:

<img src="https://img.shields.io/badge/GET-2175BF" alt="method" style="max-width: 100%;"> https://api.payclip.io/f2f/pinpad/v1/payment?pinpadRequestId={id}

Example: `GET https://api.payclip.io/f2f/pinpad/v1/payment?pinpadRequestId=pinpad-51bf57ad-a186-4ac4-8a88-22133s8829`


###### Response body schema
```json
{
	"pinpadRequestId": "string",
	"reference": "string",
	"amount": "string",
	"createDate": "string",
	"status": "string"
}
```

| Field name      | Description                                                       | Type   |
| --------------- | ----------------------------------------------------------------- | ------ |
| pinpadRequestId | Represents the internal ID used to identify a transaction.        | String |
| reference       | Represents the reference you choose when creating the transaction | String |
| amount          | Represents the amount of the transaction                          | String |
| createDate      | Represents the creation date of the transaction                   | String |
| status          | Representes the status of the transaction                         | String |


> **Important Note**
>
> Ensure your webhook URL is correctly configured in the Clip portal.
The notification only indicates that there has been a status change; you must query the provided URL to get detailed information about the transaction update.


### Error codes in Server Side SDK

In the event of an error during the transaction process, it is important to understand the different types of errors that may occur. The Server Side SDK manages errors that happen before the payment is completed, while webhook handle errors that occur after the payment process.

| Error | Note |
| -- | -- | 
| 400 Bad Request | Check request body | 

**Example**
```json 
{
	"code":  "ERROR_BODY_STRUCTURE",
	"message":  "Amount  must  be  a  number"
}
```

 Error | Note |
| -- | -- | 
| 401 Unauthorized | Check api credentials |

**Example**
```json 
{
	"message":  "Unauthorized"
}
```



### Terminal result process
<a name="first-payment"></a>


After completing a payment intent creation by terminal SDK solution or Server Side SDK solution successfull,  the app will trigger its wake up  mechanism to resume the payment process within our POS Android terminal.

  <img src="https://lh7-us.googleusercontent.com/docsz/AD_4nXfpjKtwLIuBdGBfC8vrffPhLR5iUnQKHfQAWoPNpdwaMwWkigA2mm0eyFSYadrbuLQ6U3hr8TVa4WgXnVbECfsyGGR0mmPJt8qlglxZWFIg9r1KKxQRTz_wS8I6cma5nMRPvqKxuvLc8TIDWooI9VYB0Uc?key=Y9d8E1pWDfJ9scqtsvGnUQ" alt="developer portal" width="800"/>


## Stay Updated
<a name="stay_updated"></a>

We strongly recommend staying informed about new versions of our SDK and updating your library frequently. While we strive to provide clean updates with no breaking changes, keeping your SDK version up-to-date ensures you have access to the latest features and improvements.


And that's it! With these steps, you can start processing payments in your application 🤑


<p align="right">(<a href="#readme-top">back to top</a>)</p>