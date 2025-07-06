# React Native Bridge

Cette ébauche de module React Native expose une API minimale pour
rechercher des imprimantes TCP/IP et envoyer un ticket au format texte
à l'aide de la librairie Java existante.

Le code Android se trouve dans `android/src` et est basé sur les
`Native Modules` de React Native.

**Fonctionnalités principales :**

- `scanNetworkPrinters(baseIp, start, end)` : explore une plage d'adresses
  et renvoie les hôtes répondant sur le port spécifié (par défaut 9100).
- `print(ip, port, text)` : se connecte à une imprimante à l'adresse
  donnée et envoie le texte à imprimer.

Cette implémentation reste volontairement simple et pourra être
complétée selon les besoins de l'application.
