-- Datenbank anlegen und auswählen
CREATE DATABASE IF NOT EXISTS ams_fx_test;
USE ams_fx_test;

-- --------------------------------------------------------
-- Tabellenstruktur für Tabelle `raeume`
-- --------------------------------------------------------

CREATE TABLE `raeume` (
                          `raum_id` int(11) NOT NULL,
                          `bezeichnung` varchar(20) DEFAULT NULL,
                          `gebaeude` varchar(20) DEFAULT NULL,
                          `laenge_in_cm` double DEFAULT NULL,
                          `breite_in_cm` double DEFAULT NULL,
                          `verantwortlicher` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `raeume`
    ADD PRIMARY KEY (`raum_id`);

ALTER TABLE `raeume`
    MODIFY `raum_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;

COMMIT;
