import java.util.Scanner;
public class teclado {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String frase = "";
		frase = scanner.nextLine();
		char analisa[] = frase.toCharArray();
		String saida="";
		String recebe="";
		int posicao = 0 ; //0 normal - 1 inicio
		for (int z = 0; z <= analisa.length -1 ; z++) {
			
			if (analisa[z]=='[') posicao = 1;if (analisa[z]==']') posicao = 0;
			if (posicao==0)
			{
				if(analisa[z]!='[' && analisa[z]!=']')
				{
					saida = recebe + saida +  analisa[z];
					recebe = "";
				}	
			}
			else
			{
				if(analisa[z]!='[' && analisa[z]!=']')
				{
					recebe+=analisa[z];
				}
			}
			
			if (z== analisa.length -1)
			{
				if (posicao==0)
				{
					saida = recebe + saida;
				}
				else
				{
					saida = saida + recebe;
				}
			}
		}
		System.out.println(saida);
		}
}