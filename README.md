# AMS JavaFX

## Einrichten des Projekts

### Vorbereitung
1. IntelliJ und Docker installieren
2. Existierende MariaDB-Prozesse auf Port 3306 beenden!

### Projekt einrichten
1. IntelliJ -> File -> New -> Project from Version Control (.git-Link kopieren und einfügen)
2. ```docker-compose up``` oder compose.yaml öffnen und ausführen (Doppel-Run-Symbol)
3. AMSApplication ausführen (Ordner src/main/java/de.bs1bt.ams) -> Run auf main
4. Falls JDK fehlt oder Warnungen stören: Zahnrad -> Project Structure -> SDK: temurin-21 Eclipse Temurin 21.0.8 (getestet für Win11)