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



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
  </ol>
</details>



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

##### Compose
You will need compose in order to use this SDK.

##### API Key
You'll need an API key to authenticate with our services. If you don't have one yet, you can generate it on the [Clip Developers](https://developer.clip.mx/reference/token-de-autenticacion#2-crea-un-token-de-autenticaci%C3%B3n-con-codificaci%C3%B3n-base64) page.

##### Dependency
To download the SDK dependency, you'll need to add JitPack to your Maven repositories. Follow these steps:

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
    implementation("com.blaze.pinpad:pinpad-sdk:latest-version")
}
```

##### Installation Steps
- Add JitPack Repository: Open your settings.gradle.kts file and add the JitPack repository to your Maven repositories list.
- Add Dependency: Open your build.gradle.kts file and add the SDK dependency.

###### Stay Updated
We strongly recommend staying informed about new versions of our SDK and updating your library frequently. While we strive to provide clean updates with no breaking changes, keeping your SDK version up-to-date ensures you have access to the latest features and improvements.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



## How to use the SDK?

Before initiating a payment, you'll need to obtain a request ID from our SDK. We've designed our SDK to separate the payment process from the creation of a payment request, allowing you to handle loading states according to your application's requirements.

```PaymentViewModel.kt
class PaymentViewModel : ViewModel() {

    private val _client = ClipPayment.Builder()
        .setUser(YOUR_CLIP_USER)
        .setApiKey(YOUR_CLIP_TOKEN)
        .build()

    fun onPay(amount: Double, message: String) = viewModelScope.launch {
        _client.create(amount, message)
            .onSuccess {
                handleCreationSuccess()
            }
            .onFailure {
                handleCreationFailure()
            }
    }
}
```

To ensure smooth integration of our SDK into your application, we recommend handling the payment logic within your ViewModel or a similar component. This approach helps to keep your codebase organized and maintainable.

Once you've successfully obtained the request ID, our SDK provides a launcher that simplifies the payment process and handles all the necessary results. Below is a sample implementation of the launcher:

```Payment.kt
@Composable
fun PaymentScreen() {
    val launcher = remember { ClipLauncherFactory.create() }

    launcher.setPaymentHandler(
        onSuccess = { handlePaymentSuccess() },
        onCancelled = { handlePaymentCancelled() },
        onFailure = { handlePaymentCancelled() }
    )

    Button(
        onClick = {
            launcher.startPayment(REQUEST_ID)
        }
    ) {
        Text(text = "START PAYMENT")
    }
}
```

<p align="right">(<a href="#readme-top">back to top</a>)</p>
[Laravel.com]: https://img.shields.io/badge/Laravel-FF2D20?style=for-the-badge&logo=laravel&logoColor=white
[Laravel-url]: https://laravel.com
[Bootstrap.com]: https://img.shields.io/badge/Bootstrap-563D7C?style=for-the-badge&logo=bootstrap&logoColor=white
[Bootstrap-url]: https://getbootstrap.com
[JQuery.com]: https://img.shields.io/badge/jQuery-0769AD?style=for-the-badge&logo=jquery&logoColor=white
[JQuery-url]: https://jquery.com 
