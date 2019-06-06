package controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

public class ExportadorTemplate {
	
	Activity ac;
	final String TIPOMIME="text/csv";   //esse � o tipo MIME que ser� exportado
	final String NOMEDOPROGRAMA="FeedMemo3";
	
	
	public ExportadorTemplate(Activity ac){ //no construtor desta classe estou adicionando a Activity que a chamou
		this.ac = ac;
	}		
	
	/**
	 * M�todo para invocar o "salvar como" do Android 
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public int salvarComo(){
		Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT); //invocando a inten��o de criar um documento
		intent.addCategory(Intent.CATEGORY_OPENABLE); 			//adicionando a categoria
		intent.setType(TIPOMIME);								//adicionando o tipo MIME
		ac.startActivityForResult(intent, 1);			//invocando a inten��o
		return 1;
	}
	
	/**
	 * M�todo para exportar uma informa��o para o arquivo que foi criado pelo m�todo salvarComo()
	 * @param requestCode - codigo da requisi��o
	 * @param resultCode - c�digo do resultado obtido
	 * @param data - dados retornados pela intent
	 * @param info - as informa��es que ser�o exportadas
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public boolean exportar(int requestCode, int resultCode, Intent data, String info){
		boolean sucess = false;
		switch (requestCode) { //checando o c�digo de requisi��o                               
		case 1:
			if (resultCode == ac.RESULT_OK) { //verificando o c�digo do resultado
				Uri uri =null;
				if(data!=null){     //verificando se a intent � nula
					uri = data.getData();  //adicionando os dados na URI
					Log.i(NOMEDOPROGRAMA, "Uri: "+ uri.toString());	//imprimindo informa��o no log				
				}				
					try {
						OutputStream is = ac.getContentResolver().openOutputStream(uri); //capturando um outputstream do ContentResolver
						FileOutputStream fos = (FileOutputStream) is;     //realizando cast para FileOutputStream - 
																		//OutputStream � super classe da FileOutputStream 
						try {
							Writer fw2 = new OutputStreamWriter(fos, StandardCharsets.UTF_8); //Gerando um OutputStreamWriter, UTF-8
							fw2.append(info);
							//fw2.write(info); //gravando a informa��o no arquivo
							fw2.close(); 	 //fechando o writer
							sucess = true;
						} catch (IOException e) {
							e.printStackTrace();
						}																
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}														
			}
			break;
		}
		return sucess;
	}
	
	/**
	 * Esse m�todo realiza a prepara��o do export.
	 * @param line: line to be save
	 * @param data: carrega a informa��o de onde salvar
	 * @return um Writer com o local storage alright set.
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public Writer prepararExport(Intent data){
		Uri uri = null;
		Writer fw2 = null;
		if(data!=null){     //verificando se a intent � nula
			uri = data.getData();  //adicionando os dados na URI
			Log.i(NOMEDOPROGRAMA, "Uri: "+ uri.toString());	//imprimindo informa��o no log				
		}
		try {
			OutputStream is = ac.getContentResolver().openOutputStream(uri); //capturando um outputstream do ContentResolver
			FileOutputStream fos = (FileOutputStream) is;     //realizando cast para FileOutputStream - 
			fw2 = new OutputStreamWriter(fos, StandardCharsets.UTF_8); //Gerando um OutputStreamWriter, UTF-8																
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		return fw2;
	}
	
	/**
	 * salva a linha
	 * @param fw
	 * @param line
	 * @return
	 */
	public boolean salvar(Writer fw, String line){
		boolean ret = false;
		try{
			fw.append(line);
			ret = true;
		}catch(Exception e){
			Log.i(NOMEDOPROGRAMA, "Uri: "+ e.toString());
		}
		return ret;
	}
	
	/**
	 * fecha o writer
	 * @param fw
	 * @return
	 */
	public boolean close(Writer fw){
		boolean ret = false;
		try{
			fw.close();
			ret = true;
		}catch(Exception e){
			Log.i(NOMEDOPROGRAMA, "Uri: "+ e.toString());
		}
		return ret;
	}
}
