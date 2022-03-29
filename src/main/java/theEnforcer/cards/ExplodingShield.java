package theEnforcer.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theEnforcer.DefaultMod;
import theEnforcer.characters.TheEnforcer;
import theEnforcer.powers.ExplodingShieldPower;
import theEnforcer.powers.UpgradedExplodingShieldPower;

import static theEnforcer.DefaultMod.makeCardPath;

public class ExplodingShield extends AbstractDynamicCard {
    // TEXT DECLARATION 

    public static final String ID = DefaultMod.makeID(ExplodingShield.class.getSimpleName());
    public static final String IMG = makeCardPath("Power.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION 	

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheEnforcer.Enums.COLOR_GRAY;

    private static final int COST = 3;
    private static final int MAGIC = 1;

    // /STAT DECLARATION/


    public ExplodingShield() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(!upgraded) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
                    new ExplodingShieldPower(p, p, magicNumber), magicNumber));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
                    new UpgradedExplodingShieldPower(p,p, magicNumber), magicNumber));
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}