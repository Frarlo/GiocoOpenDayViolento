package gov.ismonnet.game.physics.table;

import javax.inject.Inject;

class TableImpl implements Table {

    private final float wallThickness;
    private final float goalHeight;

    private float width;
    private float height;

    @Inject TableImpl() {
        this.wallThickness = 25;
        // Image ratio is 243/282
        this.width = 729 + wallThickness;
        this.height = 846 + 2 * wallThickness;
        // Goal/height is 128/307
        this.goalHeight = 384;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public float getWallThickness() {
        return wallThickness;
    }

    @Override
    public float getGoalHeight() {
        return goalHeight;
    }
}
