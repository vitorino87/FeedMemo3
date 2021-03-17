package controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

/**
 * Esta classe prepara a importação
 * 
 * @author tiago.lucas
 *
 */
public class Importador {
	private static final String TIPOMIME = "*/*";
	private static final String NOMEDOPROGRAMA = "FeedMemo3";
	Activity ac;

	public Importador(Activity ac) {
		this.ac = ac;
	}

	/**
	 * Abre o arquivo que será exportado
	 * 
	 * @return número 2, significa que trata-se de uma importação
	 */
	public int abrirArquivo() {
		// Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		// Intent intent = new Intent(Intent.ACTION_PICK);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		// intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setType(TIPOMIME);
		// intent.setType(TIPOMIME);
		// intent.setType("plain/text");
		ac.startActivityForResult(intent, 1);
		return 2;
	}

	/**
	 * Programa para realizar a importação do arquivo para a memória
	 * 
	 * @param requestCode
	 *            - codigo da requisição
	 * @param resultCode
	 *            - código do resultado obtido
	 * @param data
	 *            - dados retornados pela intent
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public void importar(Intent data, ControladorDoDB mc, String TABELA) {
		boolean verifica = true, verif = false;
		if (data != null) {
			Uri uri = null;
			uri = data.getData();
			Log.i(NOMEDOPROGRAMA, "Uri: " + uri.toString());
			try {
				InputStream is = ac.getContentResolver().openInputStream(uri);
				Reader rd = new InputStreamReader(is, StandardCharsets.UTF_8);// utilizando
																				// utf-8
				int ch;
				String text = "";
				while ((ch = rd.read()) != -1) {
					if (ch != 10) {
						if (ch != 13)
							text += String.valueOf((char) ch);
					} else {
						Long l = mc.inserirRow(text.substring(1, text.lastIndexOf(",") - 1), TABELA,
								Integer.valueOf(text.substring(text.lastIndexOf(",") + 1, text.length())));
						if (l == -1L)
							verifica = false; // checa se alguma inserção falhou
						verif = true; // checa se a iteração ocorreu nesse laço
						text="";
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				Toast.makeText(ac, "Deu erro: " + e.toString(), Toast.LENGTH_SHORT).show();
				verifica = verif = false;
			}

		}
		if (verifica && verif) {
			Toast.makeText(ac, "Importado com sucesso!", Toast.LENGTH_SHORT).show();
		} else if (!verifica) {
			Toast.makeText(ac, "Alguma ideia não foi importada", Toast.LENGTH_SHORT).show();
		}
	}
}
