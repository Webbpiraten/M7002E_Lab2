package lab2;

import java.awt.Frame;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.*;
import java.util.Vector;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.*;

public class Main implements GLEventListener, MouseListener, KeyListener{


	int count = 0;
	private Point point = new Point();
	private GLU glu;
	Vector<Shapes> vec_shap = new Vector<Shapes>();
	float xcord = 0.0f;
	float ycord = 0.0f;
	int shape = 2;
	boolean addShapes = true;
	Shapes s = null;
	
	public static void main(String[] args) {
		System.out.println("Herrroooo!");
			
		GLProfile glp = GLProfile.getDefault();
	    GLCapabilities caps = new GLCapabilities(glp);
	    GLCanvas canvas = new GLCanvas(caps);

	    Frame frame = new Frame("Test");
	    frame.setSize(750, 750);
	    frame.add(canvas);
	    frame.setVisible(true);
	    
	    frame.addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	            System.exit(0);
	        }

	    });
	    canvas.addGLEventListener(new Main());
	    canvas.addMouseListener(new Main());
	    canvas.addKeyListener(new Main());
	    Animator animator = new Animator(canvas);
	    animator.start();
		
	}


	public void render(GLAutoDrawable drawable){
		GL2 gl = drawable.getGL().getGL2();
//		GLUT glut = new GLUT();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
	    Shapes s = new Shapes(xcord, ycord, shape);
	    s.SelectShape(drawable);
//	    Shapes s2 = new Shapes(1.0f, 1.0f, shape, drawable, glut);
//	    Shapes s3 = new Shapes(-1.0f, -1.0f, shape, drawable, glut);
	}
	
	public void init(GLAutoDrawable drawable) {
		drawable.getGL().setSwapInterval(1);
		GL2 gl = drawable.getGL().getGL2();
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
		
		float ambient[] 		= {1.0f, 1.0f, 1.0f, 1.0f};
		float diffuse[]			= {1.0f, 1.0f, 1.0f, 1.0f};
		float specular[]		= {1.0f, 1.0f, 1.0f, 1.0f};
		float position[] 		= {2.0f, 2.0f, 0.0f, 1.0f};
		float lmodel_ambient[] 	= {0.4f, 0.4f, 0.4f, 1.0f};
		float local_view[]		= {0.0f};
		
		gl.glEnable(GL.GL_DEPTH_TEST);
	    gl.glDepthFunc(GL.GL_LESS);
	    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambient, 0);
	    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuse, 0);
	    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, specular, 0);
	    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, position, 0);
	    gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, lmodel_ambient, 0);
	    gl.glLightModelfv(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, local_view, 0);
	    gl.glEnable(GL2.GL_LIGHTING);
	    gl.glEnable(GL2.GL_LIGHT0);
	    gl.glClearColor(0.0f, 0.1f, 0.1f, 0.0f);
 
	}
	
	public void display(GLAutoDrawable drawable) {
		update();
//		GL2 gl = drawable.getGL().getGL2();
//		select(gl);
//		for(Shapes shap : vec_shap){ // for-each
//			shap.SelectShape(drawable);
//		}
		render(drawable);
	}
	
	public void addShape(float x, float y){
		System.out.println("Hej");
		System.out.println("x: "+x+" y: "+y);
//		Shapes shap = new Shapes(xcord, ycord, shape);
		
		s = new Shapes(1.0f, 1.0f, shape);
		vec_shap.add(s);		
	}
	
	public void select(GL2 gl){
	    System.out.println("Test");
		
		int[] SelBuf = new int[512];
		IntBuffer SelBuffer = Buffers.newDirectIntBuffer(512);
		int hits;
		int viewport[] = new int[4];

		gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0);
	    gl.glSelectBuffer(512, SelBuffer);
	    gl.glRenderMode(GL2.GL_SELECT);

	    gl.glInitNames();
	    gl.glPushName(-1);

	    gl.glMatrixMode(GL2.GL_PROJECTION);
	    gl.glPushMatrix();
	    gl.glLoadIdentity();
	    
	    glu.gluPickMatrix((double) point.x, (double) (viewport[3] - point.y), 5.0, 5.0, viewport, 0);
	    //gl.glOrtho(0.0, 8.0, 0.0, 8.0, -0.5, 2.5);
	        
	    gl.glPopMatrix();
	    gl.glFlush();
	    hits = gl.glRenderMode(GL2.GL_RENDER);
	    
	    SelBuffer.get(SelBuf);
	    draw_sel(hits, SelBuf);

	}
	
	public void draw_sel(int hits, int buffer[]){
		
		int x,y = 0;
		System.out.println(hits);
		
		for (int i = 0; i < hits; i++)
	    {
	      x = buffer[y];
	      System.out.println(" number of names for hit = " + x);
	      y++;
	      System.out.println("  z1 is " + buffer[y]);
	      y++;
	      System.out.println(" z2 is " + buffer[y]);
	      y++;
	      System.out.print("\n   the name is ");
	    }
		
	}

	public void reshape(GLAutoDrawable drawable, int arg1, int arg2, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();;
		GLU glu = new GLU();
		if (height <= 0){ 
				height = 1;
			}
		final float h = (float) width / (float) height;
		gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(60, h, 0.1, 1000.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        glu.gluLookAt(0.0f, 0.0f, 5.0f, 
        			  0.0f, 0.0f, 0.0f, 
        			  0.0f, 1.0f, 0.0f);
		
	}
	
	public void keyPressed(KeyEvent k) {
		/*
		 * M = move
		 * D = delete
		 * A = delete all
		 * R = resize 
		 * Esc = exit
		 * 1 = sphere
		 * 2 = Teapot
		 * 3 = Cube
		 */
		
		int id = k.getKeyCode();
				
		if(id == KeyEvent.VK_M){
			System.out.println("Move");
		}
		if(id == KeyEvent.VK_D){
			System.out.println("Delete");
		}
		if(id == KeyEvent.VK_A){
			System.out.println("Delete All");
		}
		if(id == KeyEvent.VK_R){
			System.out.println("Resize");
		}
		if(id == KeyEvent.VK_ESCAPE){
			System.out.println("Exit");
			System.exit(0);
		}
		if(id == KeyEvent.VK_1){
			shape = 1;
			System.out.println("Teapot");
		}
		if(id == KeyEvent.VK_2){
			shape = 2;
			System.out.println("Sphere");
		}
		if(id == KeyEvent.VK_3){
			shape = 3;
			System.out.println("Cube");
		}
		
	}
	
	public void mousePressed(MouseEvent mouse) {
		xcord = mouse.getX();
		ycord = mouse.getY();
		System.out.println(mouse.getPoint());
//		System.out.println("Xcord is: " + xcord);
//		System.out.println("Ycord is: " + ycord);
	}

	public void mouseReleased(MouseEvent mouse) {
		float x = mouse.getX();
		float y = mouse.getY();
		if(addShapes){
			addShape(x, y);
			//addShapes = false;
		}
		
	}
	private void update() {}
	public void dispose(GLAutoDrawable drawable) {}
	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg0) {}

}


