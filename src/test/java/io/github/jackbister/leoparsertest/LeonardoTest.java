package io.github.jackbister.leoparsertest;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import io.github.jackbister.leoparser.Leonardo;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.geom.Point2D;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(JUnitQuickcheck.class)
public class LeonardoTest {
    private Graphics graphics;
    private Leonardo leo;

    @Before
    @BeforeEach
    public void setup() {
        graphics = Mockito.mock(Graphics.class);
        leo = new Leonardo(graphics, 1024, 1024);
    }

    @Property
    public void setColor(@InRange(minInt = 0x000000, maxInt = 0xFFFFFF) int color) {
        String colorString = "0x" + Integer.toString(color, 16);
        leo.setColor(colorString);
        assertEquals(color, leo.getColor().getRGB() & 0xFFFFFF);
        assertEquals(0xFF, leo.getColor().getAlpha());
        Mockito.verify(graphics).setColor(Color.decode(colorString));
    }

    @Test
    public void up() {
        leo.up();
        assertFalse(leo.isDown());
    }

    @Test
    public void down() {
        leo.down();
        assertTrue(leo.isDown());
    }

    @Property
    public void left(int theta) {
        int facingBefore = leo.getFacing();
        leo.left(theta);
        assertEquals(facingBefore + theta, leo.getFacing());
    }

    @Property
    public void right(int theta) {
        int facingBefore = leo.getFacing();
        leo.right(theta);
        assertEquals(facingBefore - theta, leo.getFacing());
    }

    @Property
    public void forw_down(int d) {
        Point2D posBefore = leo.getPosition();
        Point2D expectedPosAfter = new Point2D.Double(
                posBefore.getX() + d * Math.cos(Math.PI * leo.getFacing() / 180),
                posBefore.getY() + d * Math.sin(Math.PI * leo.getFacing() / 180)
        );

        leo.down();
        leo.forw(d);
        Point2D posAfter = leo.getPosition();

        assertEquals(expectedPosAfter.getX(), posAfter.getX());
        assertEquals(expectedPosAfter.getY(), posAfter.getY());

        Mockito.verify(graphics).drawLine((int) posBefore.getX(), (int) posBefore.getY(), (int) posAfter.getX(), (int) posAfter.getY());
    }

    @Property
    public void forw_up(int d) {
        Point2D posBefore = leo.getPosition();

        Point2D expectedPosAfter = new Point2D.Double(
                posBefore.getX() + d * Math.cos(Math.PI * leo.getFacing() / 180),
                posBefore.getY() + d * Math.sin(Math.PI * leo.getFacing() / 180)
        );

        leo.up();
        leo.forw(d);
        Point2D posAfter = leo.getPosition();

        assertEquals(expectedPosAfter.getX(), posAfter.getX());
        assertEquals(expectedPosAfter.getY(), posAfter.getY());

        Mockito.verifyZeroInteractions(graphics);
    }

    @Property
    public void back_down(int d) {
       Point2D posBefore = leo.getPosition();

        Point2D expectedPosAfter = new Point2D.Double(
                posBefore.getX() + d * Math.cos(Math.PI * (leo.getFacing() + 180) / 180),
                posBefore.getY() + d * Math.sin(Math.PI * (leo.getFacing() + 180) / 180)
        );

        leo.down();
        leo.back(d);
        Point2D posAfter = leo.getPosition();

        assertEquals(expectedPosAfter.getX(), posAfter.getX());
        assertEquals(expectedPosAfter.getY(), posAfter.getY());

        Mockito.verify(graphics).drawLine((int) posBefore.getX(), (int) posBefore.getY(), (int) posAfter.getX(), (int) posAfter.getY());
    }

    @Property
    public void back_up(int d) {
        Point2D posBefore = leo.getPosition();

        Point2D expectedPosAfter = new Point2D.Double(
                posBefore.getX() + d * Math.cos(Math.PI * (leo.getFacing() + 180) / 180),
                posBefore.getY() + d * Math.sin(Math.PI * (leo.getFacing() + 180) / 180)
        );

        leo.up();
        leo.back(d);
        Point2D posAfter = leo.getPosition();

        assertEquals(expectedPosAfter.getX(), posAfter.getX());
        assertEquals(expectedPosAfter.getY(), posAfter.getY());

        Mockito.verifyZeroInteractions(graphics);
    }
}