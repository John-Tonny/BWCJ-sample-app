### Description
The **Sample** Android application for a simple wallet implementation that relays on [**BWS Bitcore wallet service**](https://github.com/bitpay/bitcore-wallet-service) and uses [**Kuvabase-BWCJ**](https://github.com/kuvabase/Kuvabase-BWCJ) library.

---
### Get started

Clone or download a sample application.
Start your own local [Bitcore wallet service](https://github.com/bitpay/bitcore-wallet-service) instance. In this example we assume you have bitcore-wallet-service running.
Set up url to your bitcore-wallet-service in a `ApiUrls.java`

```java
package org.openkuva.kuvabase.bwcj.sample;

public class ApiUrls {
    public static String URL_BWS ="http://url.to.your.running.instance/bws/api/";
}
```

Build and run a sample application.
