package lab2;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import com.jogamp.opengl.util.gl2.GLUT;

public class Shapes {

    float no_mat[] = 			{ 0.0f, 0.0f, 0.0f, 1.0f };
    float mat_ambient[] = 		{ 0.7f, 0.7f, 0.7f, 1.0f };
    float mat_ambient_color[] = { 0.8f, 0.8f, 0.2f, 1.0f };
    float mat_diffuse[] = 		{ 0.1f, 0.5f, 0.8f, 1.0f };
    float mat_specular[] = 		{ 1.0f, 1.0f, 1.0f, 1.0f };
    float no_shininess[] = 		{ 0.0f };
    float low_shininess[] = 	{ 5.0f };
    float medium_shininess[] = 	{ 25.0f };
    float high_shininess[] = 	{ 100.0f };
    float mat_emission[] =  	{ 0.3f, 0.2f, 0.2f, 0.0f };
	
	float xcord,ycord;
	int id;
	int shape;
	GLUT glut = new GLUT();
	
    Shapes(float x, float y, int shapes, int id){
		this.xcord = x;
		this.ycord = y;
		shape = shapes;
		this.id = id;
//		System.out.println("Inuti shapes: x: "+x+" y: "+y+" shape: "+shape);
	}
    
    public void SelectShape(GLAutoDrawable drawable){
    	int toDraw = shape;
//    	System.out.println("SelectShape");
        switch (toDraw) {
            case 1:  Draw_Teapot(drawable, glut);
                     break;
            case 2:  Draw_Sphere(drawable, glut);
                     break;
            case 3:  Draw_Cube(drawable, glut);
                     break;
            default: ;//System.out.println("Shape doesnt exist!");
                     break;
        }
    }

    public void Draw_Teapot(GLAutoDrawable drawable, GLUT glut){
    	// Teapot
    	final GL2 gl = drawable.getGL().getGL2();

    	gl.glPushMatrix();
    	gl.glRotatef(15.0f, 1.0f, 1.0f, 0.0f);
    	gl.glTranslatef(xcord, ycord, 0.0f);
//    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, no_mat, 0);
//    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, mat_diffuse, 0);
//    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, mat_specular, 0);
//    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, high_shininess, 0);
//    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, mat_emission, 0);
    	glut.glutSolidTeapot(0.1f);
    	gl.glPopMatrix();
    	gl.glPushName(id);
    }

    public void Draw_Sphere(GLAutoDrawable drawable, GLUT glut){
    	// Sphere
    	final GL2 gl = drawable.getGL().getGL2();

    	gl.glPushMatrix();
    	gl.glTranslatef(xcord, ycord, 0.0f);
//    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, no_mat, 0);
//    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, mat_diffuse, 0);
//    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, mat_specular, 0);
//    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, low_shininess, 0);
//    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, no_mat, 0);
    	glut.glutSolidSphere(0.1f, 200, 200);
    	gl.glPopMatrix();
    	gl.glPushName(id);
    }

    public void Draw_Cube(GLAutoDrawable drawable, GLUT glut){
    	// Cube
    	final GL2 gl = drawable.getGL().getGL2();
    	
    	gl.glPushMatrix();
    	gl.glRotatef(15.0f, 1.0f, 1.0f, 0.0f);
    	gl.glTranslatef(xcord, ycord, 0.0f);
    	glut.glutSolidCube(0.1f);
    	gl.glPopMatrix();
    	gl.glPushName(id);
    }
}

