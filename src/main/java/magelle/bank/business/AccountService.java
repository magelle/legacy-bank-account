package magelle.bank.business;

import magelle.bank.data.Op;
import magelle.bank.repository.AccountRepository;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AccountService {


    private static AccountService instance = new AccountService();

    public static AccountService getInstance() {
        return instance;
    }

    public int add(int amount) throws SQLException {
        if (amount < 0) {
            throw new IllegalArgumentException("invalid param");
        }
        int balance = AccountRepository.getInstance().getBalance();
        if (balance - amount < 0)
            throw new IllegalStateException("Invalid State");
        Op op = new Op();
        op.setDate(new Date());
        op.setAmount1(amount);
        op.setSum(balance + amount);
        AccountRepository.getInstance().save(op);
        return balance + amount;
    }

    public int remove(int amount) throws SQLException {
        if (amount <= 0)
            throw new IllegalArgumentException("invalid arg");
        int balance = AccountRepository.getInstance().getBalance();
        if (balance - amount < 0) {
            throw new IllegalStateException("Invalid State");
        }
        Op op = new Op();
        op.setDate(new Date());
        op.setAmount2((long) amount);
        op.setSum(balance - amount);
        AccountRepository.getInstance().save(op);
        return amount;
    }

    public String printStatement() throws SQLException {
        List<Op> ops = AccountRepository.getInstance().getOperations();
        StringBuilder stringBuilder = new StringBuilder("| date | credit | debit | balance |\n");
        for(Op op : ops) {
            stringBuilder.append("| ");
            stringBuilder.append(new SimpleDateFormat("yyy-MM-dd").format(op.getDate()));
            stringBuilder.append(" | ");
            stringBuilder.append(op.getAmount1() != null ? op.getAmount1() : "");
            stringBuilder.append(" | ");
            stringBuilder.append(op.getAmount2() != null ? op.getAmount2() : "");
            stringBuilder.append(" | ");
            stringBuilder.append(op.getSum());
            stringBuilder.append(" |");
            stringBuilder.append("\n");
        }
        String statement = stringBuilder.toString();
        return statement;
    }
}
