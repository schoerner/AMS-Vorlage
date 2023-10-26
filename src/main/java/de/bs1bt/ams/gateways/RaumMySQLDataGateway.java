package de.bs1bt.ams.gateways;

import de.bs1bt.ams.model.Raum;

import java.sql.*;
import java.util.ArrayList;

public class RaumMySQLDataGateway {
    private Connection connection = null;
    private PreparedStatement ptmt = null;
    private ResultSet resultSet = null;

    public void erstelleTabelle() throws DataGatewayException {
        // Quelle: https://www.tutorialspoint.com/java_mysql/java_mysql_create_tables.htm
        String query = "CREATE TABLE `raeume` (raum_id integer PRIMARY KEY AUTO_INCREMENT, " +
                "bezeichnung varchar(20), " +
                "gebaeude varchar(20), " +
                "laenge_in_cm double, " +
                "breite_in_cm double, " +
                "verantwortlicher varchar(20))";
        System.out.println(query);
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ams_fx_test", "schueler", "Geheim01");
            ptmt = connection.prepareStatement(query);
            ptmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataGatewayException(e.getMessage());
        }
    }

    public void loescheTabelle() throws DataGatewayException {
        String query = "DROP TABLE raeume";
        System.out.println(query);
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ams_fx_test", "schueler", "Geheim01");
            ptmt = connection.prepareStatement(query);
            ptmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataGatewayException(e.getMessage());
        }
    }

    public Raum hole(int id) throws DataGatewayException {
        Raum raum = null;
        try {
            String queryString = "SELECT * FROM raeume WHERE raum_id=?";
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ams_fx_test", "schueler", "Geheim01");
            ptmt = connection.prepareStatement(queryString);
            ptmt.setInt(1, id);
            resultSet = ptmt.executeQuery();

            int count = 0;
            while (resultSet.next())
            {
                if(count > 0) {
                    // soweit sollte es bei unique PK nie kommen:
                    throw new DataGatewayException("Der Datensatz ist nicht einzigartig.");
                }

                raum = new Raum( resultSet.getInt("raum_id"),
                        resultSet.getString("bezeichnung"),
                        resultSet.getString("gebaeude"),
                        resultSet.getDouble("breite_in_cm"),
                        resultSet.getDouble("laenge_in_cm")
                );
                count++;
            }
            if ( 0 == count || null == raum) {
                throw new DataGatewayException("Es ist kein Raum mit der raum_id=" + id + " vorhanden.");
            }

        } catch (SQLException e) {
            throw new DataGatewayException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(); // soweit sollte es bei bestehenden, validen Daten aus der DB nie kommen
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (ptmt != null) {
                    ptmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new DataGatewayException(e.getMessage());
            }
        }

        return raum;
    }

    public ArrayList<Raum> holeAlle() throws DataGatewayException {
        ArrayList<Raum> liste = new ArrayList<Raum>();

        try {
            String query = "SELECT * FROM raeume";
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ams_fx_test", "schueler", "Geheim01");
            ptmt = connection.prepareStatement(query);
            resultSet = ptmt.executeQuery();
            while (resultSet.next()) {
                Raum raum = new Raum( resultSet.getInt("raum_id"),
                        resultSet.getString("bezeichnung"),
                        resultSet.getString("gebaeude"),
                        resultSet.getDouble("breite_in_cm"),
                        resultSet.getDouble("laenge_in_cm")
                );
                liste.add(raum);
            }
        } catch (SQLException e) {
            throw new DataGatewayException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(); // soweit sollte es bei bestehenden, validen Daten aus der DB nie kommen
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (ptmt != null) {
                    ptmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new DataGatewayException(e.getMessage());
            }
        }
        return liste;
    }

    public int erstelle(Raum raumModel) throws DataGatewayException {
        try {
            String query = "INSERT INTO raeume (bezeichnung, gebaeude, laenge_in_cm, breite_in_cm) VALUES (?,?,?,?)";

            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ams_fx_test", "schueler", "Geheim01");
            ptmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            int param = 0;
            ptmt.setString(++param, raumModel.getBezeichnung());
            ptmt.setString(++param, raumModel.getGebaeude());
            ptmt.setDouble(++param, raumModel.getLaengeInCm());
            ptmt.setDouble(++param, raumModel.getBreiteInCm());
            ptmt.executeUpdate();

            // get the last added ID
            ResultSet rs = ptmt.getGeneratedKeys();
            if(rs.next()) {
                raumModel.setId( rs.getInt(1) );
                return raumModel.getId();
            }

        } catch (SQLException e) {
            throw new DataGatewayException(e.getMessage());
        } finally {
            try {
                if (ptmt != null) {
                    ptmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new DataGatewayException(e.getMessage());
            }
        }
        return -1;
    }

    public void aktualisiere(Raum raumModel) throws DataGatewayException {
        try {
            String query = "UPDATE `raeume` SET bezeichnung=?, gebaeude=?, laenge_in_cm=?, breite_in_cm=? WHERE raum_id=?";

            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ams_fx_test", "Schueler", "Geheim01");
            ptmt = connection.prepareStatement(query);
            int param = 0;
            ptmt.setString(++param, raumModel.getBezeichnung());
            ptmt.setString(++param, raumModel.getGebaeude());
            ptmt.setDouble(++param, raumModel.getLaengeInCm());
            ptmt.setDouble(++param, raumModel.getBreiteInCm());
            ptmt.setInt(++param, raumModel.getId());
            ptmt.executeUpdate();

        } catch (SQLException e) {
            throw new DataGatewayException(e.getMessage());
        } finally {
            try {
                if (ptmt != null) {
                    ptmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new DataGatewayException(e.getMessage());
            }
        }
    }

    public void loesche(int id) throws DataGatewayException {

        try {
            String query = "DELETE FROM raeume WHERE raum_id=?";

            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ams_fx_test", "schueler", "Geheim01");
            ptmt = connection.prepareStatement(query);
            ptmt.setInt(1, id);
            ptmt.executeUpdate();

        } catch (SQLException e) {
            throw new DataGatewayException(e.getMessage());
        } finally {
            try {
                if (ptmt != null) {
                    ptmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new DataGatewayException(e.getMessage());
            }
        }
    }

    public void loesche(Raum raumModel) throws DataGatewayException {
        loesche(raumModel.getId());
    }
}
