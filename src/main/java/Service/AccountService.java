package Service;

import Model.Account;
import DAO.AccountDAO;

import java.util.List;


public class AccountService {
    AccountDAO accDAO;

    public AccountService() {
        accDAO = new AccountDAO();

    }

    public AccountService(AccountDAO accDAO) {
        this.accDAO = accDAO;

    }

    public Account insertAccount (Account account) {
        return accDAO.addAccount(account);
    }

   public Account LogonAccount (Account account) {
    return accDAO.checkLogOn(account);
   }

}
