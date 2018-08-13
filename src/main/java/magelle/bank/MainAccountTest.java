package magelle.bank;

import magelle.bank.business.AccountService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MainAccountTest {

    public static void main(String [] args) throws SQLException {
        AccountService accountService = new AccountService();

        accountService.add(1000);
        accountService.add(2000);
        accountService.remove(500);

        System.out.println(accountService.printStatement());


        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if (!accountService.printStatement().equals(
                "| date | credit | debit | balance |\n" +
                        "| " + today + " |  | 500 | 2500 |\n" +
                        "| " + today + " | 2000 |  | 3000 |\n" +
                        "| " + today + " | 1000 |  | 1000 |\n"
        )) {
            throw new RuntimeException("Test failed !!!");
        }
    }

}
