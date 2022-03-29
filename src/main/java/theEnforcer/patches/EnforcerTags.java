package theEnforcer.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;


public class EnforcerTags {
    @SpireEnum public static AbstractCard.CardTags CAN_FLIP; // Used when a card has the "Flip" Keyword
    @SpireEnum public static AbstractCard.CardTags STIM; // Used for "Stimulant" Cards.
}
