package AppFrontend.src.main.java.servlet.modelo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import AppFrontend.src.main.java.servlet.modelo.DTO.Departamento;

public class TestJSONDepartamento {

	private static URL url;
	private static String sitio = "http://localhost:5000/";
//			private static String sitio = "http://localhost:8080/Back_PapeleriaWWW-0.0.1-SNAPSHOT/";

	public static ArrayList<Departamento> parsingDepartamento(String json) throws ParseException {// devulve un arraylist
		JSONParser jsonParser = new JSONParser();
		ArrayList<Departamento> lista = new ArrayList<Departamento>();
		JSONArray departamentos = (JSONArray) jsonParser.parse(json);
		Iterator i = departamentos.iterator(); // recorrer la tabla cliente
		while (i.hasNext()) {
			JSONObject innerObj = (JSONObject) i.next();
			Departamento departamento = new Departamento();
			departamento.setIdCiudad(Long.parseLong(innerObj.get("idCiudad").toString())); // convertir de String
																								// a Long
			departamento.setNomDepartamento(innerObj.get("nomDepartamento").toString());
			departamento.setCodPais(innerObj.get("codPais").toString());			
			lista.add(departamento);
		}
		return lista;
	}

	// listar la informacion
	public static ArrayList<Departamento> getJSONClientes() throws IOException, ParseException { // devolver un listado JSON

		url = new URL(sitio + "departamento/listar"); // trae el metodo de Clientes.API
		HttpURLConnection http = (HttpURLConnection) url.openConnection();

		http.setRequestMethod("GET");
		http.setRequestProperty("Accept", "application/json");

		InputStream respuesta = http.getInputStream();
		byte[] inp = respuesta.readAllBytes();
		String json = "";

		for (int i = 0; i < inp.length; i++) {
			json += (char) inp[i];
		}

		ArrayList<Departamento> lista = new ArrayList<Departamento>();
		lista = parsingDepartamento(json);
		http.disconnect();
		return lista;
	}

	public static ArrayList<Departamento> getJSONClientes(Long id) throws IOException, ParseException { // devolver un
																									// listado JSON

		url = new URL(sitio + "departamento/listar"); // trae el metodo de Usuarios.API
		HttpURLConnection http = (HttpURLConnection) url.openConnection();

		http.setRequestMethod("GET");
		http.setRequestProperty("Accept", "application/json");

		InputStream respuesta = http.getInputStream();
		byte[] inp = respuesta.readAllBytes();
		String json = "";

		for (int i = 0; i < inp.length; i++) {
			json += (char) inp[i];
		}
		ArrayList<Departamento> listaTemporal = new ArrayList<Departamento>();
		ArrayList<Departamento> lista = new ArrayList<Departamento>();
		listaTemporal = parsingDepartamento(json);

		for (Departamento departamento : listaTemporal) {
			if (departamento.getIdCiudad() == id) {
				lista.add(departamento);
			}
		}
		http.disconnect();
		return lista;
	}

	public static Departamento getJSONCliente(Long id) throws IOException, ParseException { // devolver un
		// listado JSON

		url = new URL(sitio + "departamento/listar"); // trae el metodo de Usuarios.API
		HttpURLConnection http = (HttpURLConnection) url.openConnection();

		http.setRequestMethod("GET");
		http.setRequestProperty("Accept", "application/json");

		InputStream respuesta = http.getInputStream();
		byte[] inp = respuesta.readAllBytes();
		String json = "";

		for (int i = 0; i < inp.length; i++) {
			json += (char) inp[i];
		}
		ArrayList<Departamento> listaTemporal = new ArrayList<Departamento>();
		Departamento lista = new Departamento();
		listaTemporal = parsingDepartamento(json);

		for (Departamento departamento : listaTemporal) {
			if (departamento.getIdCiudad() == id) {
				lista = departamento;
			}
		}
		http.disconnect();
		return lista;
	}

	public static int postJSON(Departamento departamento) throws IOException {

		url = new URL(sitio + "departamento/guardar");
		HttpURLConnection http;
		http = (HttpURLConnection) url.openConnection();

		try {
			http.setRequestMethod("POST");
		} catch (ProtocolException e) {
			e.printStackTrace();
		}

		http.setDoOutput(true);
		http.setRequestProperty("Accept", "application/json");
		http.setRequestProperty("Content-Type", "application/json");

		String data = "{" + "\"idCiudad\":\"" + String.valueOf(departamento.getIdCiudad())
				+ "\",\"nomDepartamento\": \"" + departamento.getNomDepartamento() 
				+ "\",\"codPais\": \""	+ departamento.getCodPais() 				
				+ "\"}";
		
		
		byte[] out = data.getBytes(StandardCharsets.UTF_8);
		OutputStream stream = http.getOutputStream();
		stream.write(out);

		int respuesta = http.getResponseCode();
		http.disconnect();
		return respuesta;
	}

	public static int putJSON(Departamento departamento, Long id) throws IOException {

		url = new URL(sitio + "departamento/actualizar");
		HttpURLConnection http;
		http = (HttpURLConnection) url.openConnection();

		try {
			http.setRequestMethod("PUT");
		} catch (ProtocolException e) {
			e.printStackTrace();
		}

		http.setDoOutput(true);
		http.setRequestProperty("Accept", "application/json");
		http.setRequestProperty("Content-Type", "application/json");

		String data = "{" + "\"idCiudad\":\"" + id + "\",\"nomDepartamento\": \"" + departamento.getNomDepartamento()
				+ "\",\"codPais\": \"" + departamento.getCodPais() +  "\"}";
		byte[] out = data.getBytes(StandardCharsets.UTF_8);
		OutputStream stream = http.getOutputStream();
		stream.write(out);

		int respuesta = http.getResponseCode();
		http.disconnect();
		return respuesta;
	}

	public static int deleteJSONDepartamento(Long id) throws IOException {

		url = new URL(sitio + "departamento/eliminar/" + id);
		HttpURLConnection http;
		http = (HttpURLConnection) url.openConnection();

		try {
			http.setRequestMethod("DELETE");
		} catch (ProtocolException e) {
			e.printStackTrace();
		}

		http.setDoOutput(true);
		http.setRequestProperty("Accept", "application/json");
		http.setRequestProperty("Content-Type", "application/json");

		int respuesta = http.getResponseCode();
		http.disconnect();
		return respuesta;
	}

}
