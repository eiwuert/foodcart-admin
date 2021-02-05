# Willkommen bei FoodCart

FoodCart ist das Online Bestellsystem für deinen Lieferservice. Das vorliegende Projekt ist eines von Dreien. Es handelt sich hierbei um die Server-Anwendung basierend auf JavaScript / ReactJs. 

<p align="center">
<img src="https://github.com/habibhaidari1/food-cart-js/raw/master/public/icon.png" alt="icon" width="100"/>
</p>
<p align="center">
<img src="https://i.imgur.com/xFzhE9d.png" alt="screenshot" width="40%"/>
<img src="https://i.imgur.com/OWD7tlU.jpg" alt="screenshot" width="40%"/>
</p>

 - [Beispiel 🧪](#Beispiel)
 - [Konfiguration 💥](#Konfiguration)
	 - [Umgebungsvariablen 🏝](#Umgebungsvariablen)
 - [Kommentar 🌍](#Kommentar)
 - [FoodCart Projekte 🍲](#FoodCart-Projekte)


## Beispiel

Schau dir an wie FoodCart aussieht! Sollte dir es gefallen, dann würde ich mich über einen Stern freuen 😊

[Zur Demo-Installation](https://foodcart.habibhaidari1.de/)

**Tipp:** Tarife sind für die Postleitzahl 34119 hinterlegt. 


## Konfiguration

### Umgebungsvariablen

Unter `gradle.properties` muss die Domain, auf die der FoodCart-Server läuft, einkonfiguriert werden. Darüber hinaus kann auch die Frequenz festgelegt werden, mit der die App nach neuen Bestellungen sucht.

## Kommentar

Mit der Android App kann der Administrator die bisherigen Bestellungen ansehen. Bei neuen Bestellungen wird der Administrator benachrichtigt.
Da die Implementierung von Android Benachrichtigungen und Energiesparmaßnahmen von dem Smartphonehersteller diktiert wird, offenbart sich die Echtzeitbenachrichtigung als ein kompliziertes Unterfangen.
Um dennoch Echtzeitbenachtichtungen gewährleisten zu können, benutzt FoodCart einen Foregroundservice mit Wakelock.

Ein Refactoring des Programmcodes ist empfehlenswert, da die Android-App hektisch entwickelt wurde.

Für den Belegdruck empfehle ich einen Bluetoothfähigen Belegdrucker. Der von mir verwendete Drucker ist [hier](https://www.amazon.de/KKmoon-Thermodrucker-Quittungsdrucker-Kassendrucker-Belegdrucker/dp/B01MR79BFI/ref=sr_1_13?__mk_de_DE=%C3%85M%C3%85%C5%BD%C3%95%C3%91&dchild=1&keywords=bluetooth+receipt+printer&qid=1598003915&sr=8-13) verlinkt. 

### FoodCart-Projekte

| Projekt | Beschreibung |
|--|--|
| [foodcart-server](https://github.com/habibhaidari1/foodcart-server) | Server-Anwendung |
| [foodcart-client](https://github.com/habibhaidari1/foodcart-client) | Client-Anwendung |
| [foodcart-admin](https://github.com/habibhaidari1/foodcart-admin) | Client für Administratoren |
