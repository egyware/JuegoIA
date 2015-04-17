package com.egysoft.ia.juego;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.Cullable;

/**
 * Esta clase esta basada en Libfdx/Group, sin embargo en vez de usar un SnapshotArray utilza un grid. 
 * @author Edgardo
 */
public class Laberinth extends Actor implements Cullable
{
    public final int boxWidth;
    public final int boxHeight;
    public final int columns;
    public final int rows;
    public final Actor grid[];
    private Rectangle cullingArea;
    private final Affine2 worldTransform = new Affine2();
    private final Matrix4 computedTransform = new Matrix4();
    private final Matrix4 oldTransform = new Matrix4();
    private boolean transform = true;
        
    public Laberinth(final int c, final int r,final int bw, final int hw)
    {
        boxWidth = bw;
        boxHeight = hw;
        columns = c;
        rows = r;
        
        grid = new Actor[columns*rows];
    }
   
    @Override
    public void act(float delta)
    {
       for(final Actor actor:grid)
       {
           if(actor != null)actor.act(delta);
       }        
    }
    
    @Override
    public void draw(final Batch batch,final float parentAlfa)
    {
        if(cullingArea != null)
        {
            drawWithCulling(batch, parentAlfa);
        }
        else
        {
            drawWithoutCulling(batch,parentAlfa);
        }
    }
    
    private void drawWithCulling(final Batch batch, final float parentAlpha)
    {
        // Draw children only if inside culling area.
        float cullLeft = cullingArea.x;
        float cullRight = cullLeft + cullingArea.width;
        float cullBottom = cullingArea.y;
        float cullTop = cullBottom + cullingArea.height;
        if (transform) 
        {
                for(int j=0;j<rows;j++)
                {
                    for(int i=0;i<columns;i++)
                    {
                        final Actor child = grid[j*columns+i];                
                        if(child != null) 
                        {                            
                            if (!child.isVisible()) continue;
                            float cx = child.getX(), cy = child.getY();
                            float w = child.getWidth(), h = child.getHeight();
                            
                            if (cx <= cullRight && cy <= cullTop && cx + w >= cullLeft && cy + h >= cullBottom)
                               child.draw(batch, parentAlpha);
                        }
                    }
                }
        } 
        else 
        {
               // No transform for this group, offset each child.
               float offsetX = getX(), offsetY = getY();
               setPosition(0,0);
               for(int j=0;j<rows;j++)
               {
                    for(int i=0;i<columns;i++)
                    {
                        final Actor child = grid[j*columns+i];                
                        if(child != null) 
                        {
                            if (!child.isVisible()) continue;
                            float cx = child.getX(), cy = child.getY();
                            float w = child.getWidth(), h = child.getHeight();
                            if (cx <= cullRight && cy <= cullTop && cx + w >= cullLeft && cy + h >= cullBottom) 
                            {
                               child.setPosition(cx + offsetX, cy + offsetY);
                               child.draw(batch, parentAlpha);
                               child.setPosition(cx, cy);
                            }
                        }
                    }
               }
               setPosition(offsetX, offsetY);
       }
   }    
    
   private void drawWithoutCulling(final Batch batch, final float parentAlpha)
    {
        // No culling, draw all children.
        if (transform)
        {
            for(int j=0;j<rows;j++)
            {
                    for(int i=0;i<columns;i++)
                    {
                        final Actor child = grid[j*columns+i];                
                        if(child != null) 
                        {
                            if (!child.isVisible()) continue;
                            child.draw(batch, parentAlpha);
                        }
                    }                        
            }
        }
        else
        {
            // No transform for this group, offset each child.
            float offsetX = getX(), offsetY = getY();
            setPosition(0,0);
            for(int j=0;j<rows;j++)
            {
                 for(int i=0;i<columns;i++)
                 {
                     final Actor child = grid[j*columns+i];                
                     if(child != null) 
                     {
                         if (!child.isVisible()) continue;
                         float cx = child.getX(), cy = child.getY();
                         float w = child.getWidth(), h = child.getHeight();
                         child.setPosition(cx + offsetX, cy + offsetY);
                         child.draw(batch, parentAlpha);
                         child.setPosition(cx,cy);;
                     }
                 }
            }
            setPosition(offsetX, offsetY);
        }
    }
    
    public boolean isCellFree(int i, int j)
    {
        return grid[j*columns+i] == null;
    }

    @Override
    public void setCullingArea(Rectangle r) 
    {
        this.cullingArea = r;
    }
    
    public Rectangle getCullingArea () 
    {
	return cullingArea;
    }
    
    //copy/paste
   
    protected Matrix4 computeTransform ()
    {
        Affine2 worldTransform = this.worldTransform;

        float originX = this.originX;
        float originY = this.originY;
        float rotation = this.rotation;
        float scaleX = this.scaleX;
        float scaleY = this.scaleY;

        worldTransform.setToTrnRotScl(x + originX, y + originY, rotation, scaleX, scaleY);
        if (originX != 0 || originY != 0) worldTransform.translate(-originX, -originY);

      
        computedTransform.set(worldTransform);
        return computedTransform;
    }

    /** Set the batch's transformation matrix, often with the result of {@link #computeTransform()}. Note this causes the batch to
     * be flushed. {@link #resetTransform(Batch)} will restore the transform to what it was before this call. */
    protected void applyTransform (Batch batch, Matrix4 transform) {
            oldTransform.set(batch.getTransformMatrix());
            batch.setTransformMatrix(transform);
    }

    /** Restores the batch transform to what it was before {@link #applyTransform(Batch, Matrix4)}. Note this causes the batch to be
     * flushed. */
    protected void resetTransform (Batch batch) {
            batch.setTransformMatrix(oldTransform);
    }

    /** Set the shape renderer transformation matrix, often with the result of {@link #computeTransform()}. Note this causes the
     * shape renderer to be flushed. {@link #resetTransform(ShapeRenderer)} will restore the transform to what it was before this
     * call. */
    protected void applyTransform (ShapeRenderer shapes, Matrix4 transform)
    {
            oldTransform.set(shapes.getTransformMatrix());
            shapes.setTransformMatrix(transform);
    }

    /** Restores the shape renderer transform to what it was before {@link #applyTransform(Batch, Matrix4)}. Note this causes the
     * shape renderer to be flushed. */
    protected void resetTransform (ShapeRenderer shapes) 
    {
            shapes.setTransformMatrix(oldTransform);
    }
}
