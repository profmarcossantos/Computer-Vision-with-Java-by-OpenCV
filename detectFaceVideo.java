/*
 * Criado pelo Prof. Marcos R. Santos (marcos.santos@imed.edu.br)
 * Aplicado no Projeto HackDay
 * da Faculdade Meridional (IMED - Passo Fundo)
 */
//Bibliotecas Utilziadas 
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacv.CanvasFrame;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_objdetect.*;

import org.bytedeco.javacpp.opencv_highgui;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacpp.opencv_objdetect.CvHaarClassifierCascade;

public class detectFaceVideo {

	// variáveis criadas para analisar o eixo de movimentação da face!
	static int analisaX = 0;
	static int analisaY = 0;

	public static void main(String[] args) throws Exception {

		// Load da classe que importa o detector de objetos por paramentro.

		Loader.load(opencv_objdetect.class);

		// Variavel que recebe o caminho do XML treinado para detecção do rosto
		// de frente
		String XML_FILE = "resources/haarcascade_frontalface_default.xml";
		// Criando um objeto utilizado para comparar a imagem (foto ou vídeo)
		// com os dados do XML
		CvHaarClassifierCascade faceClassifier = new CvHaarClassifierCascade(
				cvLoad(XML_FILE));
		// criando um objeto que recebe o conteúdo da Web Cam (utilize o 0 ou
		// 1,2..para mudar a web cam a ser utilizada
		CvCapture capture = opencv_highgui.cvCreateCameraCapture(0);

		// Definindo a resolução da imagem - Altura: 320 x Altura: 480

		opencv_highgui.cvSetCaptureProperty(capture,
				opencv_highgui.CV_CAP_PROP_FRAME_HEIGHT, 320);
		opencv_highgui.cvSetCaptureProperty(capture,
				opencv_highgui.CV_CAP_PROP_FRAME_WIDTH, 480);

		// Criando o objeto grabbedImage para pegar cada frame da camera!

		// Criando o mirrorImage e clonando o frame da web cam capturado!
		IplImage grabbedImage = opencv_highgui.cvQueryFrame(capture);
		
		// Criando o mirrorImage e clonando o frame da web cam capturado!
		IplImage mirrorImage = grabbedImage.clone();

		// Criando uma imagem em tom de cinza (mais fácil para analisar (diminui o ruido)
		IplImage grayImage = IplImage.create(mirrorImage.width(),
				mirrorImage.height(), IPL_DEPTH_8U, 1);

		// Como o frameword é em C, é necessário alocar memória para a compilação
		CvMemStorage faceStorage = CvMemStorage.create();

		// Criando um CanvaFrame, formulário de saída
		CanvasFrame frame = new CanvasFrame("HackDay - Detect face", 1);

		// Entra em looping infinito enquanto tiver imagens na camera
		while (frame.isVisible()
				&& (grabbedImage = opencv_highgui.cvQueryFrame(capture)) != null) {

			// Limpa a memória para processar só o que é novo
			cvClearMemStorage(faceStorage);

			// Inverte a imagem, para parecer mais natural ( o normal é invertido)
			cvFlip(grabbedImage, mirrorImage, 1);
			
			// Criar uma imagem em preto e branco - o melhor para a detecção de rosto
			cvCvtColor(mirrorImage, grayImage, CV_BGR2GRAY);

			// Localiza o rosto na imagem e marca com um verde
			// Parametro (XML - Memória - Cor - Imagem Cinza - Imagem de Saida)
			findAndMarkObjects(faceClassifier, faceStorage, CvScalar.GREEN,
					grayImage, mirrorImage);

			// Mostra na tela a imagem altera com o marcador
			frame.showImage(mirrorImage);
		}

		// Fecha o Frame
		frame.dispose();
		//nova Captura
		opencv_highgui.cvReleaseCapture(capture);
	}

	//função para marcar na tela os objetos detectados
	
	private static void findAndMarkObjects(CvHaarClassifierCascade classifier,
			CvMemStorage storage, CvScalar colour, IplImage inImage,
			IplImage outImage) {
		
		// detecta as faces na imagem e retorna uma lista
		CvSeq faces = cvHaarDetectObjects(inImage, classifier, storage, 1.1, 3,
				CV_HAAR_DO_CANNY_PRUNING);
		// contador de faces para loop abaixo
		int totalFaces = faces.total();
		// System.out.println("Total Faces:"+ totalFaces );
		//marca quantas faces foram encotradas
		
		for (int i = 0; i < totalFaces; i++) {
			// cria um objeto de retangulo com as coordenadas
			CvRect r = new CvRect(cvGetSeqElem(faces, i));
			// pega o tamanho e as coordeadas iniciais
			int x = r.x(), y = r.y(), w = r.width(), h = r.height();
			// marca na imagem os pontos
			
			cvRectangle(outImage, cvPoint(x, y), cvPoint(x + w, y + h), colour,
					1, CV_AA, 0);
			// System.out.println("Eixo X:"+ x );
			// System.out.println("AnalisaX:"+ analisaX );
			// System.out.println("Eixo Y:"+ y );

			if (x > analisaX)
				System.out.println("->");
			if (x < analisaX)
				System.out.println("<-");
			if (x == analisaX)
				System.out.println("--");

			if (y > analisaY)
				System.out.println("^");
			if (y < analisaY)
				System.out.println("v");
			if (y == analisaY)
				System.out.println("--");

			analisaX = x;
			analisaY = y;
		}
	}

	/**
	 * Load a Haar classifier from its xml representation.
	 * 
	 * @param classifierName
	 *            Filename for the haar classifier xml.
	 * @return a Haar classifier object.
	 */
	private static CvHaarClassifierCascade loadHaarClassifier(
			String classifierName) {
		
		//função para fazer o load do XML
		CvHaarClassifierCascade classifier = new CvHaarClassifierCascade(
				cvLoad(classifierName));
		//se der erro!!
		if (classifier.isNull()) {
			System.err.println("Error loading classifier file \"" + classifier
					+ "\".");
			System.exit(1);
		}

		return classifier;
	}

}
