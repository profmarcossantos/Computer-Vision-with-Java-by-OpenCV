package br.com.detecface;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import org.bytedeco.javacpp.opencv_core.IplImage;
import static org.bytedeco.javacpp.opencv_objdetect.*;
 
public class detectFacePhoto{
	
	//Load haar classifier XML file
	public static final String XML_FILE = 
			"resources/haarcascade_frontalface_default.xml";
	
	public static void main(String[] args){
		
		//Load image
		IplImage img = cvLoadImage("resources/MLT_8369.jpg");		
		detect(img);		
	}	
	
	//Detect for face using classifier XML file 
	public static void detect(IplImage src){
		
		//Define classifier 
		CvHaarClassifierCascade cascade = new CvHaarClassifierCascade(cvLoad(XML_FILE));
		
		CvMemStorage storage = CvMemStorage.create();
		
		//Detect objects
		CvSeq sign = cvHaarDetectObjects(
				src,
				cascade,
				storage,
				1.5,
				3,
				CV_HAAR_DO_CANNY_PRUNING);
		
		cvClearMemStorage(storage);
		
		int total_Faces = sign.total();		
		
		//Draw rectangles around detected objects
		for(int i = 0; i < total_Faces; i++){
			CvRect r = new CvRect(cvGetSeqElem(sign, i));
			cvRectangle (
					src,
					cvPoint(r.x(), r.y()),
					cvPoint(r.width() + r.x(), r.height() + r.y()),
					CvScalar.RED,
					2,
					CV_AA,
					0);
			
		}
		
		//Display result
		cvShowImage("Result", src);
		cvWaitKey(0);
		
	}			
}