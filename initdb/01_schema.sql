-- Datenbank anlegen und auswählen
CREATE DATABASE IF NOT EXISTS ams_fx_test;
USE ams_fx_test;

-- --------------------------------------------------------
-- Tabellenstruktur fuer `raeume`
-- --------------------------------------------------------

CREATE TABLE `raeume` (
                          `raum_id` INT NOT NULL AUTO_INCREMENT,
                          `bezeichnung` VARCHAR(20) DEFAULT NULL,
                          `gebaeude` VARCHAR(20) DEFAULT NULL,
                          `laenge_in_cm` DOUBLE DEFAULT NULL,
                          `breite_in_cm` DOUBLE DEFAULT NULL,
                          `verantwortlicher` VARCHAR(20) DEFAULT NULL,
                          PRIMARY KEY (`raum_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------
-- Tabellenstruktur fuer `geraete`
-- --------------------------------------------------------

CREATE TABLE `geraete` (
                           `geraet_id` INT NOT NULL AUTO_INCREMENT,
                           `bezeichnung` VARCHAR(50) NOT NULL,
                           `defekt` TINYINT(1) NOT NULL DEFAULT 0,
                           `hersteller` VARCHAR(50) DEFAULT NULL,
                           `modell` VARCHAR(50) DEFAULT NULL,
                           `kauf_datum` DATE DEFAULT NULL,
                           `garantie_monate` INT DEFAULT NULL,
                           PRIMARY KEY (`geraet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

COMMIT;
