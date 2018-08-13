package magelle.bank.repository;

import magelle.bank.data.Op;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountRepository {

    static AccountRepository instance = new AccountRepository();

    private Connection connection;

    public AccountRepository() {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "");
            connection.prepareStatement("CREATE TABLE ACCOUNT_OPERATION (" +
                    " SOLDE INTEGER NOT NULL, " +
                    " DATE TIMESTAMP NOT NULL, " +
                    " DEBIT INTEGER, " +
                    " CREDIT INTEGER " +
                    ")").execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static AccountRepository getInstance() {
        return instance;
    }

    public int getBalance() throws SQLException {
        int result;
        ResultSet res = this.connection.createStatement().executeQuery("SELECT SOLDE FROM ACCOUNT_OPERATION ORDER BY date desc");
        if (res.next()) {
            result = res.getInt(1);
        }
        else {
            result = 0;
        }
        return result;
    }

    public void save(Op op) throws SQLException {
        PreparedStatement ps = this.connection.prepareStatement("INSERT INTO ACCOUNT_OPERATION VALUES(?, ?, ?, ?)");
        ps.setTimestamp(2, new Timestamp(op.getDate().getTime()));
        ps.setInt(1, op.getSum());
        if (op.getAmount1() != null) {
            ps.setLong(3, op.getAmount1());
        } else {
            ps.setNull(3, Types.INTEGER);
        }
        if (op.getAmount2() != null) {
            ps.setLong(4, op.getAmount2());
        } else {
            ps.setNull(4, Types.DOUBLE);
        }

        ps.executeUpdate();
        ps.close();
    }

    public List<Op> getOperations() throws SQLException {
        List<Op> ops = new ArrayList<>();
        ResultSet res = this.connection.createStatement().executeQuery("SELECT * FROM ACCOUNT_OPERATION ORDER BY DATE desc");
        while (res.next()) {
            Op op = new Op();
            op.setSum(res.getInt(1));
            op.setDate(res.getTimestamp(2));
            if (res.getNString(3) != null) {
                op.setAmount1(res.getInt(3));
            }
            if (res.getNString(4) != null) {
                op.setAmount2(res.getLong(4));
            }
            ops.add(op);
        }
        res.close();
        return ops;
    }
}
