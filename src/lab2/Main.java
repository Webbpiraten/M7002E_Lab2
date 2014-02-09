package lab2;

import java.awt.Frame;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.*;
import javax.swing.JOptionPane;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.*;

public class Main {

	public static void main(String[] args) {

		new Main();
	}
	
	public Main(){
		System.out.println("Herrroooo!");
		System.out.println("Welcome to this fenomenal and user-friendly interface!");
		System.out.println("For further help, please consult the README.txt");
		
	    Frame frame = new Frame();
		GLProfile glp = GLProfile.getDefault();
	    GLCapabilities caps = new GLCapabilities(glp);
	    GLCanvas canvas = new GLCanvas(caps);

	    final RenderShapes rs = new RenderShapes();
	    canvas.addGLEventListener(rs);
	    canvas.addMouseListener(rs);
	    canvas.addKeyListener(rs);
	    canvas.addMouseMotionListener(rs);
	    
	    frame.add(canvas);
	    frame.setSize(750, 750);
	    frame.setResizable(false); // !!!!!!!!!
	    
	    final Animator animator = new Animator(canvas);
	    
	    frame.addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	        	animator.stop();
	            System.exit(0);
	        }
	    });

	    frame.setVisible(true);
	    animator.start();
	}
	
static class RenderShapes implements GLEventListener, MouseListener, KeyListener, MouseMotionListener{
	
	private Point point = new Point();
	private GLU glu = new GLU();
	public ArrayList<Shapes> arr_shap = new ArrayList<Shapes>();
	float xcord, ycord, radie;
	boolean add, sel, move, resize = false;
	Shapes s = null;
	int current, shape;
	int count = 0;
	int id = 0;
	int light = 0;
	int mode = 0;	// 0 = draw, 1 = select, 2 = add, 3 = move, 4 = resize, 5 = light
	
//	Light material
	float ambient[]; 		//= {1.0f, 1.0f, 1.0f, 1.0f};
	float diffuse[];		//= {1.0f, 1.0f, 1.0f, 1.0f};
	float specular[];		//= {1.0f, 1.0f, 1.0f, 1.0f};
	float position[] 		= {2.0f, 2.0f, 0.0f, 1.0f};
	float lmodel_ambient[] 	= {0.4f, 0.4f, 0.4f, 1.0f};
	float local_view[]		= {0.0f};
	
//	Shape material
	float s_amb[];
	float s_diff[];
	float s_spec[];
	float s_shiny[];
	float s_emis[];
	

	public void init(GLAutoDrawable drawable) {
		drawable.getGL().setSwapInterval(1);
		GL2 gl = drawable.getGL().getGL2();
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
	    gl.glDepthFunc(GL.GL_LESS);
	    gl.glEnable(GL2.GL_LIGHTING);
	    gl.glEnable(GL2.GL_LIGHT0);
		gl.glEnable(GL.GL_DEPTH_TEST);
//	    gl.glEnable(GL2.GL_CULL_FACE);
//	    gl.glEnable(GL2.GL_NORMALIZE);
	    gl.glClearColor(0.0f, 0.1f, 0.1f, 0.0f);
 
	}
	
	public void reshape(GLAutoDrawable drawable, int arg1, int arg2, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();
		if (height <= 0){ 
				height = 1;
			}
//		final float h = (float) width / (float) height; // For perspective
		
//		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
//        glu.gluPerspective(60, h, 0.1, 1000.0); // Fix Later
        glu.gluOrtho2D(10.0f,10.0f,10f,10.0f);

//        gl.glMatrixMode(GL2.GL_MODELVIEW); // Fix Later
//        gl.glLoadIdentity();
//        glu.gluLookAt(0.0f, 0.0f, 5.0f, 
//        			  0.0f, 0.0f, 0.0f, 
//        			  0.0f, 1.0f, 0.0f);
        
//        gl.glMatrixMode(GL2.GL_PROJECTION);
//        gl.glLoadIdentity();
	}

	public void render(GLAutoDrawable drawable){
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		if(arr_shap.size() != 0){
			for(Shapes obj : arr_shap){
				obj.SelectShape(drawable);
			}
		}
	}
	
	public void display(GLAutoDrawable drawable) {

		GL2 gl = drawable.getGL().getGL2();
//		float h = (float) 750 / (float) 750; // For perspective
		switch(mode){
		case 0: // 0 = draw
			render(drawable);
			break;
			
			
//		http://www.java-tips.org/other-api-tips/jogl/another-picking-example-in-jogl.html
		case 1: // 1 = select
			int buffsize = 512;
			double x = (double) point.x; 
			double y = (double) point.y;
			int[] viewPort = new int[4];
			int hits = 0;
			IntBuffer selectBuffer = Buffers.newDirectIntBuffer(buffsize);
			gl.glGetIntegerv(GL2.GL_VIEWPORT, viewPort, 0);
			gl.glSelectBuffer(buffsize, selectBuffer);
			gl.glRenderMode(GL2.GL_SELECT);
			gl.glInitNames();
			gl.glMatrixMode(GL2.GL_PROJECTION);
			gl.glPushMatrix();
			gl.glLoadIdentity();			
			glu.gluPickMatrix(x, (double) viewPort[3] - y, 5.0d, 5.0d, viewPort, 0);
//			glu.gluPerspective(60, h, 0.1, 1000.0); // fix later
	        glu.gluOrtho2D(10.0f,10.0f,10f,10.0f);
			render(drawable);
			gl.glMatrixMode(GL2.GL_PROJECTION);
			gl.glPopMatrix();
			gl.glFlush();
			hits = gl.glRenderMode(GL2.GL_RENDER);
			processHits(hits, selectBuffer);
			mode = 0;
			break;
			
			
//		http://www.java-tips.org/other-api-tips/jogl/how-to-use-gluunproject-in-jogl.html
		case 2: // 2 = add
			double x1 = point.getX(), y1 = point.getY();
			int viewport[] = new int[4];
			double wcoord[] = new double[4];
			double mvmatrix[] = new double[16];
			double projmatrix[] = new double[16];
			gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0);
	        gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, mvmatrix, 0);
	        gl.glGetDoublev(GL2.GL_PROJECTION_MATRIX, projmatrix, 0);
			double realy = viewport[3] - (double) y1 - 1;
//	        System.out.println("Coordinates at cursor are (" + x1 + ", " + realy +")");
	        glu.gluUnProject((double) x1, (double) realy, 0.0, mvmatrix, 0, projmatrix, 0, viewport, 0, wcoord, 0);
//	        System.out.println("World coords at z=0.0 are ( " + wcoord[0] + ", " + wcoord[1] + ", " + wcoord[2] + ")");
	        
	        
	        String input_add =  JOptionPane.showInputDialog("Radie, write on the form: x.xf: ");
	        
			String mat_amb =  JOptionPane.showInputDialog("Ambient, write on the form: x.xf,x.xf,x.xf,x.xf: ");
			List<String> m_amb = Arrays.asList(mat_amb.split(","));
			
			String mat_diff =  JOptionPane.showInputDialog("Diffuse, write on the form: x.xf,x.xf,x.xf,x.xf: ");
			List<String> m_diff = Arrays.asList(mat_diff.split(","));
			
			String mat_spec =  JOptionPane.showInputDialog("Specular, write on the form: x.xf,x.xf,x.xf,x.xf: ");
			List<String> m_spec = Arrays.asList(mat_spec.split(","));
			
			String shininess =  JOptionPane.showInputDialog("Shininess, write on the form: x.xf: ");
			List<String> shiny = Arrays.asList(shininess.split(","));
			
			String emission =  JOptionPane.showInputDialog("Emission, write on the form: x.xf,x.xf,x.xf,x.xf: ");
			List<String> emis = Arrays.asList(emission.split(","));
			
			
			float[] arr_amb = new float[m_amb.size()];
			float[] arr_diff = new float[m_diff.size()];
			float[] arr_spec = new float[m_spec.size()];
			float[] arr_shininess = new float[shiny.size()];
			float[] arr_emission = new float[emis.size()];
			for(int i = 0; i < m_amb.size(); i++){
				arr_amb[i] = Float.parseFloat(m_amb.get(i));
				arr_diff[i] = Float.parseFloat(m_diff.get(i));
				arr_spec[i] = Float.parseFloat(m_spec.get(i));
				arr_emission[i] = Float.parseFloat(emis.get(i));
			}
			arr_shininess[0] = Float.parseFloat(shiny.get(0));
			
			if(mat_amb != null && mat_diff != null && mat_spec != null && shiny != null && emis != null){
				s_amb = arr_amb;
				s_diff = arr_diff;
				s_spec = arr_spec;
				s_shiny = arr_shininess;
				s_emis = arr_emission;
			}
	        
	        
			if(input_add != null){
				radie = Float.parseFloat(input_add);
				addShape((float) wcoord[0], (float) wcoord[1], s_amb, s_diff, s_spec, s_shiny, s_emis);
			}
			mode = 0;
			break;
			
		case 3: // 3 = move
			int viewport_move[] = new int[4];
			double wcoord_move[] = new double[4];
			double mvmatrix_move[] = new double[16];
			double projmatrix_move[] = new double[16];
			gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport_move, 0);
	        gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, mvmatrix_move, 0);
	        gl.glGetDoublev(GL2.GL_PROJECTION_MATRIX, projmatrix_move, 0);
			double realy2 = viewport_move[3] - (double) ycord - 1;
	        glu.gluUnProject((double) xcord, (double) realy2, 0.0, mvmatrix_move, 0, projmatrix_move, 0, viewport_move, 0, wcoord_move, 0);
	        moveobj((float) wcoord_move[0], (float) wcoord_move[1]);
			break;
			
		case 4: // 4 = resize
			System.out.println("Resize");
			String input =  JOptionPane.showInputDialog("New radie, write on the form: x.xf: ");
			if(input != null){
				radie = Float.parseFloat(input);
				resizeobj();
			}else{
				System.out.println("Error");
			}
			resize = false;
			mode = 0;
			break;
			
		case 5: // 5 = light
			String input_amb =  JOptionPane.showInputDialog("Ambient, write on the form: x.xf,x.xf,x.xf,x.xf: ");
			List<String> amb = Arrays.asList(input_amb.split(","));
			String input_diff =  JOptionPane.showInputDialog("Diffuse, write on the form: x.xf,x.xf,x.xf,x.xf: ");
			List<String> diff = Arrays.asList(input_diff.split(","));
			String input_spec =  JOptionPane.showInputDialog("Specular, write on the form: x.xf,x.xf,x.xf,x.xf: ");
			List<String> spec = Arrays.asList(input_spec.split(","));
			float[] array_amb = new float[amb.size()];
			float[] array_diff = new float[diff.size()];
			float[] array_spec = new float[spec.size()];
			for(int i = 0; i < amb.size(); i++){
				array_amb[i] = Float.parseFloat(amb.get(i));
				array_diff[i] = Float.parseFloat(diff.get(i));
				array_spec[i] = Float.parseFloat(spec.get(i));
			}
			if(input_amb != null && input_diff != null && input_spec != null){
				ambient = array_amb;
				diffuse = array_diff;
				specular = array_spec;
			}
		    gl.glPushMatrix();
		    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambient, 0);
		    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuse, 0);
		    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, specular, 0);
		    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, position, 0);
		    gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, lmodel_ambient, 0);
		    gl.glLightModelfv(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, local_view, 0);
		    gl.glPopMatrix();
		    mode = 0;
			break;
		}
	}
	
    public void processHits(int hits, IntBuffer buffer)
    {
//    http://www.java-tips.org/other-api-tips/jogl/another-picking-example-in-jogl.html
      int offset = 0;
      int names;
      for (int i=0;i<hits;i++)
        {
          names = buffer.get(offset); 
          offset++;	// Derp...
          offset++;
          offset++;
          for (int j=0;j<names;j++){
              if (j==(names-1)){
          	  	current = buffer.get(offset);
          	  	System.out.println("Current object is: "+current);
              }
              offset++;
            }
        }
    }
	
    public void print(){
		if(arr_shap.size() != 0){
			for(Shapes obj : arr_shap){
				System.out.println(obj.id + ", " + obj.radie + ", " + obj.shape + ", " + obj.xcord + ", " + obj.ycord);
				System.out.println(obj.mat_amb[0] + "," + obj.mat_amb[1] + "," + obj.mat_amb[2] + "," + obj.mat_amb[3]);
				System.out.println(obj.mat_diff[0] + "," + obj.mat_diff[1] + "," + obj.mat_diff[2] + "," + obj.mat_diff[3]);
				System.out.println(obj.mat_spec[0] + "," + obj.mat_spec[1] + "," + obj.mat_spec[2] + "," + obj.mat_spec[3]);
				System.out.println(obj.shininess[0]);
				System.out.println(obj.mat_emission[0] + "," + obj.mat_emission[1] + "," + obj.mat_emission[2] + "," + obj.mat_emission[3]);
			}
		}else{
			System.out.println("These objects are not the shapes you're looking for...");
		}
    }
    
	public void addShape(float x, float y, float[] amb, float[] diff, float[] spec, float[] shininess, float[] emission){
		System.out.println(" x: "+x+" y: "+y+" shape: "+shape);
		Shapes s = new Shapes(x, y, shape, id, radie, amb, diff, spec, shininess, emission);
		id++;
		arr_shap.add(s);
	}
	
	public void moveobj(float x, float y){
		for(int i = 0; i < arr_shap.size(); i++){
			if(current == arr_shap.get(i).id){
				arr_shap.get(i).xcord = x;
				arr_shap.get(i).ycord = y;
			}
		}
	}
	
	public void resizeobj(){
		for(int i = 0; i < arr_shap.size(); i++){
			if(current == arr_shap.get(i).id){
				arr_shap.get(i).radie = radie;
			}
		}
	}

	public void DelAllObj(){
		id = 0;
		arr_shap.clear();
	}
	
	public void DelObj(){
		for(int i = 0; i < arr_shap.size(); i++){
			if(current == arr_shap.get(i).id){
				arr_shap.remove(i);	
			}
		}
	}

	public void keyPressed(KeyEvent k) {
		/*
		 * A = adding
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
			move = true;
			System.out.println("Move");
		}
		if(id == KeyEvent.VK_P){
			System.out.println("Printing");
			print();
		}
		if(id == KeyEvent.VK_D){
			DelObj();
		}
		if(id == KeyEvent.VK_DELETE){
			DelAllObj();
			System.out.println("Delete everything");
		}
		if(id == KeyEvent.VK_R && resize == false){
			resize = true;
			System.out.println("Resize");
		}
		if(id == KeyEvent.VK_ESCAPE){
			System.out.println("Exit");
			System.exit(0);
		}
		if(id == KeyEvent.VK_1 && add == true){
			shape = 1;
			System.out.println("Teapot");
		}
		if(id == KeyEvent.VK_2 && add == true){
			shape = 2;
			System.out.println("Sphere");
		}
		if(id == KeyEvent.VK_3 && add == true){
			shape = 3;
			System.out.println("Cube");
		}
		if(id == KeyEvent.VK_A && add == false){
			add = true;
			shape = 1;
			System.out.println("Adding");
		}
		if(id == KeyEvent.VK_S && sel == false){
			sel = true;
			System.out.println("Selecting");
		}
		if(id == KeyEvent.VK_L && light != 1){
			light = 1;
			mode = 5;
			System.out.println("Let there be light!");
		}
		
	}
	
	public void mousePressed(MouseEvent mouse) {
		point = mouse.getPoint();
		if(sel){
			mode = 1;
			sel = false;
		}
		if(add){
			mode = 2;
			add = false;
		}
		if(move){
			mode = 3;
		}
		if(resize){
			mode = 4;
		}
	}

	public void mouseReleased(MouseEvent mouse) {
		if(move){
			move = false;
			mode = 0;
		}
	}
	
	public void mouseDragged(MouseEvent mouse) {
		xcord = (float) mouse.getX();
		ycord = (float) mouse.getY();
	}
	
//	private void update() {}
	public void dispose(GLAutoDrawable drawable) {}
	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg0) {}
	public void mouseMoved(MouseEvent arg0) {}
	
	}
}

