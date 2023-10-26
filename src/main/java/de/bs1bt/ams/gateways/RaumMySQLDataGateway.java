package de.bs1bt.ams.model;

import de.bs1bt.ams.db.MySQLConnection;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RaumMySQLDataGateway implements iRaumDataGateway {
    private Connection connection = null;
    private PreparedStatement ptmt = null;
    private ResultSet resultSet = null;

    @Override
    public void erstelleTabelle() throws DataGatewayException {

    }

    @Override
    public void loescheTabelle() throws DataGatewayException {

    }

    @Override
    public Raum hole(int id) throws DataGatewayException {
        Raum raum = null;
        try {
            String queryString = "SELECT * FROM location WHERE id=?";
            connection = MySQLConnection.getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setInt(1, id);
            resultSet = ptmt.executeQuery();

            int count = 0;
            while (resultSet.next())
            {
                if(count > 0) {
                    throw new SQLException("Dataset not unique.");
                }

                raum = new Raum( resultSet.getInt("id"),
                        resultSet.getString("bezeichnung"),
                        resultSet.getString("gebaeude"),
                        resultSet.getDouble("breiteInCm"),
                        resultSet.getDouble("laengeInCm")
                );
                count++;
            }
            if ( 0 == count || null == raum) {
                throw new DataGatewayException("Es ist kein Raum mit der id=" + id + " vorhanden.");
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

    @Override
    public ArrayList<Raum> holeAlle() throws DataGatewayException {
        ArrayList<Raum> liste = new ArrayList<Raum>();

        try {
            String query = "SELECT * FROM raeume";
            connection = MySQLConnection.getConnection();
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

    @Override
    public void erstelle(Raum raumModel) throws DataGatewayException {
        try {
            String query = "INSERT INTO raeume (bezeichnung, gebaeude, laenge_in_cm, breite_in_cm) VALUES (?,?,?,?)";

            connection = MySQLConnection.getConnection();
            ptmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            int param = 0;
            ptmt.setString(++param, raumModel.getBezeichnung());
            ptmt.setString(++param, raumModel.getGebaeude());
            ptmt.setDouble(++param, raumModel.getLaengeInCm());
            ptmt.setDouble(++param, raumModel.getBreiteInCm());
            ptmt.setInt(++param, raumModel.getId());
            ptmt.executeUpdate();

            // get the last added ID
            ResultSet rs = ptmt.getGeneratedKeys();
            if(rs.next()) {
                raumModel.setId( rs.getInt(1) );
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
    }

    @Override
    public void aktualisiere(Raum raumModel) throws DataGatewayException {
        try {
            String query = "UPDATE `raeume` SET bezeichnung=?, gebaeude=?, laenge_in_cm=?, breite_in_cm=? WHERE raum_id=?";

            connection = MySQLConnection.getConnection();
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

    @Override
    public void loesche(int id) throws DataGatewayException {

        try {
            String query = "DELETE FROM location WHERE id=?";

            connection = MySQLConnection.getConnection();
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

    @Override
    public void loesche(Raum raumModel) throws DataGatewayException {
        loesche(raumModel.getId());
    }
}
