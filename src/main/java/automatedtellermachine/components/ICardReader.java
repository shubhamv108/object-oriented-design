package automatedtellermachine.components;

import automatedtellermachine.cards.Card;
import automatedtellermachine.cards.CardInfo;

public interface ICardReader {

    default CardInfo acceptCard(Card card) {
        return this.fetchCardDetails(card);
    }

    private CardInfo fetchCardDetails(Card card) { return null; }

}
