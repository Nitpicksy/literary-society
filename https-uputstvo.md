# lokalna https konfiguracija

* pronaći $JAVA_HOME na vašem OS-u, i unutar java-1.11.openjdk-amd64 ići na /lib/security 
	* napomena imate drugačiji JDK, tj. vam se ne zove java-1.11.*, onda prilikom paljenja bilo koje spring boot aplikacije u konzoli intelliJ ispisuje koji JDK vi koristite 
* unutar lib/security u cacerts datoteku importovati sertifikate: zuul, eureka, tls, root 

* moja linux komanda: 
``` 
sudo keytool -importkeystore -srckeystore ~/master/nitpicksy/literary-society/tls/certs/zuul/keystore/zuul.keystore.p12 -alias zuul -destkeystore cacerts -storepass changeit  #komande za windows su iste, sem putanje ka keystoru.
```

* šifre svih keystore i truststore je password
```
> Enter source keystore password: password
```
* eureka: 
```
sudo keytool -importkeystore -srckeystore ~/master/nitpicksy/literary-society/tls/certs/eureka/keystore/eureka.keystore.p12 -alias eureka -destkeystore cacerts -storepass changeit
```

* ROOT:
```
sudo keytool -importcert -file ~/master/nitpicksy/literary-society/tls/ca/root-ca.crt -alias root -destkeystore cacerts -storepass changeit
```

* TLS-CA 
```
sudo keytool -importcert -file ~/master/nitpicksy/literary-society/tls/ca/tls-ca.crt -alias tls-ca -destkeystore cacerts -storepass changeit
```

## dodavanje sertifikata u browser

* onda sledi procedura dodavanja CLIENT sertifikata u chrome, što se na windowsu razlikuje od linux-a, a to se nalazi u pdfu jeleninog kursa

## testiranje
* dodati u željeni alat client.keystore.p12 u insomniu ili postman (ili iz browsera) i napraviti zahtev ka 

```
https://localhost:8080/literary-society/api/auth/health
```
* preduslov: upaljeni LU, KP, Eureka, Zuul, Paypal, Bank servisi.
* ako želite da testirate samo jedan deo te mreže, u KP-u testServiceImpl zakomentarisati feignClient zahteve ka banci i paypalu.

### rezultat ispisa: 

Literary Society is up and running!Payment Gateway ID=payment-gateway:68e6e209f37d994e3b05f54e482b136c is up and running!
Bank is up and running!
PayPal Service is up an running!
Payment Gateway ID=payment-gateway:68e6e209f37d994e3b05f54e482b136c is up and running!
Bank is up and running!
PayPal Service is up an running!


