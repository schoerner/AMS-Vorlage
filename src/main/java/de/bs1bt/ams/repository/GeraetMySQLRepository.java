package de.bs1bt.ams.repository;

import de.bs1bt.ams.model.Geraet;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class GeraetMySQLRepository implements GeraetRepository {

    private Connection connection = null;
    private PreparedStatement ptmt = null;
    private ResultSet resultSet = null;

    private Geraet mapResultSetToGeraet(ResultSet rs) throws Exception {
        Geraet geraet = new Geraet();
        geraet.setId(rs.getInt("geraet_id"));
        geraet.setBezeichnung(rs.getString("bezeichnung"));
        geraet.setDefekt(rs.getBoolean("defekt"));
        geraet.setHersteller(rs.getString("hersteller"));
        geraet.setModell(rs.getString("modell"));

        Date kaufDatumSql = rs.getDate("kauf_datum");
        if (kaufDatumSql != null) {
            geraet.setKaufDatum(kaufDatumSql.toLocalDate());
        }

        // NULL-Behandlung bei garantie_monate: getInt() liefert 0 bei NULL
        int garantie = rs.getInt("garantie_monate");
        // wenn du NULL unterscheiden willst:
        // if (!rs.wasNull()) { ... }
        geraet.setGarantieInMonaten(garantie);

        return geraet;
    }

    @Override
    public Geraet hole(int id) throws RepositoryException {
        Geraet geraet = null;
        try {
            String queryString = "SELECT * FROM geraete WHERE geraet_id = ?";
            connection = DBConnectionSingleton.getConnection();
            ptmt = connection.prepareStatement(queryString);
            ptmt.setInt(1, id);
            resultSet = ptmt.executeQuery();

            int count = 0;
            while (resultSet.next()) {
                if (count > 0) {
                    throw new RepositoryException("Der Datensatz ist nicht eindeutig (geraet_id=" + id + ").");
                }
                geraet = mapResultSetToGeraet(resultSet);
                count++;
            }

            if (geraet == null) {
                throw new RepositoryException("Es ist kein Geraet mit der geraet_id=" + id + " vorhanden.");
            }

        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(); // Sollte bei validen Daten nicht auftreten
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
        return geraet;
    }

    @Override
    public ArrayList<Geraet> holeAlle() throws RepositoryException {
        ArrayList<Geraet> liste = new ArrayList<>();

        try {
            String query = "SELECT * FROM geraete";
            connection = DBConnectionSingleton.getConnection();
            ptmt = connection.prepareStatement(query);
            resultSet = ptmt.executeQuery();

            while (resultSet.next()) {
                try {
                    Geraet geraet = mapResultSetToGeraet(resultSet);
                    liste.add(geraet);
                } catch (Exception e) {
                    e.printStackTrace(); // "kaputter" Datensatz wird übersprungen
                }
            }

        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
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
    public int erstelle(Geraet geraetModel) throws RepositoryException {
        try {
            String query = "INSERT INTO geraete " +
                    "(bezeichnung, defekt, hersteller, modell, kauf_datum, garantie_monate) " +
                    "VALUES (?,?,?,?,?,?)";

            connection = DBConnectionSingleton.getConnection();
            ptmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            int param = 0;
            ptmt.setString(++param, geraetModel.getBezeichnung());
            ptmt.setBoolean(++param, geraetModel.isDefekt());
            ptmt.setString(++param, geraetModel.getHersteller());
            ptmt.setString(++param, geraetModel.getModell());

            LocalDate kaufDatum = geraetModel.getKaufDatum();
            if (kaufDatum != null) {
                ptmt.setDate(++param, Date.valueOf(kaufDatum));
            } else {
                ptmt.setNull(++param, Types.DATE);
            }

            ptmt.setInt(++param, geraetModel.getGarantieInMonaten());

            ptmt.executeUpdate();

            ResultSet rs = ptmt.getGeneratedKeys();
            if (rs.next()) {
                geraetModel.setId(rs.getInt(1));
                return geraetModel.getId();
            }

        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
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
    public void aktualisiere(Geraet geraetModel) throws RepositoryException {
        try {
            String query = "UPDATE geraete SET " +
                    "bezeichnung=?, defekt=?, hersteller=?, modell=?, kauf_datum=?, garantie_monate=? " +
                    "WHERE geraet_id=?";

            connection = DBConnectionSingleton.getConnection();
            ptmt = connection.prepareStatement(query);
            int param = 0;
            ptmt.setString(++param, geraetModel.getBezeichnung());
            ptmt.setBoolean(++param, geraetModel.isDefekt());
            ptmt.setString(++param, geraetModel.getHersteller());
            ptmt.setString(++param, geraetModel.getModell());

            LocalDate kaufDatum = geraetModel.getKaufDatum();
            if (kaufDatum != null) {
                ptmt.setDate(++param, Date.valueOf(kaufDatum));
            } else {
                ptmt.setNull(++param, Types.DATE);
            }

            ptmt.setInt(++param, geraetModel.getGarantieInMonaten());
            ptmt.setInt(++param, geraetModel.getId());

            ptmt.executeUpdate();

        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        } catch (Exception e) {
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
            String query = "DELETE FROM geraete WHERE geraet_id=?";

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
    public void loesche(Geraet geraetModel) throws RepositoryException {
        loesche(geraetModel.getId());
    }
}
