package datos;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;
import javax.swing.JOptionPane;

public class PoolConexion  
{
	// El patron Singleton exige que los atributos y los metodos sean declarados STATIC y PRIVADOS
	private static PoolConexion INSTANCE = null; // Crea una instancia de tipo PooConexio en null 
	private static Connection con = null; // Crea un objeto de tipo conection
	private static DataSource dataSource; // Crea un objeto de tipo dataSource
	private static String db = "HR"; //Variable con el nombre de la base de datos
	private static String url = "jdbc:postgresql://localhost:5432/"+db; //Varaible con la url de la base de datos
	private static String user = "postgres";// Variables con el username de la base de datos
	private static String pass = "Usuario123#."; // Contraseña de la base de datos
	
	private PoolConexion() // Contructor de la clase
    {
		inicializaDataSource(); // Instancia el datasource (POOL)
    }
	
	private synchronized static void createInstance() // Este metodo asigna a la instancia <<Instancia>> la instancia del objeto pool
	{
		if(INSTANCE==null)
		{
			INSTANCE = new PoolConexion();
		}
	}
	
	public static PoolConexion getInstance() // Este metodo retorna la instancia única del objeto (POOL)
	{
		if(INSTANCE == null)
		{
			createInstance();
		}
		return INSTANCE;
	}
	
	

	 public final void inicializaDataSource() // Este metodo asigna los valores de fabrica para la creacion del origen de datos de conexion
	    {

		 	org.apache.commons.dbcp.BasicDataSource basicDataSource = new org.apache.commons.dbcp.BasicDataSource();
	        basicDataSource.setDriverClassName("org.postgresql.Driver"); // Se especifica el driver de conexion
	        basicDataSource.setUsername(user); // Se asigna el usario
	        basicDataSource.setPassword(pass); // Se asigna la contraseña
	        basicDataSource.setUrl(url); // Se asigna la url de la BD
	        basicDataSource.setMaxActive(50); // Se asigna un maximo de 50 hilos de conexion a la BD
	        dataSource = basicDataSource; // Al objeto de tipo DataSource previamente instanciado se le asigna la fabrica del Pool de Conexiones
	    }
	 
	 public static Connection getConnection() // Con este metodo se obtiene un hilo de conexion activo
	    {	
		   if (EstaConectado()==false) 
			   {
			   		try 
			   		{
						con = PoolConexion.dataSource.getConnection();
					} 
			   		catch (SQLException e) 
			   		{
						// TODO Auto-generated catch block
			   			e.printStackTrace();
			   			System.out.println(e.getMessage());
					}
			   }
		   return con;
	    }
	
	 
	 public static boolean EstaConectado() // Con este metodo se valida si existe una conexion a la BD Activa
	    {
	    	boolean resp = false;
	    	try
	    	{
	    		con = PoolConexion.dataSource.getConnection();
	    		if ((con==null) || (con.isClosed()))
	    			resp = false;
	    		else
	    			resp = true;
	    	}
	    	catch(Exception e)
	    	{
	    		e.printStackTrace();
	    		System.out.println(e.getMessage());
	    	}
	    	
	    	return resp;
	    }

	       
	    
	    public static void main(String[] args) throws SQLException // Es la comprobacion de la implementacion del API del Pool de Conexion con el patron Singleton
		{
			// TODO Auto-generated method stub
			PoolConexion.getInstance();
			Connection con = null;
	        
	        try 
	        {
		    	con = PoolConexion.getConnection();
		    	if(con!=null)
		    	{
		    		JOptionPane.showMessageDialog(null, "Conectado a " + db +" !");
		    		System.out.println("Conectado a "+db+" !!!");
		    	}
		    	else
		    	{
		    		JOptionPane.showMessageDialog(null, "Error al Conectar a "+db+" !!!");
		    		System.out.println("Error al Conectar a "+db+" !!!");
		    	}
	        }
	        finally
	        {
	            try 
	            {
	               con.close();
	               System.out.println("Se desconectó de "+db+"!!!");
	            } 
	            catch (SQLException ex) 
	            {
	            	ex.printStackTrace();
	                System.out.println(ex.getMessage());
	            }
	        }

		}
	
}
