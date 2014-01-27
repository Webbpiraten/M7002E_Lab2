package lab2;

import javax.media.opengl.GL2;

public class Shapes {

	
	public Shapes(GL2 gl){
		Draw_Shape(gl);
	}
	
	public void Draw_Shape(GL2 gl){
		
		
		gl.glBegin(GL2.GL_QUADS);
		gl.glPushAttrib(GL2.GL_CURRENT_BIT);
			gl.glColor3f(0, 0, 1);
		gl.glPopAttrib();
	
		gl.glPushMatrix();
			gl.glVertex2f(-0.95f, 0.95f);
			gl.glVertex2f(-0.85f, 0.95f);
			gl.glVertex2f(-0.85f, 0.85f);
			gl.glVertex2f(-0.95f, 0.85f);
		gl.glPopMatrix();
	
	gl.glEnd();
		
	}
}
