package de.bs1bt.ams.repository;

import de.bs1bt.ams.model.Raum;

import java.sql.*;
import java.util.ArrayList;

public class RaumMySQLRepository implements RaumRepository {
    private Connection connection = null;
    private PreparedStatement ptmt = null;
    private ResultSet resultSet = null;

    public void erstelleTabelle() throws RepositoryException {
        // Quelle: https://www.tutorialspoint.com/java_mysql/java_mysql_create_tables.htm
        String query = "CREATE TABLE `raeume` (raum_id integer PRIMARY KEY AUTO_INCREMENT, " +
                "bezeichnung varchar(20), " +
                "gebaeude varchar(20), " +
                "laenge_in_cm double, " +
                "breite_in_cm double, " +
                "verantwortlicher varchar(20))";
        System.out.println(query);
        try {
            connection = DBConnectionSingleton.getConnection();
            ptmt = connection.prepareStatement(query);
            ptmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    public void loescheTabelle() throws RepositoryException {
        String query = "DROP TABLE raeume";
        System.out.println(query);
        try {
            connection = DBConnectionSingleton.getConnection();
            ptmt = connection.prepareStatement(query);
            ptmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public Raum hole(int id) throws RepositoryException {
        Raum raum = null;
        try {
            String queryString = "SELECT * FROM raeume WHERE raum_id=?";
            connection = DBConnectionSingleton.getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setInt(1, id);
            resultSet = ptmt.executeQuery();

            int count = 0;
            while (resultSet.next())
            {
                if(count > 0) {
                    // soweit sollte es bei unique PK nie kommen:
                    throw new RepositoryException("Der Datensatz ist nicht einzigartig.");
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
                throw new RepositoryException("Es ist kein Raum mit der raum_id=" + id + " vorhanden.");
            }

        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
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
                throw new RepositoryException(e.getMessage());
            }
        }

        return raum;
    }

    @Override
    public ArrayList<Raum> holeAlle() throws RepositoryException {
        ArrayList<Raum> liste = new ArrayList<Raum>();

        try {
            String query = "SELECT * FROM raeume";
            connection = DBConnectionSingleton.getConnection();
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
            throw new RepositoryException(e.getMessage());
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
                throw new RepositoryException(e.getMessage());
            }
        }
        return liste;
    }

    @Override
    public int erstelle(Raum raumModel) throws RepositoryException {
        try {
            String query = "INSERT INTO raeume (bezeichnung, gebaeude, laenge_in_cm, breite_in_cm) VALUES (?,?,?,?)";

            connection = DBConnectionSingleton.getConnection();
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
            throw new RepositoryException(e.getMessage());
        } finally {
            try {
                if (ptmt != null) {
                    ptmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RepositoryException(e.getMessage());
            }
        }
        return -1;
    }

    @Override
    public void aktualisiere(Raum raumModel) throws RepositoryException {
        try {
            String query = "UPDATE `raeume` SET bezeichnung=?, gebaeude=?, laenge_in_cm=?, breite_in_cm=? WHERE raum_id=?";

            connection = DBConnectionSingleton.getConnection();
            ptmt = connection.prepareStatement(query);
            int param = 0;
            ptmt.setString(++param, raumModel.getBezeichnung());
            ptmt.setString(++param, raumModel.getGebaeude());
            ptmt.setDouble(++param, raumModel.getLaengeInCm());
            ptmt.setDouble(++param, raumModel.getBreiteInCm());
            ptmt.setInt(++param, raumModel.getId());
            ptmt.executeUpdate();

        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        } finally {
            try {
                if (ptmt != null) {
                    ptmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RepositoryException(e.getMessage());
            }
        }
    }

    @Override
    public void loesche(int id) throws RepositoryException {

        try {
            String query = "DELETE FROM raeume WHERE raum_id=?";

            connection = DBConnectionSingleton.getConnection();
            ptmt = connection.prepareStatement(query);
            ptmt.setInt(1, id);
            ptmt.executeUpdate();

        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        } finally {
            try {
                if (ptmt != null) {
                    ptmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RepositoryException(e.getMessage());
            }
        }
    }

    @Override
    public void loesche(Raum raumModel) throws RepositoryException {
        loesche(raumModel.getId());
    }
}
