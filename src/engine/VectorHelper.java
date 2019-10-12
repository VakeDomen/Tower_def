package engine;
import java.util.Vector;

import buildings.Wall;

public class VectorHelper {
	
	public static VectorHelper vectorHelper = new VectorHelper();
	
	
	/** 
	 * Plus: 
	 * Vector<Double> v1, v2
	 * return:
	 * Vector<Double> v3
	 * sum of v1 and v2
	 * **/
	
	public static Vector<Double> plus(Vector<Double> v1, Vector<Double> v2){
		Vector<Double> v3=new Vector<>(v1.size());
		for (int i = 0; i < v1.size(); i++) {
			v3.add(v1.get(i)+v2.get(i));
		}
		return v3;
	}
	public static Vector<Double> minusDouble(Vector<Double> v1, Vector<Double> v2){
		Vector<Double> v3=new Vector<>(v2.size());
		for (int i = 0; i < v1.size(); i++) {
			v3.add(v1.get(i)-v2.get(i));
		}
		return v3;
	}
	public static Vector<Integer> minus(Vector<Integer> v1, Vector<Integer> v2){
		Vector<Integer> v3=new Vector<>(v2.size());
		for (int i = 0; i < v1.size(); i++) {
			v3.add(v1.get(i)-v2.get(i));
		}
		return v3;
	}
	public static Vector<Double> multiplyVectorWithSkalar(Vector<Double> v, double s){
		Vector<Double> v1=new Vector<Double>(v.size());
		for (int i = 0; i < v.size(); i++) {
			v1.add(v.get(i)*s);
		}		
		return v1;
	}
	public static double vectorSizeDouble(Vector<Double> v){
		double size=0.;
		for (int i = 0; i < v.size(); i++) {
			size+=Math.pow(v.elementAt(i), 2);
		}
		return Math.sqrt(size);
	}
	public static int vectorSize(Vector<Integer> v){
		double size=0.;
		for (int i = 0; i < v.size(); i++) {
			size+=Math.pow(v.elementAt(i), 2);
		}
		return (int)Math.sqrt(size);
	}
	public static boolean randBool(double odds){
		if(Math.random()<odds){
			return true;
		}else{
			return false;
		}
	}
	public static double angleBetweenVectors(Vector<Double> v1, Vector<Double> v2){
		return Math.toDegrees(Math.acos(scalarProduct(v1, v2)/(vectorSizeDouble(v1)*vectorSizeDouble(v2))));
	}
	public static double angleBetweenVectors2(Vector<Double> v1, Vector<Double> v2){
		return Math.atan2( v1.get(0)*v2.get(1) - v1.get(1)*v2.get(0), v1.get(0)*v2.get(0) + v1.get(1)*v2.get(1));
	}
	public static double scalarProduct(Vector<Double> v1, Vector<Double> v2){
		double prod=0;
		for (int i = 0; i < v2.size(); i++) {
			prod+=(v1.get(i)*v2.get(i));
		}
		return prod;
	}
	public static Vector<Double> setToZero(Vector<Double> v) {
		Vector<Double> v1=new Vector<Double>(v.size());
		for (int i = 0; i < v.size(); i++) {
			v.add(0.0);
		}
		return v1;
	}
	public static Vector<Double> resizeVector(Vector<Double> v, double size){
		return multiplyVectorWithSkalar(v, size/vectorSizeDouble(v));
	}
	@SuppressWarnings("unchecked")
	public static Vector<Double>[] splitVector(Vector<Double> split, Vector<Double> on, double angle){
		System.out.println("Angle: "+Math.toDegrees(angle));
		Vector<Double>[] a=new Vector[split.size()];
		double[] sizes=new double[split.size()];
		sizes[0]=Math.cosh(Math.toRadians(angle))*vectorSizeDouble(split);
		sizes[1]=Math.sinh(Math.toRadians(angle))*vectorSizeDouble(split);
		a[0]=resizeVector((Vector<Double>) on.clone(), sizes[0]);
		int angle1=90;
		if(angle>0 || angle<Math.PI) angle1*=-1;
		a[1]=resizeVector(rotateZ((Vector<Double>) on.clone(),Math.toRadians(angle1)), sizes[1]);
		return a;
	}
	public static Vector<Double> resizeToUnit(Vector<Double> v, double cap){
		return multiplyVectorWithSkalar(v, (1*cap)/vectorSizeDouble(v));
	}
	private static Vector<Double> rotateZ(Vector<Double> vector,double angle) { // angle in radians
		  double x1 = vector.get(0) * Math.cos(angle) - vector.get(1) * Math.sin(angle);
		  double y1 = vector.get(0) * Math.sin(angle) + vector.get(1) * Math.cos(angle) ;
		  Vector<Double> v=new Vector<Double>();
		  v.add(x1);
		  v.add(y1);
		  return v;
		}
	/*
	 * prever ce se seka-> A-----T---B  (AT+TB)=AB
	 * 
	 * input: A, B, T
	 */
	public static boolean checkIntersection(Vector<Double> a, Vector<Double> b, Vector<Double> t, double error) {
		double dAB=dstanceBetweenPoints(a, b);
		double dAT=dstanceBetweenPoints(a, t);
		double dTB=dstanceBetweenPoints(t, b);
		if(dAB-(dAT+dTB)>error*-1  && dAB-(dAT+dTB)<error){ //12=ball width
			System.out.println("collided in check intersection");
			return true;
		}else{
			return false;
		}
	}
	public static double determinant(double a, double b, double c){
		return (b*b)-(4*a*c);
	}
	public static double dstanceBetweenPoints(Vector<Double> v1, Vector<Double>v2){
		return Math.hypot(v1.get(0)-v2.get(0), v1.get(1)-v2.get(1));
	}
	public static Vector<Double> createCounterForceVector(Vector<Double> speed, double size) {
		@SuppressWarnings("unchecked")
		Vector<Double> newVector=(Vector<Double>) speed.clone();
		newVector=resizeVector(multiplyVectorWithSkalar(newVector, -1.0), size);
		return newVector;
	}
	public static Vector<Integer> resizeVector(Vector<Integer> v, int size) {
		return doubleToInt(multiplyVectorWithSkalar(intToDouble(v), size/vectorSizeDouble(intToDouble(v))));
	}
	public static Vector<Double> intToDouble(Vector<Integer> v){
		Vector<Double> v1 = new Vector<>();
		for(Integer x : v) {
			v1.add((double) x );
		}
		return v1;
	}
	public static Vector<Integer> doubleToInt(Vector<Double> v){
		Vector<Integer> v1 = new Vector<>();
		for(Double d : v) {
			v1.add((int) Math.round(d));
		}
		return v1;
	}
	

	
	
	
}
