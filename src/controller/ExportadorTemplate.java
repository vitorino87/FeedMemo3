package controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
	final String TIPOMIME="text/csv";   //esse é o tipo MIME que será exportado
	final String NOMEDOPROGRAMA="FeedMemo3";
	
	
	public ExportadorTemplate(Activity ac){ //no construtor desta classe estou adicionando a Activity que a chamou
		this.ac = ac;
	}		
	
	/**
	 * Método para invocar o "salvar como" do Android 
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public int salvarComo(){
		Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT); //invocando a intenção de criar um documento
		intent.addCategory(Intent.CATEGORY_OPENABLE); 			//adicionando a categoria
		intent.setType(TIPOMIME);								//adicionando o tipo MIME
		ac.startActivityForResult(intent, 1);			//invocando a intenção
		return 1;
	}		
	
	/**
	 * Esse método realiza a preparação do export.
	 * @param line: line to be save
	 * @param data: carrega a informação de onde salvar
	 * @return um Writer com o local storage alright set.
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public Writer prepararExport(Intent data){
		Uri uri = null;
		Writer fw2 = null;
		if(data!=null){     //verificando se a intent é nula
			uri = data.getData();  //adicionando os dados na URI
			Log.i(NOMEDOPROGRAMA, "Uri: "+ uri.toString());	//imprimindo informação no log				
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
