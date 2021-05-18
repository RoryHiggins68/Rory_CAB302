package Asset_Trading;

/**
 * A generic asset class that can be subclasses for what ever assets are needed
 */

public abstract class Assets {

    protected String assetName;

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }
}
