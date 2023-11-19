package game.entities.match;

import java.math.BigDecimal;

public class Match {
    private final String id;
    private final BigDecimal aSideReturnRate;
    private final BigDecimal bSideReturnRate;
    private final String result;

    public Match(String id, String aSideReturnRate, String bSideReturnRate, String result) {
        this.id = id;
        this.aSideReturnRate = new BigDecimal(aSideReturnRate );
        this.bSideReturnRate = new BigDecimal(bSideReturnRate);
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public BigDecimal getASideReturnRate() {
        return aSideReturnRate;
    }


    public BigDecimal getBSideReturnRate() {
        return bSideReturnRate;
    }

    public String getResult() {
        return result;
    }

}
