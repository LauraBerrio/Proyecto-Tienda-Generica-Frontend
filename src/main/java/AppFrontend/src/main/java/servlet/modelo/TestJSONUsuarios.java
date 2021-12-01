package AppFrontend.src.main.java.servlet.modelo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection; //esta libreria es para realizar la conexion
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import AppFrontend.src.main.java.servlet.modelo.DTO.Clientes;
import AppFrontend.src.main.java.servlet.modelo.DTO.Productos;
import AppFrontend.src.main.java.servlet.modelo.DTO.Proveedores;
import AppFrontend.src.main.java.servlet.modelo.DTO.Usuarios;

public class TestJSONUsuarios {
	private static URL url;
	private static String sitio = "http://localhost:5000/";
	//	private static String sitio = "http://localhost:8080/Back_PapeleriaWWW-0.0.1-SNAPSHOT/";

	// ***********************************************************************************************************************************************
	// modulo de Usuarios
	// ************************************************************************************************************************************************
	// agregar informacion a la tabla usuario

	public static ArrayList<Usuarios> parsingUsuarios(String json) throws ParseException {// devulve un arraylist
		JSONParser jsonParser = new JSONParser();
		ArrayList<Usuarios> lista = new ArrayList<Usuarios>();
		JSONArray usuarios = (JSONArray) jsonParser.parse(json);
		Iterator i = usuarios.iterator(); // recorrer la tabla usuario
		while (i.hasNext()) {
			JSONObject innerObj = (JSONObject) i.next();
			Usuarios usuario = new Usuarios();
			usuario.setCedulaUsuario(Long.parseLong(innerObj.get("cedulaUsuario").toString())); // convertir de String a
																								// Long
			usuario.setEmailUsuario(innerObj.get("emailUsuario").toString());
			usuario.setNombreUsuario(innerObj.get("nombreUsuario").toString());
			usuario.setPassword(innerObj.get("password").toString());
			usuario.setUsuario(innerObj.get("usuario").toString());
			usuario.setRol(innerObj.get("rol").toString());
			usuario.setIdCiudad(Integer.parseInt(innerObj.get("idCiudad").toString()));
			
			lista.add(usuario);
		}
		return lista;
	}

	// listar la informacion
	public static ArrayList<Usuarios> getJSONUsuarios() throws IOException, ParseException { // devolver un listado JSON

		url = new URL(sitio + "usuarios/listar"); // trae el metodo de Usuarios.API
		HttpURLConnection http = (HttpURLConnection) url.openConnection();

		http.setRequestMethod("GET");
		http.setRequestProperty("Accept", "application/json");

		InputStream respuesta = http.getInputStream();
		byte[] inp = respuesta.readAllBytes();
		String json = "";

		for (int i = 0; i < inp.length; i++) {
			json += (char) inp[i];
		}

		ArrayList<Usuarios> lista = new ArrayList<Usuarios>();
		lista = parsingUsuarios(json);
		http.disconnect();
		return lista;
	}

	public static ArrayList<Usuarios> getJSONUsuarios(Long id) throws IOException, ParseException { // devolver un
																									// listado JSON

		url = new URL(sitio + "usuarios/listar"); // trae el metodo de Usuarios.API
		HttpURLConnection http = (HttpURLConnection) url.openConnection();

		http.setRequestMethod("GET");
		http.setRequestProperty("Accept", "application/json");

		InputStream respuesta = http.getInputStream();
		byte[] inp = respuesta.readAllBytes();
		String json = "";

		for (int i = 0; i < inp.length; i++) {
			json += (char) inp[i];
		}
		ArrayList<Usuarios> listaTemporal = new ArrayList<Usuarios>();
		ArrayList<Usuarios> lista = new ArrayList<Usuarios>();
		listaTemporal = parsingUsuarios(json);

		for (Usuarios usuario : listaTemporal) {
			if (usuario.getCedulaUsuario() == id) {
				lista.add(usuario);
				break;
			}
		}

		http.disconnect();
		return lista;
	}

	public static Usuarios getJSONUsuario(Long id) throws IOException, ParseException { // devolver un
		// listado JSON

		url = new URL(sitio + "usuarios/listar"); // trae el metodo de Usuarios.API
		HttpURLConnection http = (HttpURLConnection) url.openConnection();

		http.setRequestMethod("GET");
		http.setRequestProperty("Accept", "application/json");

		InputStream respuesta = http.getInputStream();
		byte[] inp = respuesta.readAllBytes();
		String json = "";

		for (int i = 0; i < inp.length; i++) {
			json += (char) inp[i];
		}
		ArrayList<Usuarios> listaTemporal = new ArrayList<Usuarios>();
		Usuarios lista = new Usuarios();
		listaTemporal = parsingUsuarios(json);

		for (Usuarios usuario : listaTemporal) {
			if (usuario.getCedulaUsuario() == id) {
				lista = usuario;
			}
		}
		http.disconnect();
		return lista;
	}

	public static int postJSON(Usuarios usuario) throws IOException {

		url = new URL(sitio + "usuarios/guardar");
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

		String data = "{" + "\"cedulaUsuario\":\"" + String.valueOf(usuario.getCedulaUsuario())
				+ "\",\"emailUsuario\": \"" + usuario.getEmailUsuario() + "\",\"nombreUsuario\": \""
				+ usuario.getNombreUsuario() + "\",\"password\":\"" + usuario.getPassword() + "\",\"usuario\":\""
				+ usuario.getUsuario() + "\",\"rol\":\"" + usuario.getRol() + "\",\"idCiudad\":\"" + usuario.getIdCiudad() +"\"}";
		byte[] out = data.getBytes(StandardCharsets.UTF_8);
		OutputStream stream = http.getOutputStream();
		stream.write(out);

		int respuesta = http.getResponseCode();
		http.disconnect();
		return respuesta;
	}

	public static int putJSON(Usuarios usuario, Long id) throws IOException {

		url = new URL(sitio + "usuarios/actualizar");
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

		String data = "{" + "\"cedulaUsuario\":\"" + id + "\",\"emailUsuario\": \"" + usuario.getEmailUsuario()
				+ "\",\"nombreUsuario\": \"" + usuario.getNombreUsuario() + "\",\"password\":\"" + usuario.getPassword()
				+ "\",\"usuario\":\"" + usuario.getUsuario() +  "\",\"rol\":\"" + usuario.getRol() + "\",\"idCiudad\":\"" + usuario.getIdCiudad() + "\"}";
		byte[] out = data.getBytes(StandardCharsets.UTF_8);
		OutputStream stream = http.getOutputStream();
		stream.write(out);

		int respuesta = http.getResponseCode();
		http.disconnect();
		return respuesta;
	}

	public static int deleteJSONUsuarios(Long id) throws IOException {

		url = new URL(sitio + "usuarios/eliminar/" + id);
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