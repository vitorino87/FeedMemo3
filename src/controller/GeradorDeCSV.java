package controller;

import java.io.Writer;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.widget.Toast;

public class GeradorDeCSV {
	final String CHAVEPRIMARIA = "id";
	final String ULTIMACOLUNA = "tag";

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public String getCSV(ControladorDoDB mc, String TABELA) {
		mc.abrirConexao();
		mc.setTipoDeQuery(1);
		mc.retornarTodosResultados(TABELA);
		Cursor cursor = mc.getCursor();
		String csv = "";
		int qtdeColunas = cursor.getColumnCount();
		int qtdeDeRows = cursor.getCount();
		cursor.moveToFirst();
		for (int i = 0; i < qtdeDeRows; i++) {
			for (int j = 0; j < qtdeColunas; j++) {
				// não estou armazenando o campo id, porque ele é
				// auto-incrementável e chave primária
				// abaixo, verifica se o campo é integer e converte para String
				if (cursor.getType(j) == Cursor.FIELD_TYPE_INTEGER && !cursor.getColumnName(j).equals(CHAVEPRIMARIA))
					csv += String.valueOf(cursor.getString(j));
				else if (cursor.getType(j) == Cursor.FIELD_TYPE_STRING) // verificando
																		// se o
																		// tipo
																		// é
																		// String
					csv += "\"" + cursor.getString(j) + "\""; // armazenando o
																// campo ideia
				if (!cursor.getColumnName(j).equals(ULTIMACOLUNA) && !cursor.getColumnName(j).equals(CHAVEPRIMARIA))
					// verifica se a coluna é ideia e se não é a...
					// ...primeira coluna, pois a primeira coluna é id, isso
					// evita que ele adiciona vírgula na frente da ideia
					csv += ","; // tag é o último campo, por isso não é
								// necessário adicionar virgula
			}
			cursor.moveToNext(); // movendo para o próximo resultado
			csv += "\n"; // pulando linha
		}
		return csv;
	}

	public boolean salvar(ControladorDoDB mc, String TABELA, Activity ac, Intent data) {
		try {
			mc.abrirConexao();
			mc.setTipoDeQuery(1);
			mc.retornarTodosResultados(TABELA);
			Cursor cursor = mc.getCursor();
			String line = "";
			if (cursor.moveToFirst()) {
				ExportadorTemplate et = new ExportadorTemplate(ac);
				Writer wr = et.prepararExport(data);
				line = "\"" + cursor.getString(1) + "\"" + "," + cursor.getInt(2) + ((char) 10);
				et.salvar(wr, line);
				do { // "ideia",tag(LF)
					cursor.moveToNext();
					line = "\"" + cursor.getString(1) + "\"" + "," + cursor.getInt(2) + ((char) 10);
					et.salvar(wr, line);
				} while (!cursor.isLast());
				et.close(wr);
			}
			return true;
		} catch (Exception e) {
			Toast.makeText(ac, e.toString(), Toast.LENGTH_SHORT).show();
			return false;
		}
	}

}
