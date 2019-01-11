# Willkommen bei GuideLines
Hier findest du alle Infos, die du brauchst

##  Gruppe FR-21 Praktikum Software Engineering der Hochschule für angewandte Wissenschaften München
###  Die Gruppe besteht aus:
* Teng Fung Lam
* Beniamin Bratulescu
* Aleksandar Ivanov
* Alexander Schreiner

## Alexa Skill zur ÖPNV "GuideLines" - Systemidee
Unser großes Projekt zur Vorlesung dieses Semester ist gemeinsam mit Tourismusmanagment-Studenten der FK14
einen Alexa Skill zu entwickeln. Gemeinsam mit Timo Bunghardt und Sina Lorenz entwickeln wir einen Alexa-Skill, der dem Benutzer hilft Bahn- und Busverbindungen intuitiv zu erfragen.
Durch die anfängliche Erstellung eines Nutzerprofils werden personalisierte Ziele festgelegt.
Diese und die Berücksichtigung der aktuellen Verkehrslage helfen dabei, eine verlässliche und schnelle Auskunft über die Abfahrtszeit bereitzustellen.
Die Storyline als grobes "Gerüst" dieser Anwendung ist [hier](https://getstoryline.com/shared/projects/bf4d507c3a4a7754d54daa1de084d6f40d21c23f)
zu finden.

## Fachklassenmodell (Analyseklassenmodell)
![FachklassenmodellV3](https://user-images.githubusercontent.com/35468278/48474074-1120c280-e7fa-11e8-937d-9a1174bf9a71.png)

## Klassendiagramm
![Klassendiagramm](https://github.com/sweIhm-ws2018-19/skillproject-fr-21/blob/master/Diagramms/Bildschirmfoto%202019-01-10%20um%2017.01.51.png)

## Fachklassenmodell (Analyseklassenmodell)![FachklassenmodellV3](https://github.com/sweIhm-ws2018-19/skillproject-fr-21/blob/master/Diagramms/Fachklassenmodell.png)

## Wie funktioniert's?
GuideLines ist ein Alexa Skill in Java impleentiert und benutzt HereAPI für die Adressübersetzung (String text zu Adresse [HereAPI autocomplete](https://developer.here.com/documentation/geocoder-autocomplete/topics/what-is.html). Wenn die Adresse nicht gefunden wird, schickt GuideLines eine weitere Anfrage an [HereAPI Places](https://developer.here.com/documentation/places/topics/what-is.html). GuideLines sucht nach Stationen via [HereAPI Geocoder](https://developer.here.com/documentation/geocoder/topics/what-is.html). Wir ermitteln den Fahrplan mit dem [Public Transit API](https://developer.here.com/documentation/transit/topics/quick-start-routing.html) von Here. Zusätzlich "übersetzt" die App die vom User eingegebene Hausnummer in einen Integer Wert.
