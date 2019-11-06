package gov.ismonnet.client.renderer.swing;

import gov.ismonnet.client.renderer.Renderer;
import gov.ismonnet.client.table.Table;

import javax.inject.Inject;
import javax.inject.Named;
import java.awt.image.BufferedImage;

class TableRenderer implements Renderer<SwingRenderContext, Table> {

    // Hardcoded texture values

    private static final float GOAL_EXTERNAL_CORNER_RATIO = 13F / 13F;
    private static final float GOAL_INTERNAL_CORNER_RATIO = 13F / 5F;

    private static final float SIDE_CORNER_RATIO = 8F / 12F;

    // Textures

    private final BufferedImage tableTexture;

    private final BufferedImage sideWallTexture;
    private final BufferedImage sideCornerTexture;

    private final BufferedImage goalExternalCornerTexture;
    private final BufferedImage goalWallTexture;
    private final BufferedImage goalInternalCornerTexture;
    private final BufferedImage goalTexture;

    @Inject TableRenderer(@Named("table_texture") BufferedImage tableTexture,
                          // Side walls
                          @Named("side_wall_texture") BufferedImage sideWallTexture,
                          @Named("side_corner_texture") BufferedImage sideCornerTexture,
                          // Goal Wall
                          @Named("goal_external_corner_texture") BufferedImage goalExternalCornerTexture,
                          @Named("goal_wall_texture") BufferedImage goalWallTexture,
                          @Named("goal_internal_corner_texture") BufferedImage goalInternalCornerTexture,
                          @Named("goal_goal_texture") BufferedImage goalTexture) {
        this.tableTexture = tableTexture;

        this.sideWallTexture = sideWallTexture;
        this.sideCornerTexture = sideCornerTexture;

        this.goalExternalCornerTexture = goalExternalCornerTexture;
        this.goalWallTexture = goalWallTexture;
        this.goalInternalCornerTexture = goalInternalCornerTexture;
        this.goalTexture = goalTexture;
    }

    @Override
    public void render(SwingRenderContext ctx, Table toRender) {
        ctx.drawImage(tableTexture,
                toRender.getWallThickness(),
                toRender.getWallThickness(),
                toRender.getWidth() - toRender.getWallThickness(),
                toRender.getHeight() - toRender.getWallThickness() * 2,
                null);

        renderSideWalls(ctx, toRender);
        renderGoalWall(ctx, toRender);
    }

    private void renderSideWalls(SwingRenderContext ctx, Table toRender) {
        final float cornerWidth = toRender.getWallThickness() * SIDE_CORNER_RATIO;
        final float wallWidth = toRender.getWidth() - cornerWidth - toRender.getWallThickness();
        // Top wall
        ctx.drawImage(sideWallTexture,
                toRender.getWallThickness(),
                0,
                wallWidth,
                toRender.getWallThickness(), null);
        // Top corner
        ctx.drawImage(sideCornerTexture,
                toRender.getWallThickness() + wallWidth,
                0,
                cornerWidth,
                toRender.getWallThickness(), null);
        // Bottom wall
        ctx.drawImage(sideWallTexture,
                toRender.getWallThickness(),
                toRender.getHeight() - toRender.getWallThickness(),
                wallWidth,
                toRender.getWallThickness(), null);
        // Bottom corner
        ctx.drawImage(sideCornerTexture,
                toRender.getWallThickness() + wallWidth,
                toRender.getHeight() - toRender.getWallThickness(),
                cornerWidth,
                toRender.getWallThickness(), null);
    }

    private void renderGoalWall(SwingRenderContext ctx, Table toRender) {

        final float externalCornerHeight = toRender.getWallThickness() / GOAL_EXTERNAL_CORNER_RATIO;
        final float internalCornerHeight = toRender.getWallThickness() / GOAL_INTERNAL_CORNER_RATIO;
        final float goalHeight = toRender.getGoalHeight();
        final float wallHeight = (toRender.getHeight() - 2 * externalCornerHeight - 2 * internalCornerHeight - goalHeight) / 2F;

        float startY = 0;
        // External Corner
        ctx.drawImage(goalExternalCornerTexture, 0, startY, toRender.getWallThickness(), externalCornerHeight, null);
        startY += externalCornerHeight;
        // Wall
        ctx.drawImage(goalWallTexture, 0, startY, toRender.getWallThickness(), wallHeight, null);
        startY += wallHeight;
        // Internal Corner
        ctx.drawImage(goalInternalCornerTexture, 0, startY, toRender.getWallThickness(), internalCornerHeight, null);
        startY += internalCornerHeight;
        // Goal
        ctx.drawImage(goalTexture, 0, startY, toRender.getWallThickness(), goalHeight, null);
        startY += goalHeight;
        // Internal Corner
        ctx.drawImage(goalInternalCornerTexture, 0, startY + internalCornerHeight, toRender.getWallThickness(), -internalCornerHeight, null);
        startY += internalCornerHeight;
        // Wall
        ctx.drawImage(goalWallTexture, 0, startY, toRender.getWallThickness(), wallHeight, null);
        startY += wallHeight;
        // External Corner
        ctx.drawImage(goalExternalCornerTexture, 0, startY + externalCornerHeight, toRender.getWallThickness(), -externalCornerHeight, null);
    }
}
