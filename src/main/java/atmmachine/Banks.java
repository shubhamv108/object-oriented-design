package atmmachine;

import atmmachine.accounts.Account;
import atmmachine.exceptions.InvalidCardException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Banks {

    private final Map<String, Bank> BANK_CARD_CODE_TO_BANK = new HashMap<>();

    private Banks() {}

    public static Banks getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {
        private static final Banks INSTANCE = new Banks();
    }

    public Account getAccount(final String cardNumber) {
        String bankCardCode = cardNumber.substring(0, 4);
        Bank bank = BANK_CARD_CODE_TO_BANK.get(bankCardCode);
        return Optional.ofNullable(bank)
                .map(b -> bank.getAccountByCardNumber(cardNumber))
                .orElseThrow(InvalidCardException::new);
    }

}
