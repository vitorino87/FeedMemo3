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
				// n�o estou armazenando o campo id, porque ele �
				// auto-increment�vel e chave prim�ria
				// abaixo, verifica se o campo � integer e converte para String
				if (cursor.getType(j) == Cursor.FIELD_TYPE_INTEGER && !cursor.getColumnName(j).equals(CHAVEPRIMARIA))
					csv += String.valueOf(cursor.getString(j));
				else if (cursor.getType(j) == Cursor.FIELD_TYPE_STRING) // verificando
																		// se o
																		// tipo
																		// �
																		// String
					csv += "\"" + cursor.getString(j) + "\""; // armazenando o
																// campo ideia
				if (!cursor.getColumnName(j).equals(ULTIMACOLUNA) && !cursor.getColumnName(j).equals(CHAVEPRIMARIA))
					// verifica se a coluna � ideia e se n�o � a...
					// ...primeira coluna, pois a primeira coluna � id, isso
					// evita que ele adiciona v�rgula na frente da ideia
					csv += ","; // tag � o �ltimo campo, por isso n�o �
								// necess�rio adicionar virgula
			}
			cursor.moveToNext(); // movendo para o pr�ximo resultado
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
