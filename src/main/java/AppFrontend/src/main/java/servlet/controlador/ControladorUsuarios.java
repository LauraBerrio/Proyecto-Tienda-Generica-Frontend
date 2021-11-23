package AppFrontend.src.main.java.servlet.controlador;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import AppFrontend.src.main.java.servlet.modelo.TestJSONClientes;
import AppFrontend.src.main.java.servlet.modelo.TestJSONDetalleVentas;
import AppFrontend.src.main.java.servlet.modelo.TestJSONProductos;
import AppFrontend.src.main.java.servlet.modelo.TestJSONUsuarios;
import AppFrontend.src.main.java.servlet.modelo.TestJSONVentas;
import AppFrontend.src.main.java.servlet.modelo.DTO.Clientes;
import AppFrontend.src.main.java.servlet.modelo.DTO.DetalleVentas;
import AppFrontend.src.main.java.servlet.modelo.DTO.Productos;
import AppFrontend.src.main.java.servlet.modelo.DTO.Usuarios;
import AppFrontend.src.main.java.servlet.modelo.DTO.Ventas;

/**
 * Servlet implementation class ControladorUsuarios
 */
@WebServlet("/ControladorUsuarios")
public class ControladorUsuarios extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// ***********variables generales dentro de la clase controlador
	// *****************
	double totalapagar = 0, totalVenta = 0;
	long codProducto = 0;
	double valor_iva = 0 , subtotaliva = 0, acusubtotal = 0;
	double iva = 0;
	double subtotal = 0;
	double precio = 0;
	long numfac = 0;
	int cantidad = 0, item = 0;
	String descripcion;
	long cedulaCliente, cedulaUsuario;
	List<DetalleVentas> listaVentas = new ArrayList<DetalleVentas>();
	Usuarios usuarios = new Usuarios();
	DetalleVentas detalleVenta = new DetalleVentas();
	loginServlet login = new loginServlet();
	Usuarios usuarioLogeado;

		// *******************************************************************************
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControladorUsuarios() {
        super();
        Usuarios usuarioLogeado = login.validarUsuario(usuarios);
        // TODO Auto-generated constructor stub
    }
    
 // ***********Metodos locales para buscar clientes*****************
    
    public void buscarCliente(Long id, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			ArrayList<Clientes> listac = TestJSONClientes.getJSONClientes();
			for (Clientes clientes : listac) {
				if (clientes.getCedulaCliente() == (id)) {
					request.setAttribute("clienteSeleccionado", clientes);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
	public Clientes buscarClienteC(Long id, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Clientes clientereturn = null ;
		try {
			ArrayList<Clientes> listac = TestJSONClientes.getJSONClientes();
			for (Clientes clientes : listac) {
				if (clientes.getCedulaCliente() == (id)) {
					request.setAttribute("clienteSeleccionado", clientes);
					clientereturn =  clientes;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clientereturn;
	}

	public void buscarUsuario(Long cus, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			Usuarios usuario = TestJSONUsuarios.getJSONUsuario(cus);

			request.setAttribute("usuarioSeleccionado", usuario);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void buscarProducto(long cod, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			ArrayList<Productos> listap = TestJSONProductos.getJSONProductos();
			for (Productos productos : listap) {
				if (productos.getCodigoProducto() == (cod)) {
					request.setAttribute("productoSeleccionado", productos); // envio a ventas
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void buscarFactura(String numFact, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (numFact == null) {
			numfac = Integer.parseInt(numFact) + 1; // variable declarada arriba con valor 0
		} else {
			numfac = Integer.parseInt(numFact) + 1; // cuando ya tiene valor la variable
		}
		request.setAttribute("numerofactura", numfac);

	}

	public void grabarDetalle(Long numFact, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		for (int i = 0; i < listaVentas.size(); i++) {
			detalleVenta = new DetalleVentas();
			detalleVenta.setCodigoDetalleVenta(i + 1);
			detalleVenta.setCodigoVenta(numFact);
			detalleVenta.setCodigoProducto(listaVentas.get(i).getCodigoProducto());
			detalleVenta.setCantidadProducto(listaVentas.get(i).getCantidadProducto());
			detalleVenta.setValorTotal(listaVentas.get(i).getValorTotal());
			detalleVenta.setValorVenta(listaVentas.get(i).getValorVenta());
			detalleVenta.setValorIva(listaVentas.get(i).getValorIva());

			int respuesta = 0;
			try {
				respuesta = TestJSONDetalleVentas.postJSON(detalleVenta);
				PrintWriter write = response.getWriter();
				if (respuesta == 200) {
					System.out.println("Registros Grabados detalle ventas");
				} else {
					
				}
				write.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
// *******************************************************************************
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String menu = request.getParameter("menu");
		String accion = request.getParameter("accion");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		switch (menu) {
		case "menu":
			request.getRequestDispatcher("/menu.jsp").forward(request, response);
			break;
//******************************************CLIENTES*********************************************
					case "Clientes":
						if (accion.equals("Listar")) {
							try {
								ArrayList<Clientes> lista = TestJSONClientes.getJSONClientes();
								request.setAttribute("lista", lista);
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else if (accion.equals("Agregar")) {
							if (request.getParameter("txtcedula") != "" && request.getParameter("txtdireccion") != ""
									&& request.getParameter("txtemail") != "" && request.getParameter("txtnombre") != ""
									&& request.getParameter("txttelefono") != "" && request.getParameter("txtciudad") != "") {
								Clientes cliente = new Clientes();
								cliente.setCedulaCliente(Long.parseLong(request.getParameter("txtcedula")));
								cliente.setDireccionCliente(request.getParameter("txtdireccion"));
								cliente.setEmailCliente(request.getParameter("txtemail"));
								cliente.setNombreCliente(request.getParameter("txtnombre"));
								cliente.setTelefonoCliente(request.getParameter("txttelefono"));
								cliente.setCiudad(request.getParameter("txtciudad"));

								int respuesta = 0;
								try {
									respuesta = TestJSONClientes.postJSON(cliente);
									if (respuesta == 200) {
										out.println("<html><head><title>Success</title><style>"
												+ " .swal-icon--success:after, .swal-icon--success:before, .swal-icon--success__hide-corners {background: transparent}"
												+ " .swal-overlay{background-color: rgb(172, 77, 251, 0.45);}"
												+ " .swal-modal{background-color: rgba(0, 0, 0, 0.3); box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.3); font-family: 'Segoe UI', Tahoma, sans-serif}"
												+ "	.swal-title{color: #FFF; font-weight: 500;}"
												+ " .swal-text{color: #FFF; font-weight: 300;}"
												+ "	.swal-button{opacity: .8; transition: opacity 0.5s;}"
												+ "	.swal-button:hover{opacity: 1}" + "</style></head>");
										out.println("<body>");
										out.println(
												"<script src=\"https://unpkg.com/sweetalert/dist/sweetalert.min.js\"></script>");
										out.println("<script>"
												+ "	swal(\"Cliente creado exitosamente.\", \"\", \"success\", {closeOnClickOutside: false});"
												+ " const btnSwal = document.getElementsByClassName(\"swal-button\");"
												+ "	btnSwal[0].addEventListener(\"click\", () => {window.location=\"./Clientes.jsp\"})"
												+ "</script>");
										out.println("</body></html>");
									} else {
										out.println("<html><head><title>Error</title><style>"
												+ " .swal-icon--success:after, .swal-icon--success:before, .swal-icon--success__hide-corners {background: transparent}"
												+ " .swal-overlay{background-color: rgb(172, 77, 251, 0.45);}"
												+ " .swal-modal{background-color: rgba(0, 0, 0, 0.3); box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.3); font-family: 'Segoe UI', Tahoma, sans-serif}"
												+ "	.swal-title{color: #FFF; font-weight: 500;}"
												+ " .swal-text{color: #FFF; font-weight: 300;}"
												+ "	.swal-button{opacity: .8; transition: opacity 0.5s;}"
												+ "	.swal-button:hover{opacity: 1}" + "</style></head>");
										out.println("<body>");
										out.println("<script src=\"https://unpkg.com/sweetalert/dist/sweetalert.min.js\"></script>");
										out.println("<script>"
												+ "	swal(\"Error: " + respuesta +  "\", \"\", \"error\", {closeOnClickOutside: false});"
												+ " const btnSwal = document.getElementsByClassName(\"swal-button\");"
												+ "	btnSwal[0].addEventListener(\"click\", () => {window.location=\"./Productos.jsp\"})"
												+ "</script>");
										out.println("</body></html>");
									}
									out.close();
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								out.println("<html><head><title>Error</title><style>"
										+ " .swal-icon--success:after, .swal-icon--success:before, .swal-icon--success__hide-corners {background: transparent}"
										+ " .swal-overlay{background-color: rgb(172, 77, 251, 0.45);}"
										+ " .swal-modal{background-color: rgba(0, 0, 0, 0.3); box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.3); font-family: 'Segoe UI', Tahoma, sans-serif}"
										+ "	.swal-title{color: #FFF; font-weight: 500;}"
										+ " .swal-text{color: #FFF; font-weight: 300;}"
										+ "	.swal-button{opacity: .8; transition: opacity 0.5s;}"
										+ "	.swal-button:hover{opacity: 1}" + "</style></head>");
								out.println("<body>");
								out.println("<script src=\"https://unpkg.com/sweetalert/dist/sweetalert.min.js\"></script>");
								out.println("<script>" + "	swal(\"Faltan datos.\", \"\", \"error\", {closeOnClickOutside: false});"
										+ " const btnSwal = document.getElementsByClassName(\"swal-button\");"
										+ "	btnSwal[0].addEventListener(\"click\", () => {window.location=\"./Clientes.jsp\"})"
										+ "</script>");
								out.println("</body></html>");
								out.close();
							}
						} else if (accion.equals("Actualizar")) {
							if (request.getParameter("txtcedula") != "") {
								if (request.getParameter("txtcedula") != "" && request.getParameter("txtdireccion") != ""
										&& request.getParameter("txtemail") != "" && request.getParameter("txtnombre") != ""
										&& request.getParameter("txttelefono") != "" && request.getParameter("txtciudad") != "") {
									Clientes cliente = new Clientes();
									cliente.setCedulaCliente(Long.parseLong(request.getParameter("txtcedula")));
									cliente.setDireccionCliente(request.getParameter("txtdireccion"));
									cliente.setEmailCliente(request.getParameter("txtemail"));
									cliente.setNombreCliente(request.getParameter("txtnombre"));
									cliente.setTelefonoCliente(request.getParameter("txttelefono"));
									cliente.setCiudad(request.getParameter("txtciudad"));

									int respuesta = 0;
									try {
										respuesta = TestJSONClientes.putJSON(cliente, cliente.getCedulaCliente());

										if (respuesta == 200) {
											out.println("<html><head><title>Success</title><style>"
													+ " .swal-icon--success:after, .swal-icon--success:before, .swal-icon--success__hide-corners {background: transparent}"
													+ " .swal-overlay{background-color: rgb(172, 77, 251, 0.45);}"
													+ " .swal-modal{background-color: rgba(0, 0, 0, 0.3); box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.3); font-family: 'Segoe UI', Tahoma, sans-serif}"
													+ "	.swal-title{color: #FFF; font-weight: 500;}"
													+ " .swal-text{color: #FFF; font-weight: 300;}"
													+ "	.swal-button{opacity: .8; transition: opacity 0.5s;}"
													+ "	.swal-button:hover{opacity: 1}" + "</style></head>");
											out.println("<body>");
											out.println(
													"<script src=\"https://unpkg.com/sweetalert/dist/sweetalert.min.js\"></script>");
											out.println("<script>"
													+ "	swal(\"Cliente se actualizo correctamente.\", \"\", \"success\", {closeOnClickOutside: false});"
													+ " const btnSwal = document.getElementsByClassName(\"swal-button\");"
													+ "	btnSwal[0].addEventListener(\"click\", () => {window.location=\"./Clientes.jsp\"})"
													+ "</script>");
											out.println("</body></html>");
										} else {
											out.println("<html><head><title>Error</title><style>"
													+ " .swal-icon--success:after, .swal-icon--success:before, .swal-icon--success__hide-corners {background: transparent}"
													+ " .swal-overlay{background-color: rgb(172, 77, 251, 0.45);}"
													+ " .swal-modal{background-color: rgba(0, 0, 0, 0.3); box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.3); font-family: 'Segoe UI', Tahoma, sans-serif}"
													+ "	.swal-title{color: #FFF; font-weight: 500;}"
													+ " .swal-text{color: #FFF; font-weight: 300;}"
													+ "	.swal-button{opacity: .8; transition: opacity 0.5s;}"
													+ "	.swal-button:hover{opacity: 1}" + "</style></head>");
											out.println("<body>");
											out.println("<script src=\"https://unpkg.com/sweetalert/dist/sweetalert.min.js\"></script>");
											out.println("<script>"
													+ "	swal(\"Error: " + respuesta +  "\", \"\", \"error\", {closeOnClickOutside: false});"
													+ " const btnSwal = document.getElementsByClassName(\"swal-button\");"
													+ "	btnSwal[0].addEventListener(\"click\", () => {window.location=\"./Productos.jsp\"})"
													+ "</script>");
											out.println("</body></html>");
										}
										out.close();
									} catch (Exception e) {
										e.printStackTrace();
									}
								} else {
									out.println("<html><head><title>Error</title><style>"
											+ " .swal-icon--success:after, .swal-icon--success:before, .swal-icon--success__hide-corners {background: transparent}"
											+ " .swal-overlay{background-color: rgb(172, 77, 251, 0.45);}"
											+ " .swal-modal{background-color: rgba(0, 0, 0, 0.3); box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.3); font-family: 'Segoe UI', Tahoma, sans-serif}"
											+ "	.swal-title{color: #FFF; font-weight: 500;}"
											+ " .swal-text{color: #FFF; font-weight: 300;}"
											+ "	.swal-button{opacity: .8; transition: opacity 0.5s;}"
											+ "	.swal-button:hover{opacity: 1}" + "</style></head>");
									out.println("<body>");
									out.println("<script src=\"https://unpkg.com/sweetalert/dist/sweetalert.min.js\"></script>");
									out.println("<script>"
											+ "	swal(\"Faltan datos.\", \"\", \"error\", {closeOnClickOutside: false});"
											+ " const btnSwal = document.getElementsByClassName(\"swal-button\");"
											+ "	btnSwal[0].addEventListener(\"click\", () => {window.location=\"./Clientes.jsp\"})"
											+ "</script>");
									out.println("</body></html>");
									out.close();
								}

							} else {
								out.println("<html><head><title>Warning</title><style>"
										+ " .swal-icon--success:after, .swal-icon--success:before, .swal-icon--success__hide-corners {background: transparent}"
										+ " .swal-overlay{background-color: rgb(172, 77, 251, 0.45);}"
										+ " .swal-modal{background-color: rgba(0, 0, 0, 0.3); box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.3); font-family: 'Segoe UI', Tahoma, sans-serif}"
										+ "	.swal-title{color: #FFF; font-weight: 500;}"
										+ " .swal-text{color: #FFF; font-weight: 300;}"
										+ "	.swal-button{opacity: .8; transition: opacity 0.5s;}"
										+ "	.swal-button:hover{opacity: 1}" + "</style></head>");
								out.println("<body>");
								out.println("<script src=\"https://unpkg.com/sweetalert/dist/sweetalert.min.js\"></script>");
								out.println("<script>"
										+ "	swal(\"Ingrese cédula\", \"\", \"warning\", {closeOnClickOutside: false});"
										+ " const btnSwal = document.getElementsByClassName(\"swal-button\");"
										+ "	btnSwal[0].addEventListener(\"click\", () => {window.location=\"./Clientes.jsp\"})"
										+ "</script>");
								out.println("</body></html>");
								out.close();
							}

						} else if (accion.equals("Cargar")) {

							Long id = Long.parseLong(request.getParameter("id"));
							try {
								ArrayList<Clientes> lista1 = TestJSONClientes.getJSONClientes();
								System.out.println("Parametro: " + id);
								for (Clientes clientes : lista1) {
									if (clientes.getCedulaCliente() == id) {
										request.setAttribute("clienteSeleccionado", clientes);
										request.getRequestDispatcher("Controlador?menu=Clientes&accion=Listar").forward(request,
												response);
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else if (accion.equals("Eliminar")) {
							if (request.getParameter("txtcedula") != "") {
								Long id = Long.parseLong(request.getParameter("txtcedula"));
								int respuesta = 0;
								try {
									respuesta = TestJSONClientes.deleteJSONClientes(id);
									if (respuesta == 200) {
										out.println("<html><head><title>Success</title><style>"
												+ " .swal-icon--success:after, .swal-icon--success:before, .swal-icon--success__hide-corners {background: transparent}"
												+ " .swal-overlay{background-color: rgb(172, 77, 251, 0.45);}"
												+ " .swal-modal{background-color: rgba(0, 0, 0, 0.3); box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.3); font-family: 'Segoe UI', Tahoma, sans-serif}"
												+ "	.swal-title{color: #FFF; font-weight: 500;}"
												+ " .swal-text{color: #FFF; font-weight: 300;}"
												+ "	.swal-button{opacity: .8; transition: opacity 0.5s;}"
												+ "	.swal-button:hover{opacity: 1}" + "</style></head>");
										out.println("<body>");
										out.println(
												"<script src=\"https://unpkg.com/sweetalert/dist/sweetalert.min.js\"></script>");
										out.println("<script>"
												+ "	swal(\"El cliente fue eliminado.\", \"\", \"success\", {closeOnClickOutside: false});"
												+ " const btnSwal = document.getElementsByClassName(\"swal-button\");"
												+ "	btnSwal[0].addEventListener(\"click\", () => {window.location=\"./Clientes.jsp\"})"
												+ "</script>");
										out.println("</body></html>");
									} else {
										out.println("<html><head><title>Error</title><style>"
												+ " .swal-icon--success:after, .swal-icon--success:before, .swal-icon--success__hide-corners {background: transparent}"
												+ " .swal-overlay{background-color: rgb(172, 77, 251, 0.45);}"
												+ " .swal-modal{background-color: rgba(0, 0, 0, 0.3); box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.3); font-family: 'Segoe UI', Tahoma, sans-serif}"
												+ "	.swal-title{color: #FFF; font-weight: 500;}"
												+ " .swal-text{color: #FFF; font-weight: 300;}"
												+ "	.swal-button{opacity: .8; transition: opacity 0.5s;}"
												+ "	.swal-button:hover{opacity: 1}" + "</style></head>");
										out.println("<body>");
										out.println(
												"<script src=\"https://unpkg.com/sweetalert/dist/sweetalert.min.js\"></script>");
										out.println("<script>"
												+ "	swal(\"Cliente no encontrado.\", \"\", \"error\", {closeOnClickOutside: false});"
												+ " const btnSwal = document.getElementsByClassName(\"swal-button\");"
												+ "	btnSwal[0].addEventListener(\"click\", () => {window.location=\"./Usuarios.jsp\"})"
												+ "</script>");
										out.println("</body></html>");
										out.close();
									}
									out.close();
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								out.println("<html><head><title>Warning</title><style>"
										+ " .swal-icon--success:after, .swal-icon--success:before, .swal-icon--success__hide-corners {background: transparent}"
										+ " .swal-overlay{background-color: rgb(172, 77, 251, 0.45);}"
										+ " .swal-modal{background-color: rgba(0, 0, 0, 0.3); box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.3); font-family: 'Segoe UI', Tahoma, sans-serif}"
										+ "	.swal-title{color: #FFF; font-weight: 500;}"
										+ " .swal-text{color: #FFF; font-weight: 300;}"
										+ "	.swal-button{opacity: .8; transition: opacity 0.5s;}"
										+ "	.swal-button:hover{opacity: 1}" + "</style></head>");
								out.println("<body>");
								out.println("<script src=\"https://unpkg.com/sweetalert/dist/sweetalert.min.js\"></script>");
								out.println("<script>"
										+ "	swal(\"Ingrese cédula\", \"\", \"warning\", {closeOnClickOutside: false});"
										+ " const btnSwal = document.getElementsByClassName(\"swal-button\");"
										+ "	btnSwal[0].addEventListener(\"click\", () => {window.location=\"./Clientes.jsp\"})"
										+ "</script>");
								out.println("</body></html>");
								out.close();
							}
						} else if (accion.equals("Consultar")) {
							if (request.getParameter("txtcedula") != "") {
								Long id = Long.parseLong(request.getParameter("txtcedula"));
								try {
									ArrayList<Clientes> lista1 = TestJSONClientes.getJSONClientes(id);
									if (!lista1.isEmpty()) {

										for (Clientes cliente : lista1) {
											if (cliente.getCedulaCliente() == id) {
												request.setAttribute("clienteSeleccionado", cliente);
												request.setAttribute("lista", lista1);

											}
										}
									} else {
										out.println("<html><head><title>Error</title><style>"
												+ " .swal-icon--success:after, .swal-icon--success:before, .swal-icon--success__hide-corners {background: transparent}"
												+ " .swal-overlay{background-color: rgb(172, 77, 251, 0.45);}"
												+ " .swal-modal{background-color: rgba(0, 0, 0, 0.3); box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.3); font-family: 'Segoe UI', Tahoma, sans-serif}"
												+ "	.swal-title{color: #FFF; font-weight: 500;}"
												+ " .swal-text{color: #FFF; font-weight: 300;}"
												+ "	.swal-button{opacity: .8; transition: opacity 0.5s;}"
												+ "	.swal-button:hover{opacity: 1}" + "</style></head>");
										out.println("<body>");
										out.println(
												"<script src=\"https://unpkg.com/sweetalert/dist/sweetalert.min.js\"></script>");
										out.println("<script>"
												+ "	swal(\"Cliente no existente.\", \"\", \"error\", {closeOnClickOutside: false});"
												+ " const btnSwal = document.getElementsByClassName(\"swal-button\");"
												+ "	btnSwal[0].addEventListener(\"click\", () => {window.location=\"./Clientes.jsp\"})"
												+ "</script>");
										out.println("</body></html>");
										out.close();
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								out.println("<html><head><title>Warning</title><style>"
										+ " .swal-icon--success:after, .swal-icon--success:before, .swal-icon--success__hide-corners {background: transparent}"
										+ " .swal-overlay{background-color: rgb(172, 77, 251, 0.45);}"
										+ " .swal-modal{background-color: rgba(0, 0, 0, 0.3); box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.3); font-family: 'Segoe UI', Tahoma, sans-serif}"
										+ "	.swal-title{color: #FFF; font-weight: 500;}"
										+ " .swal-text{color: #FFF; font-weight: 300;}"
										+ "	.swal-button{opacity: .8; transition: opacity 0.5s;}"
										+ "	.swal-button:hover{opacity: 1}" + "</style></head>");
								out.println("<body>");
								out.println("<script src=\"https://unpkg.com/sweetalert/dist/sweetalert.min.js\"></script>");
								out.println("<script>"
										+ "	swal(\"Ingrese cédula\", \"\", \"warning\", {closeOnClickOutside: false});"
										+ " const btnSwal = document.getElementsByClassName(\"swal-button\");"
										+ "	btnSwal[0].addEventListener(\"click\", () => {window.location=\"./Clientes.jsp\"})"
										+ "</script>");
								out.println("</body></html>");
								out.close();
							}
						} else if (accion.equals("Mostrar Todo")) {
							try {
								ArrayList<Clientes> lista = TestJSONClientes.getJSONClientes();
								request.setAttribute("lista", lista);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						request.getRequestDispatcher("/Clientes.jsp").forward(request, response);
						break;
//*******************************************VENTAS**********************************************
					case "Ventas":
// **************** enviaremos la cedula del usuario al formulario ventas
						request.setAttribute("usuarioSeleccionado", usuarios);
// **********************************************************************************************
// ******************** enviaremos la Numero de Factura
						request.setAttribute("numerofactura", numfac);
// **********************************************************************************************
						if (accion.equals("BuscarCliente")) {
							String id = request.getParameter("cedulacliente");// como esta en ventas
							this.buscarCliente(Long.parseLong(id), request, response);
						} else if (accion.equals("BuscarProducto")) {
							String id = request.getParameter("cedulacliente");// como esta en ventas y lo repite
							this.buscarCliente(Long.parseLong(id), request, response);

							
							String cod = request.getParameter("codigoproducto");// como esta en ventas
							this.buscarProducto(Long.parseLong(cod), request, response);

						} else if (accion.equals("AgregarProducto")) {
							try {
								String id = request.getParameter("cedulacliente");// como esta en ventas y lo repite
								this.buscarCliente(Long.parseLong(id), request, response);
								

								detalleVenta = new DetalleVentas();
								item++; // contador
								acusubtotal = 0;
								subtotaliva = 0;
								totalapagar = 0;
								codProducto = Long.parseLong(request.getParameter("codigoproducto"));
								descripcion = request.getParameter("nombreproducto");
								precio = Double.parseDouble(request.getParameter("precioproducto"));
								cantidad = Integer.parseInt(request.getParameter("cantidadproducto"));
								iva = Double.parseDouble(request.getParameter("ivaproducto"));
								String numFact = request.getParameter("numerofactura");

								subtotal = (precio * cantidad);
								valor_iva = subtotal * iva / 100;
								// almacena temporalmente cada producto
								detalleVenta.setCodigoDetalleVenta(item);
								detalleVenta.setCodigoProducto(codProducto);
								detalleVenta.setDescripcionProducto(descripcion);
								detalleVenta.setPrecioProducto(precio);
								detalleVenta.setCantidadProducto(cantidad);
								detalleVenta.setCodigoVenta(numfac);
								detalleVenta.setValorIva(valor_iva);
								detalleVenta.setValorVenta(subtotal);
								listaVentas.add(detalleVenta);

								for (int i = 0; i < listaVentas.size(); i++) {
									acusubtotal += listaVentas.get(i).getValorVenta();
									subtotaliva += listaVentas.get(i).getValorIva();
								}
								totalapagar = acusubtotal + subtotaliva;
								detalleVenta.setValorTotal(totalapagar);
								String totalPagar = String.valueOf(totalapagar);
								// una vez hecho todos los calculos ahora hacemos el envio de la info al
								// formulario ventas seccion2
								request.setAttribute("listaventas", listaVentas);
								request.setAttribute("totalsubtotal", acusubtotal);
								request.setAttribute("totaliva", subtotaliva);
								request.setAttribute("totalapagar", totalPagar);
							} catch (Exception e) {
								// TODO: handle exception
							}
						} else if (accion.equals("GenerarVenta")) {
							cedulaCliente = Long.parseLong(request.getParameter("cedulacliente"));
							Clientes cliente = this.buscarClienteC(cedulaCliente, request, response);
							
							String numFact = request.getParameter("numerofactura");
							Ventas ventas = new Ventas();
							ventas.setCodigoVenta(Long.parseLong(numFact));
							ventas.setCedulaCliente(cedulaCliente);
							ventas.setNombreCliente(cliente.getNombreCliente());		
							ventas.setIvaVenta(subtotaliva);
							ventas.setValorVenta(acusubtotal);
							ventas.setTotalVenta(totalapagar);

							int respuesta = 0;
							try {
								respuesta = TestJSONVentas.postJSON(ventas);
								PrintWriter write = response.getWriter();
								if (respuesta == 200) {
									System.out.println("Grabacion Exitosa " + respuesta);
                                    //this.grabarDetalle(ventas.getCodigoVenta(), request, response);
									request.getRequestDispatcher("/Ventas.jsp").forward(request, response);
								} else {
									write.println("error ventas");
								}
								write.close();
							} catch (Exception e) {
								e.printStackTrace();
							}

						} else {
							// *********** muestro factura por primera vez ******
							String factura = "0"; // request.setAttribute("numerofactura");
							// String factura= request.gesetAttribute("numerofactura");
							this.buscarFactura(factura, request, response);
						}
						request.getRequestDispatcher("/Ventas.jsp").forward(request, response);
						break;
						
					case "Salir":
						request.getRequestDispatcher("/login.jsp").forward(request, response);
						break;
		}
	}
}
