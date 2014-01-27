package lab2;

import java.awt.Button;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.*;

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
	
	public static void main(String[] args) {
		System.out.println("Herrroooo!");
			
		GLProfile glp = GLProfile.getDefault();
	    GLCapabilities caps = new GLCapabilities(glp);
	    GLCanvas canvas = new GLCanvas(caps);

	    Frame frame = new Frame("Test");
	    frame.setSize(500, 500);
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
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		Shapes s = new Shapes(gl);
		
	}
	
	public void init(GLAutoDrawable drawable) {
		drawable.getGL().setSwapInterval(1);
	}
	
	public void display(GLAutoDrawable drawable) {
		update();
		render(drawable);
	}
	
	public void select(GL2 gl){
		int[] SelBuf = new int[512];
		IntBuffer SelBuffer = Buffers.newDirectIntBuffer(512);
		int hits;
		int viewport[] = new int[4];

	    gl.glSelectBuffer(512, SelBuffer);
	    gl.glRenderMode(GL2.GL_SELECT);

	    gl.glInitNames();
	    gl.glPushName(-1);

	    gl.glMatrixMode(GL2.GL_PROJECTION);
	    gl.glPushMatrix();
	    gl.glLoadIdentity();
	    
	    glu.gluPickMatrix((double) point.x, (double) (viewport[3] - point.y), 5.0, 5.0, viewport, 0);
	    gl.glOrtho(0.0, 8.0, 0.0, 8.0, -0.5, 2.5);
	        
	    gl.glPopMatrix();
	    gl.glFlush();
	    hits = gl.glRenderMode(GL2.GL_RENDER);
	    
	    SelBuffer.get(SelBuf);
	    draw_sel(hits, SelBuf);
		
		
	}
	
	public void draw_sel(int hits, int buffer[]){
		
//		int x,y = 0;
//		System.out.println(hits);
//		
//		for (int i = 0; i < hits; i++)
//	    {
//	      x = buffer[y];
//	      System.out.println(" number of names for hit = " + x);
//	      y++;
//	      System.out.println("  z1 is " + buffer[y]);
//	      y++;
//	      System.out.println(" z2 is " + buffer[y]);
//	      y++;
//	      System.out.print("\n   the name is ");
//	    }
		
	}
	


	public void reshape(GLAutoDrawable drawable, int arg1, int arg2, int arg3, int arg4) {

	}
	
	private void update() {
		
	}
	
	public void dispose(GLAutoDrawable drawable) {

	}


	public void mouseClicked(MouseEvent arg0) {
		
	}


	public void mouseEntered(MouseEvent arg0) {
		
	}


	public void mouseExited(MouseEvent arg0) {
		
	}


	public void mousePressed(MouseEvent mouse) {
		//System.out.println("Clicked!");
		point = mouse.getPoint();
		System.out.println(mouse.getPoint());
	}


	public void mouseReleased(MouseEvent arg0) {
		//System.out.println("Released!");
	}

	private void Commands(KeyEvent k) {
			
		/*
		 * M = move
		 * D = delete
		 * A = delete all
		 * R = resize 
		 * Esc = exit
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
	}

	public void keyPressed(KeyEvent k) {
		Commands(k);
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {

		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {

		
	}

}
