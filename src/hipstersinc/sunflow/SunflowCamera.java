package hipstersinc.sunflow;

import hipstersinc.sunflow.util.SceneDumpable;
import hipstersinc.sunflow.util.SceneDumper;

import org.sunflow.SunflowAPI;
import org.sunflow.math.Matrix4;
import org.sunflow.math.Point3;
import org.sunflow.math.Vector3;

public class SunflowCamera implements SceneDumpable {
	/** Default Sunflow API */
	private SunflowAPI sunflow;
	
	//////////////////////////////////////////////////////////////////
	// UID
	private static int totalCameras = 0;
	private int camNum;
	private String name;
	private String type;
	
	//////////////////////////////////////////////////////////////////
	// Types of Camera Lenses
	public static final String PINHOLE = "pinhole";
	public static final String THINLENS = "thinlens";
	public static final String SPHERICAL = "spherical";
	public static final String FISHEYE = "fisheye";
	
	//////////////////////////////////////////////////////////////////
	// Focal Parameter Constants
	private static final String FIELD_OF_VIEW = "fov";
	private static final String ASPECT_RATIO = "aspect";
	private static final String FOCUS_DISTANCE = "focus.distance";
	private static final String LENS_RADIUS = "lens.radius";
	
	//////////////////////////////////////////////////////////////////
	// Position Constants
	private static final String TRANSFORM = "transform";
	
	//////////////////////////////////////////////////////////////////
	// Focal Parameters
	private Float fieldOfView;
	private Float aspectRatio;
	private Float focalDistance;
	private Float lensRadius;
	
	//////////////////////////////////////////////////////////////////
	// Position Parameters
	private Point3 eye;
	private Point3 target;
	private Vector3 up;


	
	/**
	 * Default Constructor
	 * @param type pinhole, thinlens, spherical, or fisheye
	 * @param sunflow SunflowAPI
	 */
	public SunflowCamera(String type, SunflowAPI sunflow) {
		this.type = type;
		this.sunflow = sunflow;
		this.camNum = SunflowCamera.totalCameras++;
		this.name = String.format("camera%d", camNum);
	}
	
	/**
	 * Sets this camera in the API
	 */
	public void set() {
		if(fieldOfView != null) {
			sunflow.parameter(FIELD_OF_VIEW, fieldOfView);
		}
		
		if(aspectRatio != null) {
			sunflow.parameter(ASPECT_RATIO, aspectRatio);
		}
		
		if(focalDistance != null) {
			sunflow.parameter(FOCUS_DISTANCE, focalDistance);
		}
		
		if(lensRadius != null) {
			sunflow.parameter(LENS_RADIUS, lensRadius);
		}

		// Perform positioning
		sunflow.parameter(TRANSFORM, Matrix4.lookAt(eye, target, up));
		
		// Set as camera
		sunflow.camera(name, type);
		sunflow.parameter("camera", name);
		sunflow.options(SunflowAPI.DEFAULT_OPTIONS);
	}
	

	/**
	 * @return the eye
	 */
	public Point3 getEye() {
		return eye;
	}

	/**
	 * @param eye the eye to set
	 */
	public void setEye(Point3 eye) {
		this.eye = eye;
	}
	
	/**
	 * Sets the eye in x, y, z form
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setEye(float x, float y, float z) {
		this.eye = new Point3(x, y, z);
	}

	/**
	 * @return the target
	 */
	public Point3 getTarget() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(Point3 target) {
		this.target = target;
	}
	
	/**
	 * Sets the target in x, y, z form
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setTarget(float x, float y, float z) {
		this.target = new Point3(x, y, z);
	}

	/**
	 * @return the up
	 */
	public Vector3 getUp() {
		return up;
	}

	/**
	 * @param up the up to set
	 */
	public void setUp(Vector3 up) {
		this.up = up;
	}

	/**
	 * Sets the Up in x, y, z form
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setUp(float x, float y, float z) {
		this.up = new Vector3(x, y, z);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the fieldOfView
	 */
	public float getFieldOfView() {
		return fieldOfView;
	}

	/**
	 * @param fieldOfView the fieldOfView to set
	 */
	public void setFieldOfView(float fieldOfView) {
		this.fieldOfView = fieldOfView / ((float)Math.PI / 180f);
	}

	/**
	 * @return the aspectRatio
	 */
	public float getAspectRatio() {
		return aspectRatio;
	}

	/**
	 * @param aspectRatio the aspectRatio to set
	 */
	public void setAspectRatio(float aspectRatio) {
		this.aspectRatio = aspectRatio;
	}

	/**
	 * @return the focalDistance
	 */
	public float getFocalDistance() {
		return focalDistance;
	}

	/**
	 * @param focalDistance the focalDistance to set
	 */
	public void setFocalDistance(float focalDistance) {
		this.focalDistance = focalDistance;
	}

	/**
	 * @return the lensRadius
	 */
	public float getLensRadius() {
		return lensRadius;
	}

	/**
	 * @param lensRadius the lensRadius to set
	 */
	public void setLensRadius(float lensRadius) {
		this.lensRadius = lensRadius;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @param aspectRatio the aspectRatio to set
	 */
	public void setAspectRatio(Float aspectRatio) {
		this.aspectRatio = aspectRatio;
	}

	/**
	 * @param fieldOfView the fieldOfView to set
	 */
	public void setFieldOfView(Float fieldOfView) {
		this.fieldOfView = fieldOfView;
	}

	/**
	 * @param focalDistance the focalDistance to set
	 */
	public void setFocalDistance(Float focalDistance) {
		this.focalDistance = focalDistance;
	}

	/**
	 * @param lensRadius the lensRadius to set
	 */
	public void setLensRadius(Float lensRadius) {
		this.lensRadius = lensRadius;
	}
	
	/**
	 * Dumps the camera params to Sunflow's Scene format
	 * 
	 * camera {
  type pinhole
  eye    0 -205 50
  target 0 0 50
  up     0 0 1
  fov    45
  aspect 1.333333

	 */
	public StringBuffer toScene() {
		StringBuffer buf = new StringBuffer();
		SceneDumper.comment("Camera Parameters", buf);
		SceneDumper.startBlock("camera", buf);
		SceneDumper.param("type", type, buf);
		SceneDumper.param("eye", eye, buf);
		SceneDumper.param("target", target, buf);
		SceneDumper.param("up", up, buf);
		SceneDumper.param("fov", fieldOfView, buf);
		SceneDumper.param("aspect", aspectRatio, buf);
		SceneDumper.param("fdist", focalDistance, buf);
		SceneDumper.param("lensr", lensRadius, buf);
		SceneDumper.endBlock();
		return buf;
	}
}
