package AppFrontend.src.main.java.servlet.modelo.DTO;


public class Clientes {
	
	
	 private long cedulaCliente;
	
	 private String direccionCliente;
	
	 private String emailCliente;
	
	 private String nombreCliente;
	
	 private String telefonoCliente;
	 
	 private String ciudad;
	
	public long getCedulaCliente() {
		return cedulaCliente;
	}
	public void setCedulaCliente(long cedulaCliente) {
		this.cedulaCliente = cedulaCliente;
	}
	public String getDireccionCliente() {
		return direccionCliente;
	}
	public void setDireccionCliente(String direccionCliente) {
		this.direccionCliente = direccionCliente;
	}
	public String getEmailCliente() {
		return emailCliente;
	}
	public void setEmailCliente(String emailCliente) {
		this.emailCliente = emailCliente;
	}
	public String getNombreCliente() {
		return nombreCliente;
	}
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	public String getTelefonoCliente() {
		return telefonoCliente;
	}
	public void setTelefonoCliente(String telefonoCliente) {
		this.telefonoCliente = telefonoCliente;
	}
	public String getCiudad() {
		return ciudad;
	}
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	 
}
